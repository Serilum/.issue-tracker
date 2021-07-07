/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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

import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.util.ResourceLocation;
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
