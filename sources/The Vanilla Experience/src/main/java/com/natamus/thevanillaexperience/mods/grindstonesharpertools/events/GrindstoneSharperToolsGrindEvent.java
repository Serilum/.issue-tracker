/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
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

package com.natamus.thevanillaexperience.mods.grindstonesharpertools.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.grindstonesharpertools.config.GrindstoneSharperToolsConfigHandler;
import com.natamus.thevanillaexperience.mods.grindstonesharpertools.util.GrindstoneSharperToolsUtil;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GrindstoneSharperToolsGrindEvent {
	@SubscribeEvent
	public void onDamage(LivingHurtEvent e) {
		Entity source = e.getSource().getEntity();
		if (source == null) {
			return;
		}
		
		Level world = source.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (source instanceof Player == false) {
			return;
		}
		
		Player player = (Player)source;
		ItemStack hand = player.getMainHandItem();
		
		if (ItemFunctions.isTool(hand)) {
			CompoundTag nbtc = hand.getOrCreateTag();
			if (nbtc.contains("sharper")) {
				int sharpleft = nbtc.getInt("sharper")-1;
				
				if (sharpleft > 0) {
					nbtc.putInt("sharper", sharpleft);
					double modifier = GrindstoneSharperToolsConfigHandler.GENERAL.sharpenedDamageModifier.get();
					float damage = e.getAmount();
					e.setAmount(damage * (float)modifier);
					
					int totaluses = GrindstoneSharperToolsConfigHandler.GENERAL.usesAfterGrinding.get();
					if ((double)sharpleft == (double)totaluses*0.75) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 75% of its uses left.", ChatFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.5) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 50% of its uses left.", ChatFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.25) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 25% of its uses left.", ChatFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.1) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 10% of its uses left.", ChatFormatting.BLUE);
					}
				}
				else {
					nbtc.remove("sharper");
					StringFunctions.sendMessage(player, "Your tool is no longer sharpened.", ChatFormatting.RED);
				}
				hand.setTag(nbtc);
				GrindstoneSharperToolsUtil.updateName(hand, sharpleft);
			}
		}
	}
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide || !e.getHand().equals(InteractionHand.MAIN_HAND)) {
			return;
		}
		
		Block block = e.getWorld().getBlockState(e.getPos()).getBlock();
		if (block.equals(Blocks.GRINDSTONE)) {
			Player player = e.getPlayer();
			if (player.isShiftKeyDown()) {
				ItemStack itemstack = e.getItemStack();
				if (ItemFunctions.isTool(itemstack)) {
					CompoundTag nbtc = itemstack.getOrCreateTag();
					int sharpeneduses = GrindstoneSharperToolsConfigHandler.GENERAL.usesAfterGrinding.get();
					
					nbtc.putInt("sharper", sharpeneduses);
					itemstack.setTag(nbtc);
					GrindstoneSharperToolsUtil.updateName(itemstack, sharpeneduses);
					StringFunctions.sendMessage(player, "Your tool has been sharpened with " + sharpeneduses + " uses.", ChatFormatting.DARK_GREEN);
				}
			}
		}
	}
}
