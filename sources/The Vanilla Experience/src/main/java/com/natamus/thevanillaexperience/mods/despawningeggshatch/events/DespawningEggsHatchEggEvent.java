/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

import net.minecraft.block.Block;
import net.minecraft.block.HayBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DespawningEggsHatchEggEvent {
	@SubscribeEvent
	public void onItemExpire(ItemExpireEvent e) {
		ItemEntity entityitem = e.getEntityItem();
		World world = entityitem.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		ItemStack itemstack = entityitem.getItem();
		if (!itemstack.getItem().equals(Items.EGG)) {
			return;
		}
		
		if (DespawningEggsHatchConfigHandler.GENERAL.eggOnlyHatchesWhenOnTopOfHayBlock.get()) {
			BlockPos blockpos = entityitem.getPosition();
			Block belowblock = world.getBlockState(blockpos.down()).getBlock();
			if (belowblock instanceof HayBlock == false) {
				return;
			}
		}
		
		double num = GlobalVariables.random.nextDouble();
		if (num <= DespawningEggsHatchConfigHandler.GENERAL.eggWillHatchChance.get()) {
			int itemamount = itemstack.getCount();
			
			int moblimit = DespawningEggsHatchConfigHandler.GENERAL.onlyHatchIfLessChickensAroundThan.get();
			Vector3d iposvec = entityitem.getPositionVec();
			
			int r = DespawningEggsHatchConfigHandler.GENERAL.radiusEntityLimiterCheck.get();
			int chickencount = 0;
			Iterator<Entity> it = world.getEntitiesWithinAABBExcludingEntity(entityitem, new AxisAlignedBB(iposvec.getX()-r, iposvec.getY()-r, iposvec.getZ()-r, iposvec.getX()+r, iposvec.getY()+r, iposvec.getZ()+r)).iterator();
			while (it.hasNext()) {
				Entity ne = it.next();
				if (ne instanceof ChickenEntity) {
					chickencount++;
				}
			}
			
			for (int n = 1; n <= itemamount; n++) {
				if (chickencount > moblimit) {
					return;
				}
				
				ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
				chicken.setPosition(iposvec.x, iposvec.y+1, iposvec.z);
				if (DespawningEggsHatchConfigHandler.GENERAL.newHatchlingIsBaby.get()) {
					chicken.setGrowingAge(-24000);
				}
				
				world.addEntity(chicken);
				chickencount++;
			}
		}
	}
}
