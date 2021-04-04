/*
 * This is the latest source code of Wool Tweaks.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Wool Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.wooltweaks.events;

import com.natamus.wooltweaks.util.Util;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WoolClickEvent {
	@SubscribeEvent
	public void onWoolClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		ItemStack handstack = e.getItemStack();
		Item handitem = handstack.getItem();
		if (handitem instanceof DyeItem == false) {
			return;
		}
		
		BlockPos target = e.getPos();
		BlockState state = world.getBlockState(target);
		Block block = state.getBlock();
		
		Block newblock = null;
		if (block.getTags().contains(new ResourceLocation("wool"))) {
			newblock = Util.woolblocks.get(handitem);
		}
		else if (block instanceof BedBlock) {
			newblock = Util.bedblocks.get(handitem);
		}
		else if (block instanceof CarpetBlock) {
			newblock = Util.carpetblocks.get(handitem);
		}
		else {
			return;
		}
		
		if (newblock == null) {
			return;
		}
		
		if (block.equals(newblock)) {
			return;
		}
		
		e.setCanceled(true);
		
		BlockState newstate = newblock.getDefaultState();
		if (block instanceof BedBlock) {
			Direction direction = state.get(BedBlock.HORIZONTAL_FACING);
			newstate = newstate.with(BedBlock.HORIZONTAL_FACING, direction);
			newstate = newstate.with(BedBlock.OCCUPIED, state.get(BedBlock.OCCUPIED));
			
			BedPart bedpart = state.get(BedBlock.PART);
			newstate = newstate.with(BedBlock.PART, bedpart);
			
			BlockPos othertarget = target.toImmutable();
			BedPart otherpart;
			if (bedpart.equals(BedPart.HEAD)) {
				otherpart = BedPart.FOOT;
				othertarget = target.offset(direction.getOpposite());
				
				world.setBlockState(target, Blocks.AIR.getDefaultState());
				world.setBlockState(othertarget, Blocks.AIR.getDefaultState());
			}
			else {
				otherpart = BedPart.HEAD;
				othertarget = target.offset(direction);
				
				world.setBlockState(othertarget, Blocks.AIR.getDefaultState());
				world.setBlockState(target, Blocks.AIR.getDefaultState());
			}
			
			world.setBlockState(othertarget, newstate.with(BedBlock.PART, otherpart));
		}
		
		world.setBlockState(target, newstate);
		
		PlayerEntity player = e.getPlayer();
		if (!player.isCreative()) {
			handstack.shrink(1);
		}
	}
}