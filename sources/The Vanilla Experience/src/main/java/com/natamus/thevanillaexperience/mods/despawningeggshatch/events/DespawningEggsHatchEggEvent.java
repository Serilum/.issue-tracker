/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
