/*
 * This is the latest source code of Random Bone Meal Flowers.
 * Minecraft version: 1.19.3, mod version: 3.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.randombonemealflowers.util;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.DataFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {
	public static List<Block> allflowers = new ArrayList<Block>();
	public static List<Block> flowers = new ArrayList<Block>();
	
	private static String dirpath = DataFunctions.getConfigDirectory() + File.separator + "randombonemealflowers";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "blacklist.txt");
	
	public static void setFlowerList() throws IOException {
		List<String> blacklist = new ArrayList<String>();
		allflowers = new ArrayList<Block>();
		flowers = new ArrayList<Block>();
		
		PrintWriter writer = null;
		if (!dir.isDirectory() || !file.isFile()) {
			if (dir.mkdirs()) {
				writer = new PrintWriter(dirpath + File.separator + "blacklist.txt", StandardCharsets.UTF_8);
			}
		}
		else {
			String blcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "blacklist.txt")));
			for (String flowerrl : blcontent.split("," )) {
				String name = flowerrl.replace("\n", "").trim();
				if (name.startsWith("!")) {
					blacklist.add(name.replace("!", ""));
				}
			}
		}
		
		for (Block block : ForgeRegistries.BLOCKS) {
			if (block instanceof FlowerBlock) {
				ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);
				if (rl == null) {
					continue;
				}
				
				String name = rl.toString();
				
				allflowers.add(block);

				if (writer != null) {
					String prefix = "";
					if (name.equals("minecraft:wither_rose")) {
						blacklist.add(name);
						prefix = "!";
					}

					writer.println(prefix + name + ",");
				}
				
				if (!blacklist.contains(name)) {
					flowers.add(block);
				}
			}
		}
		
		if (writer != null) {
			writer.close();
		}
	}
	
	public static Block getRandomFlower() {
		int x = GlobalVariables.random.nextInt(flowers.size());

		return flowers.get(x);
	}
}
