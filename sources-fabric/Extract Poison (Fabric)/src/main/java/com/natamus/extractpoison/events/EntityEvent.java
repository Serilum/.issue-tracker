/*
 * This is the latest source code of Extract Poison.
 * Minecraft version: 1.17.x, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Extract Poison ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.extractpoison.events;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.ItemFunctions;
import com.natamus.extractpoison.config.ConfigHandler;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EntityEvent {
	private static Map<UUID, LocalTime> lastuse = new HashMap<UUID, LocalTime>();
	
	public static InteractionResult onEntityInteract(Player player, Level world, InteractionHand hand, Entity target, EntityHitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			String entityname = EntityFunctions.getEntityString(target);
			if (entityname.contains("CaveSpider") || target instanceof CaveSpider || target instanceof Pufferfish || target instanceof Bee) {
				LocalTime now = LocalTime.now();
				UUID targetuuid = target.getUUID();
				if (lastuse.containsKey(targetuuid)) {
					LocalTime lastnow = lastuse.get(targetuuid); 
					
					int msbetween = (int)Duration.between(lastnow, now).toMillis();
					if (msbetween < ConfigHandler.extractDelayMs.getValue()) {
						return InteractionResult.FAIL;
					}
				}
				
				ItemStack poison = new ItemStack(Items.POTION, 1);
				PotionUtils.setPotion(poison, Potions.POISON);
				
				ItemFunctions.shrinkGiveOrDropItemStack(player, hand, itemstack, poison);
				lastuse.put(targetuuid, now);
				
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.PASS;
	}
}
