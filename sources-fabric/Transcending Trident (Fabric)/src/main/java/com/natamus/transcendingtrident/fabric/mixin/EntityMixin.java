/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.x, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Transcending Trident ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.transcendingtrident.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.transcendingtrident.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TridentItem;

@Mixin(value = Entity.class, priority = 1001)
public class EntityMixin {
    @Inject(method = "isInWaterOrRain()Z", at = @At(value = "RETURN"), cancellable = true)
    private void isInWaterOrRain(CallbackInfoReturnable<Boolean> ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof Player) {
        	Player player = (Player)entity;
        	if (PlayerFunctions.isHoldingWater(player) || !ConfigHandler.mustHoldBucketOfWater.getValue()) {
        		if (player.getMainHandItem().getItem() instanceof TridentItem || player.getOffhandItem().getItem()  instanceof TridentItem) {
        			ci.setReturnValue(true);
        		}
        	}
        }
    }
}
