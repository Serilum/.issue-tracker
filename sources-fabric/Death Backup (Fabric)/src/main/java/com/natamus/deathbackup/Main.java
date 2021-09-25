/*
 * This is the latest source code of Death Backup.
 * Minecraft version: 1.17.x, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Death Backup ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.deathbackup;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.deathbackup.cmds.CommandDeathBackup;
import com.natamus.deathbackup.config.ConfigHandler;
import com.natamus.deathbackup.events.DeathBackupEvent;
import com.natamus.deathbackup.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandDeathBackup.register(dispatcher);
		});
		
		ServerPlayerEvents.ALLOW_DEATH.register((ServerPlayer player, DamageSource damageSource, float damageAmount) -> {
			DeathBackupEvent.onPlayerDeath((ServerLevel)player.getCommandSenderWorld(), player);
			return true;
		});
	}
}
