/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.3, mod version: 2.2.
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

package com.natamus.grindstonesharpertools.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.grindstonesharpertools.config.ConfigHandler;
import com.natamus.grindstonesharpertools.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

		Level world = source.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}

		if (!(source instanceof Player)) {
			return;
		}

		Player player = (Player)source;
		ItemStack hand = player.getMainHandItem();

		if (ItemFunctions.isTool(hand)) {
			CompoundTag nbtc = hand.getOrCreateTag();
			if (nbtc.contains("sharper")) {
				int sharpLeft = nbtc.getInt("sharper");
				if (!player.isCreative() || !ConfigHandler.GENERAL.infiniteCreativeUses.get()) {
					sharpLeft--;
				}

				if (sharpLeft > 0) {
					nbtc.putInt("sharper", sharpLeft);
					double modifier = ConfigHandler.GENERAL.sharpenedDamageModifier.get();
					float damage = e.getAmount();
					e.setAmount(damage * (float)modifier);

					if (ConfigHandler.GENERAL.sendUsesLeftInChat.get()) {
						int totalUses = ConfigHandler.GENERAL.usesAfterGrinding.get();
						if ((double) sharpLeft == (double) totalUses * 0.75) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 75% of its uses left.", ChatFormatting.BLUE);
						}
						else if ((double) sharpLeft == (double) totalUses * 0.5) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 50% of its uses left.", ChatFormatting.BLUE);
						}
						else if ((double) sharpLeft == (double) totalUses * 0.25) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 25% of its uses left.", ChatFormatting.BLUE);
						}
						else if ((double) sharpLeft == (double) totalUses * 0.1) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 10% of its uses left.", ChatFormatting.BLUE);
						}
					}
				}
				else {
					nbtc.remove("sharper");
					StringFunctions.sendMessage(player, "Your tool is no longer sharpened.", ChatFormatting.RED);
				}
				hand.setTag(nbtc);
				Util.updateName(hand, sharpLeft);
			}
		}
	}

	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide || !e.getHand().equals(InteractionHand.MAIN_HAND)) {
			return;
		}

		Block block = e.getLevel().getBlockState(e.getPos()).getBlock();
		if (block.equals(Blocks.GRINDSTONE)) {
			Player player = e.getEntity();
			if (player.isShiftKeyDown()) {
				ItemStack itemstack = e.getItemStack();
				if (ItemFunctions.isTool(itemstack)) {
					CompoundTag nbtc = itemstack.getOrCreateTag();
					int sharpeneduses = ConfigHandler.GENERAL.usesAfterGrinding.get();

					nbtc.putInt("sharper", sharpeneduses);
					itemstack.setTag(nbtc);
					Util.updateName(itemstack, sharpeneduses);
					StringFunctions.sendMessage(player, "Your tool has been sharpened with " + sharpeneduses + " uses.", ChatFormatting.DARK_GREEN);
				}
			}
		}
	}
}
