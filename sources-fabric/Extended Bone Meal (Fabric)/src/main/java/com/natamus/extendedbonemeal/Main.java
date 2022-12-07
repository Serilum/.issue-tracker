/*
 * This is the latest source code of Extended Bone Meal.
 * Minecraft version: 1.19.3, mod version: 2.2.
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

package com.natamus.extendedbonemeal;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCropEvents;
import com.natamus.extendedbonemeal.events.ExtendedEvent;
import com.natamus.extendedbonemeal.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
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
		CollectiveCropEvents.ON_BONE_MEAL_APPLY.register((Player player, Level world, BlockPos pos, BlockState state, ItemStack stack) -> {
			return ExtendedEvent.onBoneMeal(player, world, pos, state, stack);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return ExtendedEvent.onNetherwartClick(player, world, hand, hitResult);
		});
	}
}
