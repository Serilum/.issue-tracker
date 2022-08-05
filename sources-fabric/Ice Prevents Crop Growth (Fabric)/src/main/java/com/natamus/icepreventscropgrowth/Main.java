/*
 * This is the latest source code of Ice Prevents Crop Growth.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.icepreventscropgrowth;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCropEvents;
import com.natamus.icepreventscropgrowth.events.CropEvent;
import com.natamus.icepreventscropgrowth.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveCropEvents.PRE_CROP_GROW.register((Level world, BlockPos pos, BlockState state) -> {
			return CropEvent.onCropGrowth(world, pos, state);
		});
	}
}
