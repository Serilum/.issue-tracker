/*
 * This is the latest source code of Starter Structure.
 * Minecraft version: 1.19.3, mod version: 1.0.
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

package com.natamus.starterstructure.events;

import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.starterstructure.config.ConfigHandler;
import com.natamus.starterstructure.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Optional;

@EventBusSubscriber
public class StructureSpawnPointEvents {
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
		Player player = e.getEntity();
		Level world = player.level;
		if (world.isClientSide) {
			return;
		}

		if (ConfigHandler.GENERAL.forceExactSpawnMiddleStructure.get()) {
			ServerPlayer serverplayer = (ServerPlayer)player;
			ServerLevel serverworld = (ServerLevel)world;

			BlockPos respawnlocation = serverworld.getSharedSpawnPos();
			Vec3 respawnvec = new Vec3(respawnlocation.getX()+0.5, respawnlocation.getY(), respawnlocation.getZ()+0.5);

			BlockPos bedpos = serverplayer.getRespawnPosition();
			if (bedpos != null) {
				Optional<Vec3> optionalbed = Player.findRespawnPositionAndUseSpawnBlock(serverworld, bedpos, 1.0f, false, false);
				if (optionalbed.isPresent()) {
					return;
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

		if (!ConfigHandler.GENERAL.forceExactSpawnMiddleStructure.get()) {
			return;
		}

		Entity entity = e.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player)entity;
		if (!PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID, false)) {
			return;
		}

		ServerLevel serverworld = (ServerLevel)world;

		BlockPos ppos = player.blockPosition();
		BlockPos wspos = serverworld.getSharedSpawnPos();
		if (new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ()).closerThan(wspos, 50)) {
			player.teleportTo(wspos.getX()+0.5, wspos.getY(), wspos.getZ()+0.5);
		}
	}
}
