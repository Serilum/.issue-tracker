/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.11.
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

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = TeleportCommand.class, priority = 1001)
public abstract class TeleportCommandMixin {
	@Inject(method = "performTeleport(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFLnet/minecraft/server/commands/TeleportCommand$LookAt;)V", at = @At(value = "HEAD"), cancellable = true)
	private static void performTeleport(CommandSourceStack commandSourceStack, Entity entity, ServerLevel serverLevel, double d, double e, double f, Set<ClientboundPlayerPositionPacket.RelativeArgument> set, float g, float h, TeleportCommand.LookAt lookAt, CallbackInfo ci) {
		if (!CollectiveEntityEvents.ON_ENTITY_TELEPORT_COMMAND.invoker().onTeleportCommand(serverLevel, entity, d, e, f)) {
			ci.cancel();
		}
	}
}
