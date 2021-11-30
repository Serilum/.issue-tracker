/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.18.x, mod version: 5.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Nether Portal Spread ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.netherportalspread.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.collective_fabric.functions.CompareBlockFunctions;
import com.natamus.collective_fabric.functions.DimensionFunctions;
import com.natamus.collective_fabric.functions.NumberFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;
import com.natamus.collective_fabric.objects.RandomCollection;
import com.natamus.netherportalspread.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Util {
	public static HashMap<Level, CopyOnWriteArrayList<BlockPos>> portals = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	public static HashMap<Level, HashMap<BlockPos, Boolean>> preventedportals = new HashMap<Level, HashMap<BlockPos, Boolean>>();
	
	private static HashMap<Block, HashMap<Block, Double>> convertinto = new HashMap<Block, HashMap<Block, Double>>();
	private static List<Block> convertblocks = new ArrayList<Block>();
	private static List<Block> convertedblocks = new ArrayList<Block>();
	private static Block preventSpreadBlock = null;
	
	public static void loadSpreadBlocks() throws IOException {
		String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "netherportalspread";
		File dir = new File(dirpath);
		File file = new File(dirpath + File.separator + "spreadsettings.txt");
		
		if (dir.isDirectory() && file.isFile()) {
			String spreadsettings = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "spreadsettings.txt", new String[0])));
			spreadsettings = spreadsettings.replace("\n", "").replace("\r", "").replace(" ", ""); // remove newlines, tabs and spaces
			
			for (String line : spreadsettings.split(",")) {
				if (line.length() < 4) {
					System.out.println("The Nether Portal Spread spread settings contains an empty line. Ignoring it.");
					continue;
				}
				
				String[] linespl = line.split(";");
				if (linespl.length != 2) {
					System.out.println("[Nether Portal Spread] The spread settings line '" + line + "' contains errors. Ignoring it.");
					continue;
				}
					
				String fromblockstr = linespl[0];
				if (!fromblockstr.contains(":")) {
					fromblockstr = "minecraft:" + fromblockstr;
				}
				ResourceLocation frl = new ResourceLocation(fromblockstr);
				if (!Registry.BLOCK.keySet().contains(frl)) {
					System.out.println("[Nether Portal Spread] Unable to find from-block '" + fromblockstr + "' in the Forge block registry. Ignoring it.");
					continue;
				}
				Block fromblock = Registry.BLOCK.get(frl);
				
				String toblocks = linespl[1].replace("[", "").replace("]", "");
				
				double totalweight = 0;
				HashMap<Block, Double> tempmap = new HashMap<Block, Double>();
				for (String tb : toblocks.split("\\+")) {
					String[] tbspl = tb.split(">");
					if (tbspl.length < 2) {
						continue;
					}
					
					String toblockstr = tbspl[0];
					if (!toblockstr.contains(":")) {
						toblockstr = "minecraft:" + toblockstr;
					}
					
					Double weight = 1.0;
					try {
						weight = Double.parseDouble(tbspl[1]);
					}
					catch (NumberFormatException ex) { }
					totalweight += weight;
					
					ResourceLocation trl = new ResourceLocation(toblockstr);
					if (Registry.BLOCK.keySet().contains(trl)) {
						tempmap.put(Registry.BLOCK.get(trl), weight);
					}
					else {
						System.out.println("[Nether Portal Spread] Unable to find to-block '" + toblockstr + "' in the Forge block registry. Ignoring it.");
					}
				}
				
				if (tempmap.size() == 0 || totalweight == 0) {
					System.out.println("[Nether Portal Spread] The spread settings line '" + line + "' contains errors, no convert blocks were found. Ignoring it.");
				}
				else {
					for (Block key : tempmap.keySet()) {
						Double weightvalue = tempmap.get(key);
						tempmap.put(key, (1/totalweight)*weightvalue);
					}
					convertinto.put(fromblock, tempmap);
				}
			}

			for (Block b0 : convertinto.keySet()) {
				convertblocks.add(b0);
				for (Block b1 : convertinto.get(b0).keySet()) {
					convertedblocks.add(b1);
				}
			}
		}
		else {
			dir.mkdirs();
			
			List<String> defaultconfig = DefaultConfigs.getDefaultConfigForVersion(Reference.ACCEPTED_VERSIONS);
			
			PrintWriter writer = new PrintWriter(dirpath + File.separator + "spreadsettings.txt", "UTF-8");
			for (String line : defaultconfig) {
				writer.println(line);
			}
			writer.close();
			
			loadSpreadBlocks();
		}
		
		if (preventSpreadBlock == null) {
			String psbstr = ConfigHandler.preventSpreadBlockString.getValue();
			ResourceLocation psbrl = new ResourceLocation(psbstr);
			if (Registry.BLOCK.keySet().contains(psbrl)) {
				preventSpreadBlock = Registry.BLOCK.get(psbrl);
			}
			else {
				System.out.println("[Nether Portal Spread] Unable to get a prevent-spread-block from the string '" + psbstr + "'. Using the default coal block instead.");
				preventSpreadBlock = Blocks.COAL_BLOCK;
			}
		}
	}
	
	public static void loadPortalsFromWorld(Level world) {
		String portalfolder = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "data" + File.separator + "netherportalspread" + File.separator + "portals";
		String specificportalfolder = portalfolder + File.separator + DimensionFunctions.getSimpleDimensionString(world);
		
		if (specificportalfolder.endsWith("overworld")) {
			final File sourceDir = new File(portalfolder);
			final File destinationDir = new File(specificportalfolder);
			destinationDir.mkdirs();
			
			File[] files = sourceDir.listFiles((File pathname) -> pathname.getName().endsWith(".portal"));
			
			for (File f : files ) {
				Path sourcePath = Paths.get(sourceDir.getAbsolutePath() + File.separator + f.getName());
				Path destinationPath = Paths.get(destinationDir.getAbsolutePath() + File.separator + f.getName());
			
				try {
					Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					continue;
				}
			}
		}
		
		File dir = new File(specificportalfolder);
		dir.mkdirs();
		
		File[] listOfFiles = dir.listFiles();
		if (listOfFiles == null) {
			return;
		}
		
		int r = ConfigHandler.portalSpreadRadius.getValue();
		Integer psamount = ConfigHandler.preventSpreadBlockAmountNeeded.getValue();
		for (int i = 0; i < listOfFiles.length; i++) {
			String filename = listOfFiles[i].getName();
			if (filename.endsWith(".portal")) {
				String[] cs = filename.replace(".portal", "").split("_");
				if (cs.length == 3) {
					if (NumberFunctions.isNumeric(cs[0]) && NumberFunctions.isNumeric(cs[1]) && NumberFunctions.isNumeric(cs[2])) {
						int x = Integer.parseInt(cs[0]);
						int y = Integer.parseInt(cs[1]);
						int z = Integer.parseInt(cs[2]);
						
						BlockPos portal = new BlockPos(x, y, z);
						portals.get(world).add(portal);
						
						if (ConfigHandler.preventSpreadWithBlock.getValue()) {
							int coalcount = 0;
							Iterator<BlockPos> it = BlockPos.betweenClosedStream(portal.getX()-r, portal.getY()-r, portal.getZ()-r, portal.getX()+r, portal.getY()+r, portal.getZ()+r).iterator();
							while (it.hasNext()) {
								try {
									BlockPos np = it.next();
									if (world.getBlockState(np).getBlock().equals(preventSpreadBlock)) {
										coalcount++;
										if (coalcount >= psamount) {
											break;
										}
									}
								}
								catch (NullPointerException ex) {
									continue;
								}
							}
							
							
							if (coalcount >= psamount) {
								preventedportals.get(world).put(portal, true);
							}
							else {
								preventedportals.get(world).put(portal, false);
							}
						}
					}
				}
			}
		}
	}
	
	public static void savePortalToWorld(Level world, BlockPos portal) {
		String portalfolder = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "data" + File.separator + "netherportalspread" + File.separator + "portals" + File.separator + DimensionFunctions.getSimpleDimensionString(world);
		File dir = new File(portalfolder);
		dir.mkdirs();
		
		String filename = portal.getX() + "_" + portal.getY() + "_" + portal.getZ() + ".portal";
		try {
			PrintWriter writer = new PrintWriter(portalfolder + File.separator + filename, "UTF-8");
			writer.close();
		} catch (Exception e) {
			System.out.println("[Error] Nether Portal Spread: Something went wrong while saving a portal location.");
		}
	}
	
	private static Boolean portalExists(Level world, BlockPos pos) {
		for (BlockPos portalpos : portals.get(world)) {
			Double distance = pos.distSqr(portalpos.getX(), portalpos.getY(), portalpos.getZ(), true);
			if (distance < 10) {
				return true;
			}
		}
		return false;
	}
	
	public static void validatePortalAndAdd(Level world, BlockPos p) {
		BlockPos rawportal = null;
		
		int r = 3;
		Iterator<BlockPos> it = BlockPos.betweenClosedStream(p.getX()-r, p.getY()-r, p.getZ()-r, p.getX()+r, p.getY()+r, p.getZ()+r).iterator();
		while (it.hasNext()) {		
			BlockPos nextpos = it.next();
			Block block = world.getBlockState(nextpos).getBlock();
			if (CompareBlockFunctions.isPortalBlock(block)) {
				rawportal = nextpos.immutable();
				break;
			}
		}
		
		if (rawportal == null) {
			return;
		}
		
		while (CompareBlockFunctions.isPortalBlock(world.getBlockState(rawportal.below()).getBlock())) {
			rawportal = rawportal.below().immutable();
		}
		while (CompareBlockFunctions.isPortalBlock(world.getBlockState(rawportal.west()).getBlock())) {
			rawportal = rawportal.west().immutable();
		}
		while (CompareBlockFunctions.isPortalBlock(world.getBlockState(rawportal.north()).getBlock())) {
			rawportal = rawportal.north().immutable();
		}
		
		if (portals.get(world).contains(rawportal) || preventedportals.get(world).containsKey(rawportal)) {
			return;
		}
		
		if (portalExists(world, p)) {
			return;
		}
		
		sendSpreadingMessage(world, p);
		preventedportals.get(world).put(p, false);
		portals.get(world).add(rawportal);
		savePortalToWorld(world, rawportal);
		
		int netherblockcount = countNetherBlocks(world, p);
		if (netherblockcount < ConfigHandler.instantConvertAmount.getValue()) {
			while (netherblockcount < ConfigHandler.instantConvertAmount.getValue()) {
				if (!spreadNextBlock(world, p)) {
					break;
				}
				
				netherblockcount+=1;
			}
		}
	}
	
	public static void removePortal(Level world, BlockPos portal) {
		String portalfolder = WorldFunctions.getWorldPath((ServerLevel)world) + File.separator + "data" + File.separator + "netherportalspread" + File.separator + "portals";
		String filename = portal.getX() + "_" + portal.getY() + "_" + portal.getZ() + ".portal";
		File filepath = new File(portalfolder + File.separator + filename);
		try {
			Files.deleteIfExists(filepath.toPath());
		} catch (Exception e) {
			System.out.println("[Error] Nether Portal Spread: Something went wrong while removing an old portal location.");
		}
		
		portals.get(world).remove(portal);
		preventedportals.get(world).remove(portal);
		sendBrokenPortalMessage(world, portal);
	}
	
	public static Boolean spreadNextBlock(Level world, BlockPos portal) {
		if (!world.hasChunk(portal.getX() >> 4, portal.getZ() >> 4)) {
			return true;
		}
		
		BlockState pbs = world.getBlockState(portal);
		if (pbs == null) {
			return false;
		}
		
		if (!CompareBlockFunctions.isPortalBlock(pbs.getBlock())) {
			removePortal(world, portal);
			return false;
		}
		
		int r = ConfigHandler.portalSpreadRadius.getValue();
		
		BlockPos closest = null;
		double nearestdistance = 100000;
		int coalcount = 0;
		
		Integer psamount = ConfigHandler.preventSpreadBlockAmountNeeded.getValue();
		Iterator<BlockPos> it = BlockPos.betweenClosedStream(portal.getX()-r, portal.getY()-r, portal.getZ()-r, portal.getX()+r, portal.getY()+r, portal.getZ()+r).iterator();
		while (it.hasNext()) {
			try {
				BlockPos np = it.next();
				if (ConfigHandler.preventSpreadWithBlock.getValue()) {
					if (world.getBlockState(np).getBlock().equals(preventSpreadBlock)) {
						coalcount++;
						if (coalcount >= psamount) {
							break;
						}
					}
				}
				double npnd = portal.distSqr(np.getX(), np.getY(), np.getZ(), true);
				if (npnd < nearestdistance) {
					if (isNetherTarget(world, np, false)) {
						nearestdistance = npnd;
						closest = np.immutable();
					}
				}
			}
			catch (NullPointerException ex) {
				continue;
			}
		}
		
		if (ConfigHandler.preventSpreadWithBlock.getValue()) {
			if (coalcount >= psamount) {
				boolean prevented = false;
				if (preventedportals.get(world).containsKey(portal)) {
					prevented = preventedportals.get(world).get(portal);
				}
				
				if (!prevented) {
					sendPreventedMessage(world, portal);		
				}
				
				preventedportals.get(world).put(portal, true);
				return true;
			}
		}
		
		if (closest != null) {
			boolean prevented = false;
			if (preventedportals.get(world).containsKey(portal)) {
				prevented = preventedportals.get(world).get(portal);
			}
			
			if (prevented) {
				sendSpreadingMessage(world, portal);
			}
			
			preventedportals.get(world).put(portal, false);
			spreadNetherToBlock(world, closest);
			return true;
		}
		return false;
	}

	public static int countNetherBlocks(Level world, BlockPos p) {
		int nethercount = 0;
		int r = ConfigHandler.portalSpreadRadius.getValue();
		
		Iterator<BlockPos> it = BlockPos.betweenClosedStream(p.getX()-r, p.getY()-r, p.getZ()-r, p.getX()+r, p.getY()+r, p.getZ()+r).iterator();
		while (it.hasNext()) {
			BlockPos np = it.next();
			if (isNetherTarget(world, np, true)) {
				nethercount+=1;
			}
		}
		return nethercount;
	}
	
	public static Boolean isNetherTarget(Level world, BlockPos pos, Boolean count) {
		if (world == null) {
			return false;
		}
		
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (count) {
			if (convertedblocks.contains(block)) {
				return true;
			}
			return false;
		}
		if (convertblocks.contains(block)) {
			return true;
		}
		
		return false;
	}
	
	public static void spreadNetherToBlock(Level world, BlockPos pos) {
		if (world == null) {
			return;
		}
		
		BlockState newblockstate = null;
		
		BlockState curstate = world.getBlockState(pos);
		Block curblock = curstate.getBlock();
		
		if (convertblocks.contains(curblock)) {
			// key: from-block, value: HashMap of to-block and weight. so if two had weight 10 and 5, (1/15)*10 == 66.65% and 33.35% chance.
			RandomCollection<Block> rc = new RandomCollection<>(); //.add(40, "a").add(35, "b").add(25, "c");
			HashMap<Block, Double> hashmap = convertinto.get(curblock);
			for (Block b0 : hashmap.keySet()) {
				Double weight = hashmap.get(b0);
				rc.add(weight*100, b0);
			}
			
			newblockstate = rc.next().defaultBlockState();
		}
		
		if (newblockstate != null) {
			world.setBlockAndUpdate(pos, newblockstate);
		}
	}
	
	private static void sendSpreadingMessage(Level world, BlockPos p) {
		if (!ConfigHandler.sendMessageOnPortalCreation.getValue()) {
			return;
		}
		
		String message = ConfigHandler.messageOnPortalCreation.getValue();
		StringFunctions.sendMessageToPlayersAround(world, p, ConfigHandler.portalSpreadRadius.getValue(), formatAroundString(message, ConfigHandler.preventSpreadBlockAmountNeeded.getValue(), p), ChatFormatting.RED);
	}
	
	private static void sendPreventedMessage(Level world, BlockPos p) {
		if (!ConfigHandler.sendMessageOnPreventSpreadBlocksFound.getValue()) {
			return;
		}
		
		String message = ConfigHandler.messageOnPreventSpreadBlocksFound.getValue();
		StringFunctions.sendMessageToPlayersAround(world, p, ConfigHandler.portalSpreadRadius.getValue(), formatAroundString(message, ConfigHandler.preventSpreadBlockAmountNeeded.getValue(), p), ChatFormatting.DARK_GREEN);	
	}
	
	private static void sendBrokenPortalMessage(Level world, BlockPos p) {
		if (!ConfigHandler.sendMessageOnPortalBroken.getValue()) {
			return;
		}
		
		String message = ConfigHandler.messageOnPortalBroken.getValue();
		StringFunctions.sendMessageToPlayersAround(world, p, ConfigHandler.portalSpreadRadius.getValue(), formatAroundString(message, ConfigHandler.preventSpreadBlockAmountNeeded.getValue(), p), ChatFormatting.DARK_GREEN);	
	}
	
	private static String formatAroundString(String message, int amountneeded, BlockPos portal) {
		if (preventSpreadBlock == null) {
			preventSpreadBlock = Blocks.COAL_BLOCK;
		}
		
		String blockstring = BlockFunctions.blockToReadableString(preventSpreadBlock, amountneeded);

		message = message.replace("%preventSpreadBlockString%", blockstring);
		message = message.replace("%preventSpreadBlockAmountNeeded%", amountneeded + "");
		message = message.replace("%portalSpreadRadius%", ConfigHandler.portalSpreadRadius.getValue() + "");
		if (ConfigHandler.prefixPortalCoordsInMessage.getValue()) {
			message = "Portal {" + portal.getX() + ", " + portal.getY() + ", " + portal.getZ() + "}: " + message;
		}
		return message;
	}
}
