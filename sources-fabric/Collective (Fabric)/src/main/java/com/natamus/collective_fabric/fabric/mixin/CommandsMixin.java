/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.5.
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

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCommandEvents;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

@Mixin(value = Commands.class, priority = 1001)
public class CommandsMixin {
	@Shadow private @Final CommandDispatcher<CommandSourceStack> dispatcher;
	
    @Inject(method = "performCommand(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void Commands_performCommand(CommandSourceStack commandSourceStack, String string, CallbackInfoReturnable<Integer> cir, StringReader stringReader) {
    	ParseResults<CommandSourceStack> parse = this.dispatcher.parse(stringReader, commandSourceStack);
    	CollectiveCommandEvents.ON_COMMAND_PARSE.invoker().onCommandParse(string, parse);
    }
}
