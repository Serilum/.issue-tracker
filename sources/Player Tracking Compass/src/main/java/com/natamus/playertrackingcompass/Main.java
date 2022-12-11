/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.19.3, mod version: 2.1.
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
        modEventBus.register(new RegistryHandler());
        
        RegistryHandler.init();
        
        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
		network = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID), () -> "1.0", s -> true, s -> true);

		network.registerMessage(0, RequestServerPacket.class, RequestServerPacket::toBytes, RequestServerPacket::new, RequestServerPacket::handle);
		
		network.registerMessage(1, PacketToClientUpdateTarget.class, PacketToClientUpdateTarget::toBytes, PacketToClientUpdateTarget::new, PacketToClientUpdateTarget::handle);
    }
}