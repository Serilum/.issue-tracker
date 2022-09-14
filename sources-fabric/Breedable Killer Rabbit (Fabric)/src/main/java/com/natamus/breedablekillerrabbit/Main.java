/*
 * This is the latest source code of Breedable Killer Rabbit.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.breedablekillerrabbit;

import com.natamus.breedablekillerrabbit.config.ConfigHandler;
import com.natamus.breedablekillerrabbit.events.EntityEvent;
import com.natamus.breedablekillerrabbit.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveAnimalEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveAnimalEvents.PRE_BABY_SPAWN.register((ServerLevel world, Animal parentA, Animal parentB, AgeableMob offspring) -> {
			return EntityEvent.onBaby(world, parentA, parentB, offspring);
		});
		
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return EntityEvent.onEntityInteract(player, world, hand, entity, hitResult);
		});
		
		CollectiveEntityEvents.ON_LIVING_ATTACK.register((Level world, Entity entity, DamageSource damageSource, float damageAmount) -> {
			return EntityEvent.onTarget(world, entity, damageSource, damageAmount);
		});
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			EntityEvent.mobSpawn(world, entity);
		});
		
		CollectiveEntityEvents.ON_LIVING_DAMAGE_CALC.register((Level world, Entity entity, DamageSource damageSource, float damageAmount) -> {
			return EntityEvent.onPlayerDamage(world, entity, damageSource, damageAmount);
		});
	}
}
