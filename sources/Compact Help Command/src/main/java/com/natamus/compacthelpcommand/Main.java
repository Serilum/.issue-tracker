/*
 * This is the latest source code of Compact Help Command.
 * Minecraft version: 1.19.2, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.compacthelpcommand;

import com.natamus.collective.check.RegisterMod;
import com.natamus.compacthelpcommand.cmds.CommandHelp;
import com.natamus.compacthelpcommand.config.ConfigHandler;
import com.natamus.compacthelpcommand.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
        instance = this;
        
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
    	
        MinecraftForge.EVENT_BUS.register(this);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	CommandHelp.register(e.getDispatcher());
    }
}