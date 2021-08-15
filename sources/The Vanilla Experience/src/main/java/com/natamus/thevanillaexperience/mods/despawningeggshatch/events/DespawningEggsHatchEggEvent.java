/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.despawningeggshatch.events;

import java.util.Iterator;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.thevanillaexperience.mods.despawningeggshatch.config.DespawningEggsHatchConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HayBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DespawningEggsHatchEggEvent {
	@SubscribeEvent
	public void onItemExpire(ItemExpireEvent e) {
		ItemEntity entityitem = e.getEntityItem();
		Level world = entityitem.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = entityitem.getItem();
		if (!itemstack.getItem().equals(Items.EGG)) {
			return;
		}
		
		if (DespawningEggsHatchConfigHandler.GENERAL.eggOnlyHatchesWhenOnTopOfHayBlock.get()) {
			BlockPos blockpos = entityitem.blockPosition();
			Block belowblock = world.getBlockState(blockpos.below()).getBlock();
			if (belowblock instanceof HayBlock == false) {
				return;
			}
		}
		
		double num = GlobalVariables.random.nextDouble();
		if (num <= DespawningEggsHatchConfigHandler.GENERAL.eggWillHatchChance.get()) {
			int itemamount = itemstack.getCount();
			
			int moblimit = DespawningEggsHatchConfigHandler.GENERAL.onlyHatchIfLessChickensAroundThan.get();
			Vec3 iposvec = entityitem.position();
			
			int r = DespawningEggsHatchConfigHandler.GENERAL.radiusEntityLimiterCheck.get();
			int chickencount = 0;
			Iterator<Entity> it = world.getEntities(entityitem, new AABB(iposvec.x()-r, iposvec.y()-r, iposvec.z()-r, iposvec.x()+r, iposvec.y()+r, iposvec.z()+r)).iterator();
			while (it.hasNext()) {
				Entity ne = it.next();
				if (ne instanceof Chicken) {
					chickencount++;
				}
			}
			
			for (int n = 1; n <= itemamount; n++) {
				if (chickencount > moblimit) {
					return;
				}
				
				Chicken chicken = new Chicken(EntityType.CHICKEN, world);
				chicken.setPos(iposvec.x, iposvec.y+1, iposvec.z);
				if (DespawningEggsHatchConfigHandler.GENERAL.newHatchlingIsBaby.get()) {
					chicken.setAge(-24000);
				}
				
				world.addFreshEntity(chicken);
				chickencount++;
			}
		}
	}
}
