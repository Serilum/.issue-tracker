/*
 * This is the latest source code of Better Beacon Placement.
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

package com.natamus.betterbeaconplacement;

import com.natamus.betterbeaconplacement.config.ConfigHandler;
import com.natamus.betterbeaconplacement.events.BeaconEvent;
import com.natamus.betterbeaconplacement.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveBlockEvents.BLOCK_RIGHT_CLICK.register((Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) -> {
			return BeaconEvent.onBeaconClick(world, player, hand, pos, hitVec);
		});
		
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			BeaconEvent.onBlockBreak(world, player, pos, state, entity);
		});
	}
}
