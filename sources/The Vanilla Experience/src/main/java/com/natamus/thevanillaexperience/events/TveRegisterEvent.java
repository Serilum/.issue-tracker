/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.events;

import com.natamus.thevanillaexperience.config.ConfigHandler;
import com.natamus.thevanillaexperience.mods.areas.cmds.CommandAreas;
import com.natamus.thevanillaexperience.mods.deathbackup.cmds.CommandDeathBackup;
import com.natamus.thevanillaexperience.mods.enchantingcommands.cmds.CommandEc;
import com.natamus.thevanillaexperience.mods.justmobheads.cmds.CommandJmh;
import com.natamus.thevanillaexperience.mods.nametagtweaks.cmds.NametagCommand;
import com.natamus.thevanillaexperience.mods.nametagtweaks.config.NameTagTweaksConfigHandler;
import com.natamus.thevanillaexperience.mods.omegamute.cmds.CommandOmega;
import com.natamus.thevanillaexperience.mods.recipecommands.cmds.CommandRecipes;
import com.natamus.thevanillaexperience.mods.recipecommands.util.Recipes;
import com.natamus.thevanillaexperience.mods.starterkit.cmds.CommandStarterkit;
import com.natamus.thevanillaexperience.util.TveUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;

@EventBusSubscriber
public class TveRegisterEvent {
	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent e) {
		if ((ConfigHandler.GENERAL.enableAreas.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Areas")) {
			CommandAreas.register(e.getDispatcher());
		}
		if ((ConfigHandler.GENERAL.enableDeathBackup.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Death Backup")) {
			CommandDeathBackup.register(e.getDispatcher());
		}
		if ((ConfigHandler.GENERAL.enableEnchantingCommands.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Enchanting Commands")) {
			CommandEc.register(e.getDispatcher());
		}
		if ((ConfigHandler.GENERAL.enableJustMobHeads.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Just Mob Heads")) {
			CommandJmh.register(e.getDispatcher());
		}
		if ((ConfigHandler.GENERAL.enableNameTagTweaks.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Name Tag Tweaks")) {
			if (NameTagTweaksConfigHandler.GENERAL.enableNameTagCommand.get()) {
			NametagCommand.register(e.getDispatcher());
		}
		}
		if ((ConfigHandler.GENERAL.enableOmegaMute.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Omega Mute") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			CommandOmega.register(e.getDispatcher());
		}
		if ((ConfigHandler.GENERAL.enableRecipeCommands.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Recipe Commands")) {
			Recipes.InitRecipes();
		CommandRecipes.register(e.getDispatcher());
		}
		if ((ConfigHandler.GENERAL.enableStarterKit.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Starter Kit")) {
			CommandStarterkit.register(e.getDispatcher());
		}
	}
}
