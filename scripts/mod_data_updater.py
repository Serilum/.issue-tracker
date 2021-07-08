# -*- coding: utf-8 -*-
#!/usr/bin/env python
from datetime 							import datetime
import urllib.request
import pathlib
import os
import json

# Updates mod file information via the CurseForge API.
# by Rick South.
def main():
	localpath = str(pathlib.Path(__file__).parent)
	url = "https://raw.githubusercontent.com/ricksouth/serilum-mc-mods/master/data/project_ids.json"

	alldata = "{"
	for decl in urllib.request.urlopen(url):
		line = decl.decode('utf-8')
		if not ":" in line:
			continue

		line = line.replace(",", "").replace('"', '').strip()
		linespl = line.split(" : ")

		modname = linespl[0]
		projectid = linespl[1]
		print(modname, projectid)

		modurl = "https://addons-ecs.forgesvc.net/api/v2/addon/" + projectid
		for mdecl in urllib.request.urlopen(modurl):
			moddata = mdecl.decode('utf-8')

			if alldata != "{":
				alldata += ","

			alldata += '"' + modname + '" : ' + moddata
			break

		print("Processed " + modname + ".")

	alldata += "}"
	print(alldata)

	with open('C:/The Forge/serilum-mc-mods/data/mod_data.json', 'w') as file:
		file.write(alldata)

	print("Mod data generated.")
	return

if __name__ == '__main__':
	main()