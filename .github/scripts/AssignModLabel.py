# -*- coding: utf-8 -*-
#!/usr/bin/env python
from collections import defaultdict
import difflib
import os
import random
import re
import requests

REPO_URL = "https://api.github.com/repos/Serilum/.issue-tracker"
MOD_DATA_URL = "https://workflow.serilum.com/mods/data/mod_data.min.json"

MISSING_LABEL = "Mod Label Missing"
SPECIAL_LABELS = {
	"Collective": "Library: Collective",
	"Bundles": "Bundles",
}

NAME_SEPARATORS = r"\s*(?:&|/|,|\+|\|)\s*|\s+(?:and|x|with)\s+"
MOD_URL_SLUG = r"(?:curseforge\.com/minecraft/mc-mods|modrinth\.com/(?:mod|datapack|plugin))/([a-z0-9\-]+)"
CRASH_LOG_SECTION = r"##\s*\*\*Crash Report / Latest Log\*\*.*?(?=\n##\s*\*\*|\Z)"

fprefix = " [Assign Mod Label] "


def githubHeaders():
	headers = {"Accept": "application/vnd.github+json"}
	token = os.environ.get("GITHUB_TOKEN", "")
	if token != "":
		headers["Authorization"] = "bearer " + token
	return headers


def githubGet(path):
	resp = requests.get(REPO_URL + path, headers=githubHeaders(), timeout=30)
	resp.raise_for_status()
	return resp.json()


def githubGetAllPages(path):
	results = []
	page = 1
	while True:
		pageResults = githubGet(path + "?per_page=100&page=" + str(page))
		if len(pageResults) == 0:
			return results

		results.extend(pageResults)
		page += 1


def githubPost(path, data):
	resp = requests.post(REPO_URL + path, json=data, headers=githubHeaders(), timeout=30)
	return resp


def githubDelete(path):
	resp = requests.delete(REPO_URL + path, headers=githubHeaders(), timeout=30)
	return resp


def modKey(name):
	lowered = name.lower().replace("&", "and").replace("’", "").replace("'", "")
	return re.sub(r"[^a-z0-9]", "", lowered)


def modWords(name):
	lowered = name.lower().replace("&", "and").replace("’", "").replace("'", "")
	return re.findall(r"[a-z0-9]+", lowered)


def buildModIndex(modNames, labelNames):
	allNames = list(modNames)
	for labelName in labelNames:
		if labelName.startswith("Mod: "):
			allNames.append(labelName[5:])

	allNames.extend(SPECIAL_LABELS.keys())

	byKey = {}
	for name in allNames:
		key = modKey(name)
		if key != "" and key not in byKey:
			byKey[key] = name

	namesPerAcronym = defaultdict(list)
	for name in byKey.values():
		words = modWords(name)
		if len(words) >= 3:
			acronym = "".join(word[0] for word in words)
			namesPerAcronym[acronym].append(name)

	acronyms = {}
	for acronym, names in namesPerAcronym.items():
		if len(names) == 1 and acronym not in byKey:
			acronyms[acronym] = names[0]

	patterns = []
	for name in sorted(byKey.values(), key=len, reverse=True):
		wordPattern = r"[\s\-_]*".join(re.escape(word) for word in modWords(name))
		pattern = r"(?<![a-z0-9])" + wordPattern + r"s?(?![a-z0-9])"
		patterns.append((name, re.compile(pattern)))

	return {
		"byKey": byKey,
		"acronyms": acronyms,
		"patterns": patterns,
	}


def lookupModKey(key, modIndex):
	byKey = modIndex["byKey"]

	variants = [key, key + "s"]
	if key.endswith("s"):
		variants.append(key[:-1])

	for variant in variants:
		if variant in byKey:
			return byKey[variant]

	return modIndex["acronyms"].get(key)


def releasedModNames(modData):
	modNames = []
	for modName, modEntry in modData.items():
		if modEntry.get("is_bundle") or modEntry.get("project_type") != "mod":
			continue

		if re.search(r"\(.*\)$", modName):
			continue

		modNames.append(modName)

	return modNames


def existingModLabel(modName, labelNames):
	if modName in SPECIAL_LABELS:
		return SPECIAL_LABELS[modName]

	wantedKey = modKey(modName)
	for labelName in labelNames:
		if labelName.startswith("Mod: ") and modKey(labelName[5:]) == wantedKey:
			return labelName

	return None


def resolveNameField(value, modIndex, allowSplit=True):
	if re.match(r"serilum.+bundle", value, re.IGNORECASE):
		return ["Bundles"], False

	slug = re.search(MOD_URL_SLUG, value, re.IGNORECASE)
	if slug:
		found = lookupModKey(modKey(slug.group(1)), modIndex)
		if found:
			return [found], False

	text = value.strip()
	markdownLink = re.match(r"^\[([^\]]+)\]\(.*\)$", text)
	if markdownLink:
		text = markdownLink.group(1)

	text = re.sub(r"[*`]", "", text).strip()
	text = re.sub(r"^serilum\S*\s+", "", text, flags=re.IGNORECASE)
	withoutVersion = re.split(r"[-_ ]v?\d+(?:\.\d+)+", text)[0]
	withoutBrackets = re.sub(r"\(.*?\)", "", withoutVersion)

	for variant in [text, withoutVersion, withoutBrackets]:
		found = lookupModKey(modKey(variant), modIndex)
		if found:
			return [found], False

	if allowSplit:
		parts = [part for part in re.split(NAME_SEPARATORS, text, flags=re.IGNORECASE) if part.strip() != ""]
		if len(parts) > 1:
			names = []
			isFuzzy = False
			for part in parts:
				partNames, partIsFuzzy = resolveNameField(part, modIndex, allowSplit=False)
				for name in partNames:
					if name not in names:
						names.append(name)

				isFuzzy = isFuzzy or partIsFuzzy

			if len(names) > 0:
				return names, isFuzzy

	key = modKey(withoutBrackets)
	if len(key) < 6:
		return [], False

	containedKeys = [modKeyEntry for modKeyEntry in modIndex["byKey"] if len(modKeyEntry) >= 8 and modKeyEntry in key]
	if len(containedKeys) > 0:
		longestKey = max(containedKeys, key=len)
		return [modIndex["byKey"][longestKey]], True

	candidateKeys = [modKeyEntry for modKeyEntry in modIndex["byKey"] if len(modKeyEntry) >= 6]
	closeKeys = difflib.get_close_matches(key, candidateKeys, n=1, cutoff=0.85)
	if len(closeKeys) > 0:
		return [modIndex["byKey"][closeKeys[0]]], True

	return [], False


def modsInText(text, modIndex, minLength):
	normalised = text.replace("&", "and").replace("’", "").replace("'", "")
	lowered = normalised.lower()

	hits = []
	for name, pattern in modIndex["patterns"]:
		if len(name) < minLength:
			continue

		match = None
		for candidate in pattern.finditer(lowered):
			if len(name) >= 8 or normalised[candidate.start()].isupper():
				match = candidate
				break

		if match is None:
			continue

		overlaps = any(match.start() < end and start < match.end() for start, end, _ in hits)
		if not overlaps:
			hits.append((match.start(), match.end(), name))

	hits.sort()
	return [name for _, _, name in hits]


def withoutCollective(names):
	others = [name for name in names if name != "Collective"]
	if len(others) > 0:
		return others
	return names


def nameFieldValues(body):
	values = []
	bodyLines = body.split("\n")
	for i, line in enumerate(bodyLines):
		if "mod name:" in line.lower():
			value = line.split(":", 1)[1].strip()
			if value.lower().startswith("mod "):
				value = value[4:].strip()

			if value != "" and value != "Mod":
				values.append(value)

		elif re.match(r"###\s+(?:first\s+|second\s+|third\s+)?mod\s+name", line, re.IGNORECASE):
			for nextLine in bodyLines[i + 1:]:
				nextLine = nextLine.strip()
				if nextLine == "":
					continue

				if not nextLine.startswith("#") and nextLine != "_No response_" and nextLine != "Mod":
					values.append(nextLine)
				break

	return values


def resolveIssueModNames(title, body, getCommentBodies, modIndex):
	names = []
	isFuzzy = False
	for value in nameFieldValues(body):
		valueNames, valueIsFuzzy = resolveNameField(value, modIndex)
		for name in valueNames:
			if name not in names:
				names.append(name)

		isFuzzy = isFuzzy or valueIsFuzzy

	if len(names) > 0:
		source = "name field (fuzzy)" if isFuzzy else "name field"
		return withoutCollective(names), source

	found = withoutCollective(modsInText(title, modIndex, 4))
	if len(found) > 0:
		return found, "title text"

	crashLogs = re.findall(CRASH_LOG_SECTION, body, flags=re.DOTALL)
	prose = re.sub(CRASH_LOG_SECTION, "", body, flags=re.DOTALL)

	found = withoutCollective(modsInText(prose, modIndex, 8))
	if len(found) > 0:
		return found[:1], "body text"

	found = withoutCollective(modsInText("\n".join(crashLogs), modIndex, 8))
	if len(found) > 0:
		return found[:1], "crash log"

	try:
		commentsText = "\n".join(getCommentBodies())
	except Exception:
		commentsText = ""

	found = withoutCollective(modsInText(commentsText, modIndex, 8))
	if len(found) > 0:
		return found[:1], "comment text"

	return [], None


def createModLabel(modName, modData, labelNames, isDryRun):
	labelName = "Mod: " + modName
	slug = modData.get(modName, {}).get("slug", "")

	print(fprefix + "Creating missing label '" + labelName + "'.")
	if isDryRun:
		return labelName

	resp = githubPost("/labels", {
		"name": labelName,
		"color": "%06x" % random.randint(0, 0xFFFFFF),
		"description": "https://curseforge.com/minecraft/mc-mods/" + slug,
	})

	if resp.status_code != 201 and resp.status_code != 422:
		resp.raise_for_status()

	labelNames.append(labelName)
	return labelName


def writeSummary(line):
	print(fprefix + line)

	summaryPath = os.environ.get("GITHUB_STEP_SUMMARY", "")
	if summaryPath != "":
		with open(summaryPath, "a", encoding="utf-8") as f:
			f.write(line + "\n")


def main():
	print("\n" + fprefix + "Starting.\n")

	issueNumber = os.environ["ISSUE_NUMBER"]
	isDryRun = os.environ.get("DRY_RUN", "") == "true"

	issue = githubGet("/issues/" + issueNumber)
	issueLabels = [label["name"] for label in issue["labels"]]
	if MISSING_LABEL not in issueLabels:
		writeSummary("#" + issueNumber + ": No '" + MISSING_LABEL + "' label, nothing to do.")
		return

	labelNames = [label["name"] for label in githubGetAllPages("/labels")]

	modData = requests.get(MOD_DATA_URL, timeout=30).json()
	modNames = releasedModNames(modData)
	modIndex = buildModIndex(modNames, labelNames)
	print(fprefix + "Loaded " + str(len(modNames)) + " mods and " + str(len(labelNames)) + " labels.")

	def getCommentBodies():
		comments = githubGetAllPages("/issues/" + issueNumber + "/comments")
		return [comment["body"] or "" for comment in comments]

	modNames, source = resolveIssueModNames(issue["title"] or "", issue["body"] or "", getCommentBodies, modIndex)
	if len(modNames) == 0:
		writeSummary("#" + issueNumber + ": No mod found, leaving '" + MISSING_LABEL + "'.")
		return

	targetLabels = []
	for modName in modNames:
		labelName = existingModLabel(modName, labelNames)
		if labelName is None:
			labelName = createModLabel(modName, modData, labelNames, isDryRun)

		if labelName not in targetLabels:
			targetLabels.append(labelName)

	writeSummary("#" + issueNumber + ": " + ", ".join(targetLabels) + " (found via " + source + ").")
	if isDryRun:
		return

	githubPost("/issues/" + issueNumber + "/labels", {"labels": targetLabels}).raise_for_status()

	resp = githubDelete("/issues/" + issueNumber + "/labels/" + MISSING_LABEL)
	if resp.status_code != 200 and resp.status_code != 404:
		resp.raise_for_status()

	print("\n" + fprefix + "Finished.")


if __name__ == "__main__":
	main()
