/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.randombonemealflowers.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RandomBoneMealFlowersUtil {
	public static List<Block> allflowers = new ArrayList<Block>();
	public static List<Block> flowers = new ArrayList<Block>();
	
	private static String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "TVE" + File.separator + "randombonemealflowers";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "blacklist.txt");
	
	public static void setFlowerList() throws IOException, FileNotFoundException, UnsupportedEncodingException {
		List<String> blacklist = new ArrayList<String>();
		
		PrintWriter writer = null;
		if (!dir.isDirectory() || !file.isFile()) {
			dir.mkdirs();
			writer = new PrintWriter(dirpath + File.separator + "blacklist.txt", "UTF-8");
		}
		else {
			String blcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "blacklist.txt", new String[0])));
			for (String flowerrl : blcontent.split("," )) {
				String name = flowerrl.replace("\n", "").trim();
				if (name.startsWith("!")) {
					blacklist.add(name.replace("!", ""));
				}
			}
		}
		
		for (Block block : ForgeRegistries.BLOCKS) {
			if (block instanceof FlowerBlock) {
				ResourceLocation rl = block.getRegistryName();
				if (rl == null) {
					continue;
				}
				
				String name = rl.toString();
				
				allflowers.add(block);
				
				if (writer != null) {
					writer.println(name + ",");
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
