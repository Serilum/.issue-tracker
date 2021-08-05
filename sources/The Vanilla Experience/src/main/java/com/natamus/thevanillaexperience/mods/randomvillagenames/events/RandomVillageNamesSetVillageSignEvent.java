/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.randomvillagenames.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.TileEntityFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.randomvillagenames.util.RandomVillageNamesUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RandomVillageNamesSetVillageSignEvent {
	private static HashMap<World, CopyOnWriteArrayList<BlockPos>> ignorevillages = new HashMap<World, CopyOnWriteArrayList<BlockPos>>();
	private static HashMap<String, Integer> lastticks = new HashMap<String, Integer>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!ignorevillages.containsKey(world)) {
			ignorevillages.put(world, new CopyOnWriteArrayList<BlockPos>());
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		String playername = player.getName().getString();
		
		int ticki = 0;
		if (lastticks.containsKey(playername)) {
			ticki = lastticks.get(playername);
		}
		
		if (ticki > 0) {
			lastticks.put(playername, ticki-1);
			return;
		}
		lastticks.put(playername, 200);
		
		ServerWorld serverworld = (ServerWorld)world;
		BlockPos ppos = player.blockPosition();
		
		BlockPos villagepos = BlockPosFunctions.getNearbyStructure(serverworld, Structure.VILLAGE, ppos, 100);
		if (villagepos == null) {
			return;
		}
		
		if (ignorevillages.get(world).contains(villagepos)) {
			return;
		}
		
		BlockPos twonorth = villagepos.immutable().north(2);
		BlockPos signpos = BlockPosFunctions.getSurfaceBlockPos(serverworld, twonorth.getX(), twonorth.getZ());
		
		BlockState state = world.getBlockState(signpos);
		Block block = state.getBlock();
		while (!RandomVillageNamesUtil.isOverwritableBlockOrSign(block)) {
			signpos = signpos.above().immutable();
			if (signpos.getY() >= 256) {
				ignorevillages.get(world).add(villagepos);
				return;
			}
			
			state = world.getBlockState(signpos);
			block = state.getBlock();
		}
		
		if (RandomVillageNamesUtil.isSign(block)) {
			ignorevillages.get(world).add(villagepos);
			return;
		}
		
		Block northblock = world.getBlockState(signpos.north()).getBlock();
		if (!RandomVillageNamesUtil.isOverwritableBlockOrSign(northblock)) {
			world.setBlockAndUpdate(signpos, Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, Direction.SOUTH));
		}
		else {
			world.setBlockAndUpdate(signpos, Blocks.OAK_SIGN.defaultBlockState());
		}
		
		TileEntity te = world.getBlockEntity(signpos);
		if (te instanceof SignTileEntity == false) {
			return;
		}
		
		SignTileEntity signentity = (SignTileEntity)te;
		signentity.setMessage(0, new StringTextComponent("[Area] 60"));
		TileEntityFunctions.updateTileEntity(serverworld, signpos, signentity);
	}
	
	@SubscribeEvent
	public void onSignBreak(BlockEvent.BreakEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		if (RandomVillageNamesUtil.isSign(state.getBlock())) {
			BlockPos signpos = e.getPos();
			for (BlockPos villagepos : ignorevillages.get(world)) {
				if (BlockPosFunctions.withinDistance(villagepos, signpos, 10)) {
					ignorevillages.get(world).remove(villagepos);
					break;
				}
			}
		}
	}
}
