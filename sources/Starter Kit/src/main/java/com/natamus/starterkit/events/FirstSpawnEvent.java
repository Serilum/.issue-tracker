/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.16.5, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Starter Kit ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.starterkit.events;

import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.starterkit.util.Reference;
import com.natamus.starterkit.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FirstSpawnEvent {
	@SubscribeEvent
	public void onSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID)) {
			Util.setStarterKit(player);
		}
	}
}
