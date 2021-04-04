/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.16.5, mod version: 1.6.
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

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WorldSpawnEvent {
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.CreateSpawnPosition e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		ServerWorld serverworld = (ServerWorld)world;
		int x = ConfigHandler.GENERAL.xCoordSpawnPoint.get();
		int y = ConfigHandler.GENERAL.yCoordSpawnPoint.get();
		int z = ConfigHandler.GENERAL.zCoordSpawnPoint.get();
		
		if (y < 0) {
			BlockPos surfacepos = BlockPosFunctions.getSurfaceBlockPos(serverworld, x, z);
			y = surfacepos.getY();
		}
		
		BlockPos spawnpos = new BlockPos(x, y, z);
		
		e.setCanceled(true);
		serverworld.func_241124_a__(spawnpos, 1.0f);
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.world;
		if (world.isRemote) {
			return;
		}
		
		if (ConfigHandler.GENERAL._forceExactSpawn.get()) {
			ServerPlayerEntity serverplayer = (ServerPlayerEntity)player;
			ServerWorld serverworld = (ServerWorld)world;
			
			BlockPos respawnlocation = serverworld.getSpawnPoint(); // get spawn point
			Vector3d respawnvec = new Vector3d(respawnlocation.getX(), respawnlocation.getY(), respawnlocation.getZ());
			
			BlockPos bedpos = serverplayer.func_241140_K_();
			if (bedpos != null) {
				Optional<Vector3d> optionalbed = PlayerEntity.func_242374_a(serverworld, bedpos, 1.0f, false, false);
				if (optionalbed != null) {
					if (optionalbed.isPresent()) {
						Vector3d bedlocation = optionalbed.get();
						BlockPos bl = new BlockPos(bedlocation.getX(), bedlocation.getY(), bedlocation.getZ());
			
						Iterator<BlockPos> it = BlockPos.getAllInBox(bl.getX()-1, bl.getY()-1, bl.getZ()-1, bl.getX()+1, bl.getY()+1, bl.getZ()+1).iterator();
						while (it.hasNext()) {
							BlockPos np = it.next();
							BlockState state = world.getBlockState(np);
							Block block = state.getBlock();
							if (block instanceof BedBlock) {
								if (state.get(BedBlock.PART).equals(BedPart.FOOT)) {
									bedlocation = new Vector3d(np.getX()+0.5, np.getY(), np.getZ()+0.5);
									break;
								}
							}
						}
						
						respawnvec = new Vector3d(bedlocation.getX(), bedlocation.getY(), bedlocation.getZ());
					}
				}
			}
			
			player.setPositionAndUpdate(respawnvec.x, respawnvec.y, respawnvec.z);
		}
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		if (!ConfigHandler.GENERAL._forceExactSpawn.get()) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (!PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID)) {
			return;
		}
		
		ServerWorld serverworld = (ServerWorld)world;
		
		BlockPos wspos = serverworld.getSpawnPoint();
		BlockPos ppos = player.getPosition();
		BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());
		
		if (cpos.withinDistance(wspos, 50)) {
			player.setPositionAndUpdate(wspos.getX(), wspos.getY(), wspos.getZ());
		}
	}
}
