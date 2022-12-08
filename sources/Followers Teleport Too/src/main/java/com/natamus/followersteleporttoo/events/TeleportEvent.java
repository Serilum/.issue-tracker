/*
 * This is the latest source code of Followers Teleport Too.
 * Minecraft version: 1.19.3, mod version: 1.1.
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

package com.natamus.followersteleporttoo.events;

import com.natamus.followersteleporttoo.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@EventBusSubscriber
public class TeleportEvent {
	private static HashMap<UUID, Date> teleportedfollowers = new HashMap<UUID, Date>();

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerTeleport(EntityTeleportEvent.TeleportCommand e) {
		if (e.isCanceled()) {
			return;
		}

		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}

		if (!(entity instanceof Player)) {
			return;
		}

		ServerPlayer player = (ServerPlayer)entity;
		BlockPos ppos = player.blockPosition();
		Vec3 targetvec = e.getTarget();

		for (Entity ea : world.getEntities(null, new AABB(ppos.getX()-50, ppos.getY()-50, ppos.getZ()-50, ppos.getX()+50, ppos.getY()+50, ppos.getZ()+50))) {
			if (ea instanceof TamableAnimal) {
				TamableAnimal ta = (TamableAnimal)ea;
				if (ta.isTame()) {
					if (!ta.isInSittingPose()) {
						if (ta.isOwnedBy(player)) {
							ta.teleportTo(targetvec.x, targetvec.y, targetvec.z);

							if (ConfigHandler.GENERAL.disableFollowerDamageAfterTeleport.get()) {
								teleportedfollowers.put(ta.getUUID(), new Date());
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onFollowerDamage(LivingHurtEvent e) {
		if (!ConfigHandler.GENERAL.disableFollowerDamageAfterTeleport.get()) {
			return;
		}

		if (teleportedfollowers.size() > 0) {
			Entity entity = e.getEntity();
			if (!(entity instanceof TamableAnimal)) {
				return;
			}

			UUID uuid = entity.getUUID();
			if (teleportedfollowers.containsKey(uuid)) {
				Date lastteleported = teleportedfollowers.get(uuid);

				long ms = ((new Date()).getTime()-lastteleported.getTime());
				if (ms > ConfigHandler.GENERAL.durationInSecondsDamageShouldBeDisabled.get()*1000) {
					teleportedfollowers.remove(uuid);
					return;
				}

				e.setAmount(0F);
				e.setCanceled(true);
			}
		}
	}
}
