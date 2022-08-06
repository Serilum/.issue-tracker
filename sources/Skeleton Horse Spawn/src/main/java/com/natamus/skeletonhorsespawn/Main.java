/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.skeletonhorsespawn;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.objects.SAMObject;
import com.natamus.skeletonhorsespawn.config.ConfigHandler;
import com.natamus.skeletonhorsespawn.events.SkeletonHorseEvent;
import com.natamus.skeletonhorsespawn.util.Reference;

import net.minecraft.world.entity.EntityType;
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
    	MinecraftForge.EVENT_BUS.register(new SkeletonHorseEvent());
    	new SAMObject(EntityType.SKELETON, EntityType.SKELETON_HORSE, null, ConfigHandler.GENERAL.chanceSurfaceSkeletonHasHorse.get(), false, true, ConfigHandler.GENERAL.onlySpawnSkeletonHorsesOnSurface.get());
	}
}