/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.2, mod version: 3.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.giantspawn.ai;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Giant;

public class GiantAttackGoal extends MeleeAttackGoal {
	   private int ticksUntilNextAttack; // attackTick
	   private final Giant giant;
	   private int raiseArmTicks;

	   public GiantAttackGoal(Giant giantIn, double speedIn, boolean longMemoryIn) {
	      super(giantIn, speedIn, longMemoryIn);
	      this.giant = giantIn;
	   }

	   /**
	    * Execute a one shot task or start executing a continuous task
	    */
	   public void start() {
	      super.start();
	      this.raiseArmTicks = 0;
	   }

	   /**
	    * Reset the task's internal state. Called when this task is interrupted by another one
	    */
	   public void stop() {
	      super.stop();
	      this.giant.setAggressive(false);
	   }

	   /**
	    * Keep ticking a continuous task that has already been started
	    */
	   public void tick() {
	      super.tick();
	      ++this.raiseArmTicks;
	      if (this.raiseArmTicks >= 5 && this.ticksUntilNextAttack < 10) {  // attackTick
	         this.giant.setAggressive(true);
	      } else {
	         this.giant.setAggressive(false);
	      }

	   }
}
