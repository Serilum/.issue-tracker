/*
 * This is the latest source code of Full Brightness Toggle.
 * Minecraft version: 1.18.2, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.fullbrightnesstoggle;

import com.natamus.collective.check.RegisterMod;
import com.natamus.fullbrightnesstoggle.events.ToggleEvent;
import com.natamus.fullbrightnesstoggle.util.Reference;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	public static KeyMapping hotkey;
	
    public Main() {
		if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
			return;
		}
    	
        instance = this;
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::initClient);
        modEventBus.addListener(this::loadComplete);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

    private void initClient(final FMLClientSetupEvent event) {
    	hotkey = new KeyMapping("Toggle Brightness", 71, "key.categories.misc");
    	ClientRegistry.registerKeyBinding(hotkey);    	
    }
    
    private void loadComplete(final FMLLoadCompleteEvent event) {

    	MinecraftForge.EVENT_BUS.register(new ToggleEvent());
	}
}
