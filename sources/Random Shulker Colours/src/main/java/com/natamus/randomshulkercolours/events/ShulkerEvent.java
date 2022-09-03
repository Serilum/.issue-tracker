/*
 * This is the latest source code of Random Shulker Colours.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.randomshulkercolours.events;

import java.util.List;
import java.util.Set;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.randomshulkercolours.util.Reference;
import com.natamus.randomshulkercolours.util.Util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ShulkerEvent {
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void onShulkerSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Shulker == false) {
			return;
		}
		
		String shulkertag = Reference.MOD_ID + ".coloured";
		Set<String> tags = entity.getTags();
		if (tags.contains(shulkertag)) {
			return;
		}
		
		if (Util.possibleColours == null) {
			return;
		}
		if (Util.possibleColours.size() < 1) {
			return;
		}
		
		Shulker shulker = (Shulker)entity;
		
		int randomindex = GlobalVariables.random.nextInt(Util.possibleColours.size());
		final DyeColor randomcolour = Util.possibleColours.get(randomindex);
		if (randomcolour != null) {
			int dyeid = randomcolour.getId();

			SynchedEntityData datamanager = shulker.getEntityData();
			
			EntityDataAccessor<Byte> paramater = null;
			
			List<DataItem<?>> dataentries = datamanager.packDirty();
			for (DataItem<?> entry : dataentries) {
				EntityDataAccessor<?> key = entry.getAccessor();
				Object value = entry.getValue();
				if (value.toString().equals("16")) {
					paramater = (EntityDataAccessor<Byte>)key;
				}
			}
			
			if (paramater != null) {
				datamanager.set(paramater, (byte)dyeid);
			}
		}
		
		shulker.addTag(shulkertag);
	}
}
