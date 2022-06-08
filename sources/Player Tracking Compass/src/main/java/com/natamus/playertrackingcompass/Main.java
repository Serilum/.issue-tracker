/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.19.0, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Player Tracking Compass ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.playertrackingcompass;

import com.natamus.collective.check.RegisterMod;
import com.natamus.playertrackingcompass.network.PacketToClientUpdateTarget;
import com.natamus.playertrackingcompass.network.RequestServerPacket;
import com.natamus.playertrackingcompass.util.Reference;
import com.natamus.playertrackingcompass.util.RegistryHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(Reference.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {
	public static Main instance;
	public static SimpleChannel network;
	
    public Main() {
        instance = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        modEventBus.addListener(this::commonSetup);
        
        RegistryHandler.init();
        
        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
		network = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID), () -> "1.0", s -> true, s -> true);

		network.registerMessage(0, RequestServerPacket.class, RequestServerPacket::toBytes, RequestServerPacket::new, RequestServerPacket::handle);
		
		network.registerMessage(1, PacketToClientUpdateTarget.class, PacketToClientUpdateTarget::toBytes, PacketToClientUpdateTarget::new, PacketToClientUpdateTarget::handle);
    }
}