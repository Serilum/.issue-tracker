/*
 * This is the latest source code of Full Brightness Toggle.
 * Minecraft version: 1.18.1, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Full Brightness Toggle ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
