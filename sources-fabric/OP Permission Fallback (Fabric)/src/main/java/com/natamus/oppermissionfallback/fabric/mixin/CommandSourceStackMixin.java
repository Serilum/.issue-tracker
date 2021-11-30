/*
 * This is the latest source code of OP Permission Fallback.
 * Minecraft version: 1.18.x, mod version: 1.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of OP Permission Fallback ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.oppermissionfallback.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.oppermissionfallback.config.ConfigHandler;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

@Mixin(CommandSourceStack.class)
public class CommandSourceStackMixin {
	@Shadow private Entity entity;
	
    @Inject(method = "hasPermission(I)Z", at = @At(value = "RETURN"), cancellable = true)
    private void hasPermission(int i, CallbackInfoReturnable<Boolean> cir) {
    	if (this.entity != null) {
    		if (Permissions.check(this.entity, ConfigHandler.defaultOpPermissionString.getValue())) {
    			cir.setReturnValue(true);
    		}
    	}
    }
}
