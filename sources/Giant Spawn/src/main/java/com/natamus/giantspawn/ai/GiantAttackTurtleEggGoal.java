/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.0, mod version: 3.0.
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

package com.natamus.giantspawn.ai;

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
