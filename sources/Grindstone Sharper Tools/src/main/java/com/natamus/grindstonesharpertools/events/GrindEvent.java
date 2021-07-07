/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.16.5, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Grindstone Sharper Tools ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.grindstonesharpertools.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.grindstonesharpertools.config.ConfigHandler;
import com.natamus.grindstonesharpertools.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GrindEvent {
	@SubscribeEvent
	public void onDamage(LivingHurtEvent e) {
		Entity source = e.getSource().getEntity();
		if (source == null) {
			return;
		}
		
		World world = source.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (source instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)source;
		ItemStack hand = player.getMainHandItem();
		
		if (ItemFunctions.isTool(hand)) {
			CompoundNBT nbtc = hand.getOrCreateTag();
			if (nbtc.contains("sharper")) {
				int sharpleft = nbtc.getInt("sharper")-1;
				
				if (sharpleft > 0) {
					nbtc.putInt("sharper", sharpleft);
					double modifier = ConfigHandler.GENERAL.sharpenedDamageModifier.get();
					float damage = e.getAmount();
					e.setAmount(damage * (float)modifier);
					
					int totaluses = ConfigHandler.GENERAL.usesAfterGrinding.get();
					if ((double)sharpleft == (double)totaluses*0.75) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 75% of its uses left.", TextFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.5) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 50% of its uses left.", TextFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.25) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 25% of its uses left.", TextFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.1) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 10% of its uses left.", TextFormatting.BLUE);
					}
				}
				else {
					nbtc.remove("sharper");
					StringFunctions.sendMessage(player, "Your tool is no longer sharpened.", TextFormatting.RED);
				}
				hand.setTag(nbtc);
				Util.updateName(hand, sharpleft);
			}
		}
	}
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide || !e.getHand().equals(Hand.MAIN_HAND)) {
			return;
		}
		
		Block block = e.getWorld().getBlockState(e.getPos()).getBlock();
		if (block.equals(Blocks.GRINDSTONE)) {
			PlayerEntity player = e.getPlayer();
			if (player.isShiftKeyDown()) {
				ItemStack itemstack = e.getItemStack();
				if (ItemFunctions.isTool(itemstack)) {
					CompoundNBT nbtc = itemstack.getOrCreateTag();
					int sharpeneduses = ConfigHandler.GENERAL.usesAfterGrinding.get();
					
					nbtc.putInt("sharper", sharpeneduses);
					itemstack.setTag(nbtc);
					Util.updateName(itemstack, sharpeneduses);
					StringFunctions.sendMessage(player, "Your tool has been sharpened with " + sharpeneduses + " uses.", TextFormatting.DARK_GREEN);
				}
			}
		}
	}
}
