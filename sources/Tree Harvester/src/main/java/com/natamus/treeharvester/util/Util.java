/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.2, mod version: 5.9.
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

package com.natamus.treeharvester.util;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.*;
import com.natamus.treeharvester.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Util {
	private static String dirpath = DataFunctions.getConfigDirectory() + File.separator + "treeharvester";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "harvestable_axe_blacklist.txt");

	public static List<Item> allowedAxes = new ArrayList<Item>();
	public static HashMap<BlockPos, Integer> highestleaf = new HashMap<BlockPos, Integer>();
	public static CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>> lowerlogs = new CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>>();

	public static void setupAxeBlacklist() throws IOException {
		List<String> blacklist = new ArrayList<String>();

		PrintWriter writer = null;
		if (!dir.isDirectory() || !file.isFile()) {
			boolean ignored = dir.mkdirs();
			writer = new PrintWriter(dirpath + File.separator + "harvestable_axe_blacklist.txt", StandardCharsets.UTF_8);
		}
		else {
			String blcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "harvestable_axe_blacklist.txt")));
			for (String axerl : blcontent.split("," )) {
				String name = axerl.replace("\n", "").trim();
				if (name.startsWith("//")) {
					continue;
				}
				if (name.startsWith("!")) {
					blacklist.add(name.replace("!", ""));
				}
			}
		}

		if (writer != null) {
			writer.println("// To disable a certain axe from being able to harvest trees, add an exclamation mark (!) in front of the line,");
		}

		for (Item item : ForgeRegistries.ITEMS) {
			if (ToolFunctions.isAxe(new ItemStack(item))) {
				ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
				if (rl == null) {
					continue;
				}

				String name = rl.toString();

				if (writer != null) {
					writer.println(name + ",");
				}

				if (!blacklist.contains(name)) {
					allowedAxes.add(item);
				}
			}
		}

		if (writer != null) {
			writer.close();
		}
	}

	public static int isTreeAndReturnLogAmount(Level world, BlockPos pos) {
		highestleaf.put(pos, 0);
		
		int leafcount = 9;
		int logcount = 0;
		int prevleafcount = -1;
		int prevlogcount = -1;
	
		int highesty = 0;
		for (int y = 1; y<=30; y+=1) {
			if (prevleafcount == leafcount && prevlogcount == logcount) {
				break;
			}
			prevleafcount = leafcount;
			prevlogcount = logcount;
			
			Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX()-2, pos.getY()+(y-1), pos.getZ()-2, pos.getX()+2, pos.getY()+(y-1), pos.getZ()+2).iterator();
			while (it.hasNext()) {
				BlockPos npos = it.next();
				Block nblock = world.getBlockState(npos).getBlock();
				if (CompareBlockFunctions.isTreeLeaf(nblock, ConfigHandler.GENERAL.enableNetherTrees.get()) || isGiantMushroomLeafBlock(nblock)) {
					leafcount-=1;
					if (npos.getY() > highesty) {
						highesty = npos.getY();
					}
				}
				else if (CompareBlockFunctions.isTreeLog(nblock) || isGiantMushroomStemBlock(nblock)) {
					logcount+=1;
				}
			}
		}
		
		highestleaf.put(pos.immutable(), highesty);
		
		if (leafcount < 0) {
			return logcount;
		}
		return -1;
	}
	
	public static List<BlockPos> getAllLogsToBreak(Level world, BlockPos pos, int logcount, Block logtype) {
		CopyOnWriteArrayList<BlockPos> bottomlogs = new CopyOnWriteArrayList<BlockPos>();
		if (ConfigHandler.GENERAL.replaceSaplingOnTreeHarvest.get()) {
			Block blockbelow = world.getBlockState(pos.below()).getBlock();
			if (CompareBlockFunctions.isDirtBlock(blockbelow) || blockbelow instanceof MyceliumBlock) {
				Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY(), pos.getZ()+1).iterator();
				while (it.hasNext()) {
					BlockPos npos = it.next();
					Block block = world.getBlockState(npos).getBlock();
					if (block.equals(logtype)) {
						bottomlogs.add(npos.immutable());
					}
				}
			}
		}
		
		if (ConfigHandler.GENERAL.replaceSaplingOnTreeHarvest.get()) {
			if (ConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
				replaceSapling(world, pos, bottomlogs, 1, null);
			}
			else if (ConfigHandler.GENERAL.enableFastLeafDecay.get()){
				lowerlogs.add(new Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>(pos.immutable(), bottomlogs));
			}
		}
		
		return getLogsToBreak(world, pos, new ArrayList<BlockPos>(), logcount, logtype);
	}
	
	public static void replaceSapling(Level world, BlockPos pos, CopyOnWriteArrayList<BlockPos> bottomlogs, int radius, Item saplingitem) {
		int rc = bottomlogs.size();
		if (rc == 0) {
			return;
		}

		ItemStack sapling = null;

		for (Entity ea : world.getEntities(null, new AABB(pos.getX() - radius, pos.getY() - 2, pos.getZ() - radius, pos.getX() + radius, pos.getY() + 30, pos.getZ() + radius))) {
			if (ea instanceof ItemEntity) {
				ItemEntity eia = (ItemEntity) ea;
				ItemStack eisa = eia.getItem();
				Item eisaitem = eisa.getItem();
				if (saplingitem != null) {
					if (!eisaitem.equals(saplingitem)) {
						continue;
					}
				}
				if (CompareItemFunctions.isSapling(eisa) || (ConfigHandler.GENERAL.replaceMushroomOnMushroomHarvest.get() && Block.byItem(eisaitem) instanceof MushroomBlock)) {
					if (sapling == null) {
						sapling = eisa.copy();
						saplingitem = eisaitem;
					}

					int count = eisa.getCount();
					if (count > 1) {
						for (int n = 0; n < count; n++) {
							eisa.shrink(1);
							rc -= 1;

							if (rc == 0) {
								break;
							}
						}
						eia.setItem(eisa);
					} else {
						rc -= 1;
						eia.remove(RemovalReason.DISCARDED);
					}

					if (rc == 0) {
						break;
					}
				}
			}
		}
    	
		int setsaplings = bottomlogs.size()-rc;
		for (BlockPos bottompos : bottomlogs) {
			if (setsaplings == 0) {
				break;
			}

			Block belowblock = world.getBlockState(bottompos.below()).getBlock();
			if (CompareBlockFunctions.isDirtBlock(belowblock) || belowblock instanceof MyceliumBlock) {
				world.setBlockAndUpdate(bottompos, Block.byItem(sapling.getItem()).defaultBlockState());
				ForgeEventFactory.onBlockPlace(null, BlockSnapshot.create(world.dimension(), world, bottompos), Direction.UP);
			}

			setsaplings-=1;
			bottomlogs.remove(bottompos);
		}
		
		if (bottomlogs.size() > 0) {
			if (radius >= 5) {
				return;
			}
			replaceSapling(world, pos, bottomlogs, radius+2, saplingitem);
		}
	}
	
	private static List<BlockPos> getLogsToBreak(Level world, BlockPos pos, List<BlockPos> logstobreak, int logcount, Block logtype) {
		List<BlockPos> checkaround = new ArrayList<BlockPos>();
		
		Iterator<BlockPos> aroundlogs = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1).iterator();
		while (aroundlogs.hasNext()) {
			BlockPos nalogpos = aroundlogs.next().immutable();
			if (logstobreak.contains(nalogpos)) {
				continue;
			}
			BlockState logstate = world.getBlockState(nalogpos);
			Block logblock = logstate.getBlock();
			if (logblock.equals(logtype)) {
				checkaround.add(nalogpos);
				logstobreak.add(nalogpos);

				if (ConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
					Pair<Integer, Integer> hv = getHorizontalAndVerticalValue(world, pos, logtype, logcount);
					int h = hv.getFirst();
					int v = hv.getSecond();

					Iterator<BlockPos> aroundleaves = BlockPos.betweenClosedStream(pos.getX() - h, pos.getY(), pos.getZ() - h, pos.getX() + h, pos.getY() + v, pos.getZ() + h).iterator();
					while (aroundleaves.hasNext()) {
						BlockPos naleafpos = aroundleaves.next();
						Block leafblock = world.getBlockState(naleafpos).getBlock();
						if (CompareBlockFunctions.isTreeLeaf(leafblock, ConfigHandler.GENERAL.enableNetherTrees.get()) || isGiantMushroomLeafBlock(leafblock)) {
							world.destroyBlock(naleafpos, true);
						}
					}
				}
			}
		}
		
		if (checkaround.size() == 0) {
			return logstobreak;
		}
		
		for (BlockPos capos : checkaround) {
			for (BlockPos logpos : getLogsToBreak(world, capos, logstobreak, logcount, logtype)) {
				if (!logstobreak.contains(logpos)) {
					logstobreak.add(logpos.immutable());
				}
			}
		}
		
		BlockPos up = pos.above(2);
		return getLogsToBreak(world, up.immutable(), logstobreak, logcount, logtype);
	}
	
	public static Pair<Integer, Integer> getHorizontalAndVerticalValue(Level world, BlockPos startpos, Block logtype, int logcount) {
		int h = 4; // horizontal
		int v = 4; // vertical
		if (logtype.equals(Blocks.ACACIA_LOG)) {
			h = 5;
			v = 5;
		}
		else if (isGiantMushroomStemBlock(logtype)) {
			BlockPos temppos = startpos.immutable();
			while (!isGiantMushroomLeafBlock(world.getBlockState(temppos.above()).getBlock())) {
				if (temppos.getY() > world.getMaxBuildHeight()) {
					break;
				}
				temppos = temppos.above().immutable();
			}

			if (world.getBlockState(temppos.above()).getBlock().defaultMaterialColor().equals(MaterialColor.COLOR_RED)) {
				h = 2;
				v = 2;
			}
			else {
				h = 3;
				v = 3;
			}
		}
		else if (logcount >= 20) {
			h = 5;
			v = 5;
		}
		else if (logcount >= 15) {
			h = 6;
			v = 7;
		}
		else if (logcount >= 10) {
			h = 4;
			v = 5;
		}
		
		return new Pair<Integer, Integer>(h, v);
	}

	public static boolean isGiantMushroomStemBlock(Block block) {
		if (!ConfigHandler.GENERAL.enableHugeMushrooms.get()) {
			return false;
		}
		MaterialColor materialcolour = block.defaultMaterialColor();
		return block instanceof HugeMushroomBlock && materialcolour.equals(MaterialColor.WOOL);
	}

	public static boolean isGiantMushroomLeafBlock(Block block) {
		if (!ConfigHandler.GENERAL.enableHugeMushrooms.get()) {
			return false;
		}
		MaterialColor materialcolour = block.defaultMaterialColor();
		return block instanceof HugeMushroomBlock && (materialcolour.equals(MaterialColor.DIRT) || materialcolour.equals(MaterialColor.COLOR_RED));
	}

	public static Pair<Boolean, List<BlockPos>> isConnectedToLogs(Level world, BlockPos startpos) {
		List<BlockPos> recursiveList = BlockPosFunctions.getBlocksNextToEachOtherMaterial(world, startpos, Arrays.asList(Material.WOOD, Material.LEAVES), 6);
		for (BlockPos connectedpos : recursiveList) {
			Block connectedblock = world.getBlockState(connectedpos).getBlock();
			if (CompareBlockFunctions.isTreeLog(connectedblock) || isGiantMushroomStemBlock(connectedblock)) {
				return new Pair<Boolean, List<BlockPos>>(true, recursiveList);
			}
		}
		return new Pair<Boolean, List<BlockPos>>(false, recursiveList);
	}
}