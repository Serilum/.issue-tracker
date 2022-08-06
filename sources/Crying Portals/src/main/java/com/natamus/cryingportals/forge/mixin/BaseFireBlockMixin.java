/*
 * This is the latest source code of Crying Portals.
 * Minecraft version: 1.19.2, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.cryingportals.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;

@Mixin(value = BaseFireBlock.class, priority = 1001)
public class BaseFireBlockMixin {
	@ModifyVariable(method = "isPortal", at = @At(value = "INVOKE", target="Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"))
	private static boolean BaseFireBlock_isPortal(boolean flag, Level level, BlockPos blockPos, Direction direction) {
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

		for (Direction direction2 : Direction.values()) {
			if (level.getBlockState(mutableBlockPos.set(blockPos).move(direction2)).is(Blocks.CRYING_OBSIDIAN)) {
				return true;
			}
		}
		
		return false;
	}
}
