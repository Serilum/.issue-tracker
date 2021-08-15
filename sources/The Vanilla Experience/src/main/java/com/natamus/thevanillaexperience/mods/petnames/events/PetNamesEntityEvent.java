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

package com.natamus.thevanillaexperience.mods.petnames.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.petnames.config.PetNamesConfigHandler;
import com.natamus.thevanillaexperience.mods.petnames.util.PetNamesUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PetNamesEntityEvent {
	@SubscribeEvent
	public void onBaby(BabyEntitySpawnEvent e) {
		Entity entity = e.getChild();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (!PetNamesUtil.isNamable(entity)) {
			return;
		}
		
		EntityFunctions.nameEntity(entity, StringFunctions.getRandomName(PetNamesConfigHandler.GENERAL._useMaleNames.get(), PetNamesConfigHandler.GENERAL._useFemaleNames.get()));
	}
}
