/*
 * This is the latest source code of Random Bone Meal Flowers.
 * Minecraft version: 1.19.2, mod version: 3.0.
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

package com.natamus.randombonemealflowers;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCropEvents;
import com.natamus.randombonemealflowers.events.FlowerEvent;
import com.natamus.randombonemealflowers.util.Reference;
import com.natamus.randombonemealflowers.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	private boolean shouldrun = true;

	@Override
	public void onInitialize() {
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			try {
				Util.setFlowerList();
			} catch (Exception ex) {
				System.out.println("[Random Bone Meal Flower] Error: Unable to generate flower list.");
				shouldrun = false;
			}
		});
		
		CollectiveCropEvents.ON_GENERAL_BONE_MEAL_APPLY.register((Level world, BlockPos pos, BlockState state, ItemStack stack) -> {
			if (shouldrun) {
				FlowerEvent.onBonemeal(world, pos, state, stack);
			}
			return true;
		});
	}
}
