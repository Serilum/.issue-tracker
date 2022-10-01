/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.3.
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

package com.natamus.collective_fabric.fabric.callbacks;

import com.mojang.brigadier.ParseResults;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;

public class CollectiveCommandEvents {
	private CollectiveCommandEvents() { }
	 
    public static final Event<CollectiveCommandEvents.On_Command_Parse> ON_COMMAND_PARSE = EventFactory.createArrayBacked(CollectiveCommandEvents.On_Command_Parse.class, callbacks -> (string, parse) -> {
        for (CollectiveCommandEvents.On_Command_Parse callback : callbacks) {
        	callback.onCommandParse(string, parse);
        }
	});
	
	@FunctionalInterface
	public interface On_Command_Parse {
		 void onCommandParse(String string, ParseResults<CommandSourceStack> parse);
	}
}
