/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.setworldspawnpoint.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.setworldspawnpoint.config.ConfigHandler;
import com.natamus.setworldspawnpoint.util.Reference;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Optional;

public class WorldSpawnEvent {
	private static FabricLoader fabricLoader = FabricLoader.getInstance();

	public static void onWorldLoad(ServerLevel serverworld, ServerLevelData serverLevelData) {
		if (fabricLoader.isModLoaded("village-spawn-point-fabric")) {
			return;
		}

		int x = ConfigHandler.xCoordSpawnPoint.getValue();
		int y = ConfigHandler.yCoordSpawnPoint.getValue();
		int z = ConfigHandler.zCoordSpawnPoint.getValue();
		
		if (y < 0) {
			BlockPos surfacepos = BlockPosFunctions.getSurfaceBlockPos(serverworld, x, z);
			y = surfacepos.getY();
		}
		
		BlockPos spawnpos = new BlockPos(x, y, z);
		serverworld.setDefaultSpawnPos(spawnpos, 1.0f);
	}
	
	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer player, boolean alive) {
		Level world = player.level;
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler._forceExactSpawn.getValue()) {
			ServerLevel serverworld = (ServerLevel)world;
			
			BlockPos respawnlocation = serverworld.getSharedSpawnPos(); // get spawn point
			Vec3 respawnvec = new Vec3(respawnlocation.getX(), respawnlocation.getY(), respawnlocation.getZ());
			
			BlockPos bedpos = player.getRespawnPosition();
			if (bedpos != null) {
				Optional<Vec3> optionalbed = Player.findRespawnPositionAndUseSpawnBlock(serverworld, bedpos, 1.0f, false, false);
				if (optionalbed.isPresent()) {
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
	
	public static void onEntityJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler._forceExactSpawn.getValue()) {
			return;
		}
		
		if (!(entity instanceof Player)) {
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
