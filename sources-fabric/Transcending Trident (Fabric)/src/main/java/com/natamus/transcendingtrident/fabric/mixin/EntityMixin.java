/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.2, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
