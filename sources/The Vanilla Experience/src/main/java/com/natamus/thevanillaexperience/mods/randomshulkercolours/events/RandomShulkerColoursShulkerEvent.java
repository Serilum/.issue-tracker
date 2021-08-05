/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.randomshulkercolours.events;

import java.util.List;
import java.util.Set;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.thevanillaexperience.mods.randomshulkercolours.util.Reference;
import com.natamus.thevanillaexperience.mods.randomshulkercolours.util.RandomShulkerColoursUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RandomShulkerColoursShulkerEvent {
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void onShulkerSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ShulkerEntity == false) {
			return;
		}
		
		String shulkertag = Reference.MOD_ID + ".coloured";
		Set<String> tags = entity.getTags();
		if (tags.contains(shulkertag)) {
			return;
		}
		
		if (RandomShulkerColoursUtil.possibleColours == null) {
			return;
		}
		if (RandomShulkerColoursUtil.possibleColours.size() < 1) {
			return;
		}
		
		ShulkerEntity shulker = (ShulkerEntity)entity;
		
		int randomindex = GlobalVariables.random.nextInt(RandomShulkerColoursUtil.possibleColours.size());
		final DyeColor randomcolour = RandomShulkerColoursUtil.possibleColours.get(randomindex);
		if (randomcolour != null) {
			int dyeid = randomcolour.getId();

			EntityDataManager datamanager = shulker.getEntityData();
			
			DataParameter<Byte> paramater = null;
			
			List<DataEntry<?>> dataentries = datamanager.packDirty();
			for (DataEntry<?> entry : dataentries) {
				DataParameter<?> key = entry.getAccessor();
				Object value = entry.getValue();
				if (value.toString().equals("16")) {
					paramater = (DataParameter<Byte>)key;
				}
			}
			
			if (paramater != null) {
				datamanager.set(paramater, (byte)dyeid);
			}
		}
		
		shulker.addTag(shulkertag);
	}
}
