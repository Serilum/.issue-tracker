/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.areas.network;

import java.util.function.Supplier;

import com.natamus.thevanillaexperience.mods.areas.events.AreasGUIEvent;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketToClientShowGUI {
	private String message;
	private String rgbvalue;

	public PacketToClientShowGUI() {}

	public PacketToClientShowGUI(String m, String rgb) {
		this.message = m;
		this.rgbvalue = rgb;
	}

	public PacketToClientShowGUI(PacketBuffer buf) {
		message = buf.readUtf();
		rgbvalue = buf.readUtf();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeUtf(message);
		buf.writeUtf(rgbvalue);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			AreasGUIEvent.hudmessage = message;
			AreasGUIEvent.rgb = rgbvalue;
			AreasGUIEvent.gopacity = 255;
		});
		ctx.get().setPacketHandled(true);
	}
}
