/*
 * This is the latest source code of Crying Portals.
 * Minecraft version: 1.19.0, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Crying Portals ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
