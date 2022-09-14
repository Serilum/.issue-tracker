/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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
