/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.19.1, mod version: 2.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Set World Spawn Point ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.setworldspawnpoint.events;

import java.util.Iterator;
import java.util.Optional;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.setworldspawnpoint.config.ConfigHandler;
import com.natamus.setworldspawnpoint.util.Reference;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WorldSpawnEvent {
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		int x = ConfigHandler.GENERAL.xCoordSpawnPoint.get();
		int y = ConfigHandler.GENERAL.yCoordSpawnPoint.get();
		int z = ConfigHandler.GENERAL.zCoordSpawnPoint.get();
		
		if (y < 0) {
			BlockPos surfacepos = BlockPosFunctions.getSurfaceBlockPos(serverworld, x, z);
			y = surfacepos.getY();
		}
		
		BlockPos spawnpos = new BlockPos(x, y, z);
		
		e.setCanceled(true);
		serverworld.setDefaultSpawnPos(spawnpos, 1.0f);
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player player = e.getEntity();
		Level world = player.level;
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler.GENERAL._forceExactSpawn.get()) {
			ServerPlayer serverplayer = (ServerPlayer)player;
			ServerLevel serverworld = (ServerLevel)world;
			
			BlockPos respawnlocation = serverworld.getSharedSpawnPos(); // get spawn point
			Vec3 respawnvec = new Vec3(respawnlocation.getX(), respawnlocation.getY(), respawnlocation.getZ());
			
			BlockPos bedpos = serverplayer.getRespawnPosition();
			if (bedpos != null) {
				Optional<Vec3> optionalbed = Player.findRespawnPositionAndUseSpawnBlock(serverworld, bedpos, 1.0f, false, false);
				if (optionalbed != null) {
					if (optionalbed.isPresent()) {
						Vec3 bedlocation = optionalbed.get();
						BlockPos bl = new BlockPos(bedlocation.x(), bedlocation.y(), bedlocation.z());
			
						Iterator<BlockPos> it = BlockPos.betweenClosedStream(bl.getX()-1, bl.getY()-1, bl.getZ()-1, bl.getX()+1, bl.getY()+1, bl.getZ()+1).iterator();
						while (it.hasNext()) {
							BlockPos np = it.next();
							BlockState state = world.getBlockState(np);
							Block block = state.getBlock();
							if (block instanceof BedBlock) {
								if (state.getValue(BedBlock.PART).equals(BedPart.FOOT)) {
									bedlocation = new Vec3(np.getX()+0.5, np.getY(), np.getZ()+0.5);
									break;
								}
							}
						}
						
						respawnvec = new Vec3(bedlocation.x(), bedlocation.y(), bedlocation.z());
					}
				}
			}
			
			player.teleportTo(respawnvec.x, respawnvec.y, respawnvec.z);
		}
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.GENERAL._forceExactSpawn.get()) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (!PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID)) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		
		BlockPos wspos = serverworld.getSharedSpawnPos();
		BlockPos ppos = player.blockPosition();
		BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());
		
		if (cpos.closerThan(wspos, 50)) {
			player.teleportTo(wspos.getX(), wspos.getY(), wspos.getZ());
		}
	}
}
