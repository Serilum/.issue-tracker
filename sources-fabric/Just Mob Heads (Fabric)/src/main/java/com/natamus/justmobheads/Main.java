/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.19.x, mod version: 5.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Just Mob Heads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.justmobheads;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.justmobheads.cmds.CommandJmh;
import com.natamus.justmobheads.config.ConfigHandler;
import com.natamus.justmobheads.events.HeadDropEvent;
import com.natamus.justmobheads.util.HeadData;
import com.natamus.justmobheads.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
 		HeadData.init();
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveEntityEvents.ON_ENTITY_IS_DROPPING_LOOT.register((Level world, Entity entity, DamageSource damageSource) -> {
			HeadDropEvent.mobItemDrop(world, entity, damageSource);
		});
		
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			return HeadDropEvent.onPlayerHeadBreak(world, player, pos, state, entity);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandJmh.register(dispatcher);
		});
	}
}
