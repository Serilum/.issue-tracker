/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.8.
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
