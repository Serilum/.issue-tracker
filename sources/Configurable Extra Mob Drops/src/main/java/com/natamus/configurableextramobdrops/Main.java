/*
 * This is the latest source code of Configurable Extra Mob Drops.
 * Minecraft version: 1.19.1, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.configurableextramobdrops;

import com.natamus.collective.check.RegisterMod;
import com.natamus.configurableextramobdrops.cmd.CommandCemd;
import com.natamus.configurableextramobdrops.events.MobDropEvent;
import com.natamus.configurableextramobdrops.util.Reference;
import com.natamus.configurableextramobdrops.util.Util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
        instance = this;
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadComplete);
        
        MinecraftForge.EVENT_BUS.register(this);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	try {
			Util.loadMobConfigFile();
		} catch (Exception ex) {
			System.out.println("[Error] Configurable Extra Mob Drops error on loading the entity config file. The mod has been disabled.");
			ex.printStackTrace();
			return;
		}
    	MinecraftForge.EVENT_BUS.register(new MobDropEvent());
	}
    
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	CommandCemd.register(e.getDispatcher());
    }
}
