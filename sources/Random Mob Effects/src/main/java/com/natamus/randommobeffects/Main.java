/*
 * This is the latest source code of Random Mob Effects.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randommobeffects;

import com.natamus.collective.check.RegisterMod;
import com.natamus.randommobeffects.config.ConfigHandler;
import com.natamus.randommobeffects.events.AddEffectEvent;
import com.natamus.randommobeffects.util.Reference;
import com.natamus.randommobeffects.util.Util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
        instance = this;
        
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        modEventBus.addListener(this::loadComplete);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	try {
	    	if (Util.setupPotionEffects()) {
	    		MinecraftForge.EVENT_BUS.register(new AddEffectEvent());
	    	}
		} catch (Exception ex) {
			System.out.println("!!! Something went wrong while initializing Random Mob MobEffects. The mod has been disabled.");
			return;
		}
	}
}
