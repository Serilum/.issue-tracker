/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.1, mod version: 3.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.giantspawn.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.natamus.giantspawn.ai.GiantAttackGoal;
import com.natamus.giantspawn.ai.GiantAttackTurtleEggGoal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(value = Giant.class, priority = 1001)
public class GiantMixin extends Mob {
	protected GiantMixin(EntityType<? extends Mob> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		Giant giant = (Giant)(Object)this;
		this.goalSelector.addGoal(4, new GiantAttackTurtleEggGoal(giant, 1.0D, 3));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(giant, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(giant));
		this.addBehaviourGoals(giant);
	}

	protected void addBehaviourGoals(Giant giant) {
		this.goalSelector.addGoal(2, new GiantAttackGoal(giant, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(giant, 1.0D));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(giant, new Class[0])).setAlertOthers(ZombifiedPiglin.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(giant, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<AbstractVillager>(giant, AbstractVillager.class, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<IronGolem>(giant, IronGolem.class, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<Turtle>(giant, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
	}
}
