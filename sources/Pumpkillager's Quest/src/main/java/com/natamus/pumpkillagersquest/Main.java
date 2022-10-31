/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 2.2.
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

package com.natamus.pumpkillagersquest;

import com.natamus.collective.check.RegisterMod;
import com.natamus.pumpkillagersquest.cmds.CommandPumpkillager;
import com.natamus.pumpkillagersquest.config.ConfigHandler;
import com.natamus.pumpkillagersquest.events.*;
import com.natamus.pumpkillagersquest.rendering.ClientRenderEvent;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.Reference;
import com.natamus.pumpkillagersquest.util.SpookyHeads;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
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
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::loadComplete);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
        CommandPumpkillager.register(e.getDispatcher());
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
        Data.pumpkillagerMaxHealth = ConfigHandler.GENERAL.finalBossMaxHealth.get().floatValue();
        SpookyHeads.initPumpkinHeadData();

        MinecraftForge.EVENT_BUS.register(new PkAttackEvents());
        MinecraftForge.EVENT_BUS.register(new PkBlockEvents());
        MinecraftForge.EVENT_BUS.register(new PkEntityEvents());
        MinecraftForge.EVENT_BUS.register(new PkLivingEvents());
        MinecraftForge.EVENT_BUS.register(new PkOtherEvents());
        MinecraftForge.EVENT_BUS.register(new PkPlayerEvents());
        MinecraftForge.EVENT_BUS.register(new PkTickEvents());
        MinecraftForge.EVENT_BUS.register(new PkWorldEvents());

        if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
            MinecraftForge.EVENT_BUS.register(new ClientRenderEvent());
            MinecraftForge.EVENT_BUS.register(new PkSoundEvents());
        }
	}
}