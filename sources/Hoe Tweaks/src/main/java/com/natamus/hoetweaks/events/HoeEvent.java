/*
 * This is the latest source code of Hoe Tweaks.
 * Minecraft version: 1.18.0, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hoe Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hoetweaks.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.CompareBlockFunctions;
import com.natamus.collective.functions.ToolFunctions;
import com.natamus.hoetweaks.config.ConfigHandler;
import com.natamus.hoetweaks.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack stack = e.getItemStack();
		if (!ToolFunctions.isHoe(stack)) {
			return;
		}
		
		Player player = e.getPlayer();
		BlockPos cpos = e.getPos();
		Block block = world.getBlockState(cpos).getBlock();
		
		int damage = 0;
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
			stack.hurt(damage, GlobalVariables.random, (ServerPlayer)player);
		}
	}
	
	@SubscribeEvent
	public void onHarvestBreakSpeed(PlayerEvent.BreakSpeed e) {
		Player player = e.getPlayer();
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
