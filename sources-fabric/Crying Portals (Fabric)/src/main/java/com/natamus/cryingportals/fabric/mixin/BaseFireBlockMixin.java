/*
 * This is the latest source code of Crying Portals.
 * Minecraft version: 1.19.1, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.cryingportals.fabric.mixin;

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
	private static boolean BaseFireBlock_isPortal(boolean bl, Level level, BlockPos blockPos, Direction direction) {
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
		Direction[] var5 = Direction.values();
		int var6 = var5.length;

		for(int var7 = 0; var7 < var6; ++var7) {
			Direction direction2 = var5[var7];
			if (level.getBlockState(mutableBlockPos.set(blockPos).move(direction2)).is(Blocks.CRYING_OBSIDIAN)) {
				return true;
			}
		}
		
		return false;
	}
}
