/*
 * This is the latest source code of Configurable Extra Mob Drops.
 * Minecraft version: 1.18.1, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Extra Mob Drops ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.configurableextramobdrops.events;

import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.configurableextramobdrops.util.Util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MobDropEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		EntityType<?> entitytype = entity.getType();
		if (!Util.mobdrops.containsKey(entitytype)) {
			return;
		}
		
		CopyOnWriteArrayList<ItemStack> extradrops = Util.mobdrops.get(entitytype);
		if (extradrops.size() > 0) {
			BlockPos epos = entity.blockPosition();
			
			for (ItemStack itemstack : extradrops) {
				ItemStack newstack = itemstack.copy();
				CompoundTag tag = newstack.getOrCreateTag();
				
				CompoundTag nbt = new CompoundTag();
				newstack.save(nbt);
				
				
				if (tag.contains("dropchance")) {
					double dropchance = tag.getDouble("dropchance");
					if (dropchance != 1.0) {
						double chanceroll = GlobalVariables.random.nextDouble();
						if (chanceroll > dropchance) {
							continue;
						}
					}
					tag.remove("dropchance");
					if (tag.size() == 0) {
						nbt.remove("tag");
						newstack = ItemStack.of(nbt);
					}
					else {
						newstack.setTag(tag);
					}
				}
				
				e.getDrops().add(new ItemEntity(world, epos.getX(), epos.getY()+1, epos.getZ(), newstack.copy()));
			}
		}
	}
}
