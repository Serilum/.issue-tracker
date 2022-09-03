/*
 * This is the latest source code of OP Permission Fallback.
 * Minecraft version: 1.19.2, mod version: 1.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
