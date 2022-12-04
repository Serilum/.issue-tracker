/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.17.
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

import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Commands.class, priority = 1001)
public class CommandsMixin {
	/*@Shadow private @Final CommandDispatcher<CommandSourceStack> dispatcher;
	
    @Inject(method = "performCommand(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void Commands_performCommand(CommandSourceStack commandSourceStack, String string, CallbackInfoReturnable<Integer> cir, StringReader stringReader) {
    	ParseResults<CommandSourceStack> parse = this.dispatcher.parse(stringReader, commandSourceStack);
    	CollectiveCommandEvents.ON_COMMAND_PARSE.invoker().onCommandParse(string, parse);
    }*/
}
