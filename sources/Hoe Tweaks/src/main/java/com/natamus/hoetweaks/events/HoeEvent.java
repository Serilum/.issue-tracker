/*
 * This is the latest source code of Hoe Tweaks.
 * Minecraft version: 1.19.2, mod version: 1.9.
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

package com.natamus.hoetweaks.events;

import com.natamus.collective.functions.CompareBlockFunctions;
import com.natamus.collective.functions.ToolFunctions;
import com.natamus.hoetweaks.config.ConfigHandler;
import com.natamus.hoetweaks.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MelonBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class HoeEvent {
	@SubscribeEvent
	public void onHoeRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack stack = e.getItemStack();
		if (!ToolFunctions.isHoe(stack)) {
			return;
		}

		Player player = e.getEntity();
		if (ConfigHandler.GENERAL.onlyUntillWithOtherHandEmpty.get()) {
			if (!player.getMainHandItem().isEmpty() && !player.getOffhandItem().isEmpty()) {
				return;
			}
		}

		BlockPos cpos = e.getPos();
		Block block = world.getBlockState(cpos).getBlock();
		
		int damage;
		if (block.equals(Blocks.FARMLAND)) {
			if (!player.isCrouching() && ConfigHandler.GENERAL.mustCrouchToHaveBiggerHoeRange.get()) {
				world.setBlock(cpos, Blocks.DIRT.defaultBlockState(), 3);
				damage = 1;
			}
			else {
				int range = Util.getHoeRange(stack);
				damage = Util.processSoilGetDamage(world, cpos, range, Blocks.DIRT, true);
			}
			
			Vec3 pvec = player.position();
			if (pvec.y % 1 != 0) {
				player.setPos(pvec.x, Math.ceil(pvec.y), pvec.z);
			}
		}
		else if (CompareBlockFunctions.isDirtBlock(block)) {
			if (!player.isCrouching() && ConfigHandler.GENERAL.mustCrouchToHaveBiggerHoeRange.get()) {
				return;
			}
			
			int range = Util.getHoeRange(stack);
			damage = Util.processSoilGetDamage(world, cpos, range, Blocks.FARMLAND, false);
		}
		else {
			return;
		}
		
		world.playSound(null, cpos.getX(), cpos.getY(), cpos.getZ(), SoundEvents.HOE_TILL, SoundSource.BLOCKS, 0.5F, 1.0F);
		e.setCanceled(true);
		
		InteractionHand hand = e.getHand();
		player.swing(hand);
		
		if (!player.isCreative()) {
			stack.hurtAndBreak(damage, player, (player1 -> player1.broadcastBreakEvent(e.getHand())));
		}
	}
	
	@SubscribeEvent
	public void onHarvestBreakSpeed(PlayerEvent.BreakSpeed e) {
		Player player = e.getEntity();
		Level world = player.getCommandSenderWorld();
		
		ItemStack handstack = player.getMainHandItem();
		if (!ToolFunctions.isHoe(handstack)) {
			return;
		}
		
		BlockPos pos = e.getPos();
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof PumpkinBlock || block instanceof MelonBlock) {
			e.setNewSpeed(ConfigHandler.GENERAL.cropBlockBreakSpeedModifier.get().floatValue());
		}
	}
}
