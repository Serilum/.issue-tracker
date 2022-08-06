/*
 * This is the latest source code of Scaffolding Drops Nearby.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.scaffoldingdropsnearby;

import com.natamus.collective.check.RegisterMod;
import com.natamus.scaffoldingdropsnearby.events.ScaffoldingEvent;
import com.natamus.scaffoldingdropsnearby.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	MinecraftForge.EVENT_BUS.register(new ScaffoldingEvent());
	}
}
