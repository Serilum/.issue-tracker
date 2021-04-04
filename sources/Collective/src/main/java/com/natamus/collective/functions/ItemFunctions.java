/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.25.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemFunctions {
	public static void generateEntityDropsFromLootTable(World world) {
		MinecraftServer server = world.getServer();
		if (server == null) {
			return;
		}
		
		GlobalVariables.entitydrops = new HashMap<EntityType<?>, List<Item>>();
		
		FakePlayer fakeplayer = FakePlayerFactory.getMinecraft((ServerWorld)world);
		Vector3d vec = new Vector3d(0, 0, 0);
		
		ItemStack lootingsword = new ItemStack(Items.DIAMOND_SWORD, 1);
		lootingsword.addEnchantment(Enchantments.LOOTING, 10);
		fakeplayer.setItemStackToSlot(EquipmentSlotType.MAINHAND, lootingsword);
		
		Collection<EntityType<?>> entitytypes = ForgeRegistries.ENTITIES.getValues();
		for (EntityType<?> type : entitytypes) {
			if (type == null) {
				continue;
			}
			Entity entity = type.create(world);
			if (entity == null || entity instanceof LivingEntity == false) {
				continue;
			}
			
			LivingEntity le = (LivingEntity)entity;
			ResourceLocation lootlocation = le.getType().getLootTable();
			
			LootTable loottable = server.getLootTableManager().getLootTableFromLocation(lootlocation);
			LootContext context = new LootContext.Builder((ServerWorld) world)
	                .withRandom(world.getRandom())
	                .withLuck(1000000F)
	                .withParameter(LootParameters.THIS_ENTITY, entity)
	                .withParameter(LootParameters.field_237457_g_, vec)
	                .withParameter(LootParameters.KILLER_ENTITY, fakeplayer)
	                .withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.causePlayerDamage(fakeplayer))
	                .build(LootParameterSets.ENTITY);
			
			List<Item> alldrops = new ArrayList<Item>();
			for (int n = 0; n < ConfigHandler.COLLECTIVE.loopsAmountUsedToGetAllEntityDrops.get(); n++) {
				List<ItemStack> newdrops = loottable.generate(context);
				for (ItemStack newdrop : newdrops) {
					Item newitem = newdrop.getItem();
					if (!alldrops.contains(newitem) && !newitem.equals(Items.AIR)) {
						alldrops.add(newitem);
					}
				}
			}
			
			GlobalVariables.entitydrops.put(type, alldrops);
		}
	}
	
	public static Boolean isTool(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}
		
		Item item = itemstack.getItem();
		if (item instanceof ShovelItem || item instanceof AxeItem || item instanceof PickaxeItem || item instanceof SwordItem || item instanceof HoeItem) {
			return true;
		}
		
		String itemname = item.toString().toLowerCase();
		if (itemname.contains("_sword") || itemname.contains("_pickaxe") || itemname.contains("_axe") || itemname.contains("_shovel") || itemname.contains("_hoe")) {
			return true;
		}
		return false;
	}
	
	public static void shrinkGiveOrDropItemStack(PlayerEntity player, Hand hand, ItemStack used, ItemStack give) {
		used.shrink(1);
		
		if (used.isEmpty()) {
			Item giveitem = give.getItem();
			int maxstacksize = give.getMaxStackSize();
			List<ItemStack> inventory = player.inventory.mainInventory;
			
			boolean increased = false;
			for (int n = 0; n < inventory.size(); n++) {
				ItemStack slot = inventory.get(n);
				if (slot.getItem().equals(giveitem)) {
					int slotcount = slot.getCount();
					if (slotcount < maxstacksize) {
						slot.setCount(slotcount+1);
						increased = true;
						break;
					}
				}
			}
			
			if (!increased) {
				player.setHeldItem(hand, give);
			}
		}
		else if (!player.inventory.addItemStackToInventory(give)) {
			player.dropItem(give, false);
		}
	}
	
	public static void giveOrDropItemStack(PlayerEntity player, ItemStack give) {
		if (!player.inventory.addItemStackToInventory(give)) {
			player.dropItem(give, false);
		}
	}
	
	public static boolean isStoneTypeItem(Item item) {
		if (GlobalVariables.stoneblockitems.contains(item)) {
			return true;
		}
		return false;
	}
	
	public static String itemToReadableString(Item item, int amount) {
		String itemstring = "";
		String translationkey = item.getTranslationKey();
		if (translationkey.contains("block.")) {
			return BlockFunctions.blockToReadableString(Block.getBlockFromItem(item), amount);
		}
		
		String[] itemspl = translationkey.replace("item.", "").split("\\.");
		if (itemspl.length > 1) {
			itemstring = itemspl[1];
		}
		else {
			itemstring = itemspl[0];
		}
		
		itemstring = itemstring.replace("_", " ");
		if (amount > 1) {
			itemstring = itemstring + "s";
		}
		
		return itemstring;
	}
	public static String itemToReadableString(Item item) {
		return itemToReadableString(item, 1);
	}
}