/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
