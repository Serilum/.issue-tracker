/*
 * This is the latest source code of Random Bone Meal Flowers.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.randombonemealflowers;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCropEvents;
import com.natamus.randombonemealflowers.events.FlowerEvent;
import com.natamus.randombonemealflowers.util.Reference;
import com.natamus.randombonemealflowers.util.Util;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		try {
			Util.setFlowerList();
		} catch (Exception ex) {
			return;
		}
		
		CollectiveCropEvents.ON_GENERAL_BONE_MEAL_APPLY.register((Level world, BlockPos pos, BlockState state, ItemStack stack) -> {
			FlowerEvent.onBonemeal(world, pos, state, stack);
			return true;
		});
	}
}
