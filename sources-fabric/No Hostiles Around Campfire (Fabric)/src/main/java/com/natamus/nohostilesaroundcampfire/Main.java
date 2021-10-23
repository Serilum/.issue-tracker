/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.17.x, mod version: 3.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of No Hostiles Around Campfire ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nohostilesaroundcampfire;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveSpawnEvents;
import com.natamus.nohostilesaroundcampfire.config.ConfigHandler;
import com.natamus.nohostilesaroundcampfire.events.CampfireEvent;
import com.natamus.nohostilesaroundcampfire.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	} 
	
	private void registerEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			CampfireEvent.onWorldLoad(world);
		});
		
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			CampfireEvent.onWorldTick(world);
		});
		
		CollectiveSpawnEvents.MOB_CHECK_SPAWN.register((Mob entity, ServerLevel world, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason) -> {
			return CampfireEvent.onEntityJoin(entity, world, x, y, z, spawner, spawnReason);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return CampfireEvent.onRightClickCampfireBlock(player, world, hand, hitResult);
		});
		
		CollectiveBlockEvents.BLOCK_PLACE.register((Level world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) -> {
			CampfireEvent.onCampfirePlace(world, blockPos, blockState, livingEntity, itemStack);
			return true;
		});

		CollectiveBlockEvents.BLOCK_DESTROY.register((Level world, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack) -> {
			CampfireEvent.onCampfireBreak(world, player, blockPos, blockState, blockEntity, itemStack);
			return true;
		});
	}
}
