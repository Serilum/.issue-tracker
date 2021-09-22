/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.17.x, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Grindstone Sharper Tools ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.grindstonesharpertools;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.grindstonesharpertools.config.ConfigHandler;
import com.natamus.grindstonesharpertools.events.GrindEvent;
import com.natamus.grindstonesharpertools.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
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
		CollectiveEntityEvents.ON_LIVING_DAMAGE_CALC.register((Level world, Entity entity, DamageSource damageSource, float damageAmount) -> {
			return GrindEvent.onDamage(world, entity, damageSource, damageAmount);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return GrindEvent.onClick(player, world, hand, hitResult);
		});
	}
}
