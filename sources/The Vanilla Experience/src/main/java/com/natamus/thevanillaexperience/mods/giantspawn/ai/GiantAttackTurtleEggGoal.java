/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.giantspawn.ai;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public class GiantAttackTurtleEggGoal extends RemoveBlockGoal {
	public GiantAttackTurtleEggGoal(PathfinderMob p_i50465_2_, double p_i50465_3_, int p_i50465_5_) {
		super(Blocks.TURTLE_EGG, p_i50465_2_, p_i50465_3_, p_i50465_5_);
	}
	
	public void playDestroyProgressSound(LevelAccessor p_203114_1_, BlockPos p_203114_2_) {
		p_203114_1_.playSound((Player)null, p_203114_2_, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + GlobalVariables.random.nextFloat() * 0.2F);
	}
	
	public void playBreakSound(Level p_203116_1_, BlockPos p_203116_2_) {
		p_203116_1_.playSound((Player)null, p_203116_2_, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + p_203116_1_.random.nextFloat() * 0.2F);
	}
	
	public double acceptedDistance() {
		return 1.14D;
	}
}
