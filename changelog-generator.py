# -*- coding: utf-8 -*-
#!/usr/bin/env python
from datetime 							import datetime
import urllib.request
import pathlib
import os

# Generates a changelog file for each mod available on https://minecraft.natamus.com/
# by Rick South.
def main():
	localpath = str(pathlib.Path(__file__).parent)
	url = "https://raw.githubusercontent.com/ricksouth/serilum-mc-mods/master/README.md"

	for decl in urllib.request.urlopen(url):
		line = decl.decode('utf-8')
		if "/mc-mods/" in line:
			slug = line.split("/mc-mods/")[1].split(")")[0]
			with open(os.path.join(localpath, "changelog", slug + ".txt"), 'w') as smfile:
				smfile.write("")

		if "Discontinued" in line:
			break

	return

if __name__ == '__main__':
	main()