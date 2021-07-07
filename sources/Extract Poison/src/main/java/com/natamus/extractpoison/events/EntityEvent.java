/*
 * This is the latest source code of Extract Poison.
 * Minecraft version: 1.16.5, mod version: 1.5.
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.extractpoison.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	private static Map<UUID, LocalTime> lastuse = new HashMap<UUID, LocalTime>();
	
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			Entity target = e.getTarget();
			String entityname = EntityFunctions.getEntityString(target);
			if (entityname.contains("CaveSpiderEntity") || target instanceof CaveSpiderEntity || target instanceof PufferfishEntity || target instanceof BeeEntity) {
				PlayerEntity player = e.getPlayer();
				e.setCanceled(true);
				
				LocalTime now = LocalTime.now();
				UUID targetuuid = target.getUUID();
				if (lastuse.containsKey(targetuuid)) {
					LocalTime lastnow = lastuse.get(targetuuid); 
					
					int msbetween = (int)Duration.between(lastnow, now).toMillis();
					if (msbetween < ConfigHandler.GENERAL.extractDelayMs.get()) {
						return;
					}
				}
				
				ItemStack poison = new ItemStack(Items.POTION, 1);
				PotionUtils.setPotion(poison, Potions.POISON);
				
				ItemFunctions.shrinkGiveOrDropItemStack(player, e.getHand(), itemstack, poison);
				lastuse.put(targetuuid, now);
			}
		}
	}
	
	@SubscribeEvent
	public void onWaterClick(PlayerInteractEvent.RightClickItem e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			PlayerEntity player = e.getPlayer();
			BlockPos pos = e.getPos();
			List<Entity> entitiesaround = world.getEntities(player, new AxisAlignedBB(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1));
			for (Entity ea : entitiesaround) {
				if (ea instanceof PufferfishEntity) {
					e.setCanceled(true);
					return;
				}
			}
		}
	}
}
