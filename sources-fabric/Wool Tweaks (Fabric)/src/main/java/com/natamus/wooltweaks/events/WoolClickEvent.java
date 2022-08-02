/*
 * This is the latest source code of Wool Tweaks.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.wooltweaks.events;

import com.natamus.wooltweaks.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class WoolClickEvent {
	public static InteractionResult onWoolClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		ItemStack handstack = player.getItemInHand(hand);
		Item handitem = handstack.getItem();
		if (!(handitem instanceof DyeItem)) {
			return InteractionResult.PASS;
		}
		
		Vec3 targetvec = hitResult.getLocation();
		BlockPos target = new BlockPos(targetvec.x, targetvec.y, targetvec.z);
		BlockState state = world.getBlockState(target);
		Block block = state.getBlock();
		
		Block newblock = null;
		if (block.builtInRegistryHolder().is(BlockTags.WOOL)) {
			newblock = Util.woolblocks.get(handitem);
		}
		else if (block instanceof BedBlock) {
			newblock = Util.bedblocks.get(handitem);
		}
		else if (block instanceof WoolCarpetBlock) {
			newblock = Util.carpetblocks.get(handitem);
		}
		else {
			return InteractionResult.PASS;
		}
		
		if (newblock == null) {
			return InteractionResult.PASS;
		}
		
		if (block.equals(newblock)) {
			return InteractionResult.PASS;
		}
		
		BlockState newstate = newblock.defaultBlockState();
		if (block instanceof BedBlock) {
			Direction direction = state.getValue(BedBlock.FACING);
			newstate = newstate.setValue(BedBlock.FACING, direction);
			newstate = newstate.setValue(BedBlock.OCCUPIED, state.getValue(BedBlock.OCCUPIED));
			
			BedPart bedpart = state.getValue(BedBlock.PART);
			newstate = newstate.setValue(BedBlock.PART, bedpart);
			
			BlockPos othertarget = target.immutable();
			BedPart otherpart;
			if (bedpart.equals(BedPart.HEAD)) {
				otherpart = BedPart.FOOT;
				othertarget = target.relative(direction.getOpposite());
				
				world.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(othertarget, Blocks.AIR.defaultBlockState());
			}
			else {
				otherpart = BedPart.HEAD;
				othertarget = target.relative(direction);
				
				world.setBlockAndUpdate(othertarget, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState());
			}
			
			world.setBlockAndUpdate(othertarget, newstate.setValue(BedBlock.PART, otherpart));
		}
		
		world.setBlockAndUpdate(target, newstate);
		
		if (!player.isCreative()) {
			handstack.shrink(1);
		}
		
		return InteractionResult.SUCCESS;
	}
}