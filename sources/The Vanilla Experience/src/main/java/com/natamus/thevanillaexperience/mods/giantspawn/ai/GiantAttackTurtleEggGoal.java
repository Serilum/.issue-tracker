/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.giantspawn.ai;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.BreakBlockGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GiantAttackTurtleEggGoal extends BreakBlockGoal {
	public GiantAttackTurtleEggGoal(CreatureEntity p_i50465_2_, double p_i50465_3_, int p_i50465_5_) {
		super(Blocks.TURTLE_EGG, p_i50465_2_, p_i50465_3_, p_i50465_5_);
	}
	
	public void playDestroyProgressSound(IWorld p_203114_1_, BlockPos p_203114_2_) {
		p_203114_1_.playSound((PlayerEntity)null, p_203114_2_, SoundEvents.ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5F, 0.9F + GlobalVariables.random.nextFloat() * 0.2F);
	}
	
	public void playBreakSound(World p_203116_1_, BlockPos p_203116_2_) {
		p_203116_1_.playSound((PlayerEntity)null, p_203116_2_, SoundEvents.TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + p_203116_1_.random.nextFloat() * 0.2F);
	}
	
	public double acceptedDistance() {
		return 1.14D;
	}
}
