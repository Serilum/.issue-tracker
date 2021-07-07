/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.passiveshield.events;

import com.natamus.thevanillaexperience.mods.passiveshield.config.PassiveShieldConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PassiveShieldServerEvent {
	@SubscribeEvent
	public void onEntityDamageTaken(LivingHurtEvent e) {
		Entity target = e.getEntity();
		World world = target.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
			
		if (target instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)target;
			if (player.getMainHandItem().getItem() instanceof ShieldItem == false) {
				if (player.getOffhandItem().getItem() instanceof ShieldItem == false) {
					return;
				}
			}
			
			double modifier = 1.0 -PassiveShieldConfigHandler.GENERAL.passiveShieldPercentageDamageNegated.get();
			e.setAmount((float)(e.getAmount()*modifier));
		}
	}
}
