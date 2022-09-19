/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.64.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.mixin;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 1001)
public class MinecraftMixin {
    @Inject(method = "createUserApiService", cancellable = true, at = @At(value = "HEAD"))
    public void Minecraft_createUserApiService(YggdrasilAuthenticationService yggdrasilAuthenticationService, GameConfig gameConfig, CallbackInfoReturnable<UserApiService> cir) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            System.out.println("Failed to verify authentication");
            cir.setReturnValue(UserApiService.OFFLINE);
        }
    }
}
