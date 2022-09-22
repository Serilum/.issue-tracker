/*
 * This is the latest source code of End Portal Recipe.
 * Minecraft version: 1.19.2, mod version: 4.0.
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

package com.natamus.endportalrecipe.events;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.MessageFunctions;
import com.natamus.collective.functions.ToolFunctions;
import com.natamus.endportalrecipe.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class EndPortalEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!(entity instanceof EnderDragon)) {
			return;
		}

		ItemStack egg = new ItemStack(Blocks.DRAGON_EGG, 1);

		Player playertodrop = null;
		Entity source = e.getSource().getEntity();
		if (!(entity instanceof Player)) {
			BlockPos pos = entity.blockPosition();
			List<Entity> entitiesaround = world.getEntities(null, new AABB(pos.getX()-50, pos.getY()-50, pos.getZ()-50, pos.getX()+50, pos.getY()+50, pos.getZ()+50));
			for (Entity ea : entitiesaround) {
				if (ea instanceof Player) {
					playertodrop = (Player)ea;
					break;
				}
			}

			if (playertodrop == null) {
				return;
			}
		}
		else {
			playertodrop = (Player)source;
		}

		ItemFunctions.giveOrDropItemStack(playertodrop, egg);

		if (ConfigHandler.GENERAL.sendMessageOnExtraDragonEggDrop.get()) {
			MessageFunctions.sendMessage(playertodrop, "An extra dragon egg has dropped at your position!", ChatFormatting.DARK_GREEN);
		}
	}

	@SuppressWarnings( "deprecation" )
	@SubscribeEvent
	public void onLeftClick(PlayerInteractEvent.LeftClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (ToolFunctions.isPickaxe(hand)) {
			if (ConfigHandler.GENERAL.mustHaveSilkTouchToBreakPortal.get()) {
				if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, hand) < 1) {
					return;
				}
			}
			
			BlockPos cpos = e.getPos().immutable();
			BlockState cbs = world.getBlockState(cpos);
			if (cbs.getBlock().equals(Blocks.END_PORTAL_FRAME)) {
				Player player = e.getEntity();
				ItemStack portalframe = new ItemStack(Blocks.END_PORTAL_FRAME, 1);
				ItemStack endereye = new ItemStack(Items.ENDER_EYE, 1);
				
				if (ConfigHandler.GENERAL.addBrokenPortalFramesToInventory.get()) {
					ItemFunctions.giveOrDropItemStack(player, portalframe);
					if (BlockFunctions.isFilledPortalFrame(cbs)) {
						ItemFunctions.giveOrDropItemStack(player, endereye);
					}
				}
				else {
					ItemEntity frame = new ItemEntity(world, cpos.getX(), cpos.getY()+1, cpos.getZ(), portalframe);
					world.addFreshEntity(frame);
					
					if (BlockFunctions.isFilledPortalFrame(cbs)) {
						ItemEntity eoe = new ItemEntity(world, cpos.getX(), cpos.getY()+1, cpos.getZ(), endereye);
						world.addFreshEntity(eoe);
					}
				}
				
				world.destroyBlock(cpos, false);
				Iterator<BlockPos> it = BlockPos.betweenClosedStream(cpos.getX()-3,  cpos.getY(), cpos.getZ()-3, cpos.getX()+3, cpos.getY(), cpos.getZ()+3).iterator();
				while (it.hasNext()) {
					BlockPos np = it.next();
					if (world.getBlockState(np).getBlock().equals(Blocks.END_PORTAL)) {
						world.setBlockAndUpdate(np, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}
}
