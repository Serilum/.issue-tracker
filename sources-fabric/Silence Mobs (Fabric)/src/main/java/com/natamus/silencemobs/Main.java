/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Silence Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.silencemobs;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.silencemobs.cmds.CommandSt;
import com.natamus.silencemobs.config.ConfigHandler;
import com.natamus.silencemobs.events.EntityEvent;
import com.natamus.silencemobs.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandSt.register(dispatcher);
        });
		
		CollectiveEntityEvents.ON_LIVING_ATTACK.register((Level world, Entity entity, DamageSource damageSource, float damageAmount) -> {
			return EntityEvent.onEntityDamage(world, entity, damageSource, damageAmount);
		});
	}
}
