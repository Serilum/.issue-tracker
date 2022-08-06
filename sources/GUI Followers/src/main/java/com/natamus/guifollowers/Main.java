/*
 * This is the latest source code of GUI Followers.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.guifollowers;

import com.natamus.collective.check.RegisterMod;
import com.natamus.guifollowers.config.ConfigHandler;
import com.natamus.guifollowers.events.FollowerEvent;
import com.natamus.guifollowers.events.GUIEvent;
import com.natamus.guifollowers.util.Reference;
import com.natamus.guifollowers.util.Variables;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
		if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
			return;
		}
    	
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        modEventBus.addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.register(this);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

    @SubscribeEvent
	public void registerKeyBinding(RegisterKeyMappingsEvent e) {
    	Variables.clearlist_hotkey = new KeyMapping("Clear Follower List", 92, "key.categories.misc");
    	e.register(Variables.clearlist_hotkey);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	MinecraftForge.EVENT_BUS.register(new GUIEvent(Minecraft.getInstance(), Minecraft.getInstance().getItemRenderer()));
    	MinecraftForge.EVENT_BUS.register(new FollowerEvent());
	}
}