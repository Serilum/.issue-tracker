/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.44.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
