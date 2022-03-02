/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.x, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Giant Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
