/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.13.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
