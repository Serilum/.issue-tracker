/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.1.
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

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveAnimalEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

@Mixin(value = Animal.class, priority = 1001)
public abstract class AnimalMixin extends AgeableMob {
	@Shadow private int inLove;
	
	protected AnimalMixin(EntityType<? extends AgeableMob> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyVariable(method = "spawnChildFromBreeding", at = @At(value= "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/animal/Animal;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;", ordinal = 0), ordinal = 0)
	private AgeableMob Animal_spawnChildFromBreeding(AgeableMob ageablemob, ServerLevel serverLevel, Animal animal) {
		Animal parentA = (Animal)(Object)this;
		boolean shouldBreed = CollectiveAnimalEvents.PRE_BABY_SPAWN.invoker().onBabySpawn(serverLevel, parentA, animal, ageablemob);
		
		if (!shouldBreed) {
			ageablemob = null;	
			
			this.setAge(6000);
			animal.setAge(6000);
			this.inLove = 0;
			animal.setInLoveTime(0);
		}

		return ageablemob;
	}
}
