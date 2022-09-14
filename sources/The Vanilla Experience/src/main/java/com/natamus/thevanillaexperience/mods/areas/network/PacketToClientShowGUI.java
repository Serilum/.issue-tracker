/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.areas.network;

import java.util.function.Supplier;

import com.natamus.thevanillaexperience.mods.areas.events.AreasGUIEvent;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class PacketToClientShowGUI {
	private String message;
	private String rgbvalue;

	public PacketToClientShowGUI() {}

	public PacketToClientShowGUI(String m, String rgb) {
		this.message = m;
		this.rgbvalue = rgb;
	}

	public PacketToClientShowGUI(FriendlyByteBuf buf) {
		message = buf.readUtf();
		rgbvalue = buf.readUtf();
	}

	public void toBytes(FriendlyByteBuf buf) {
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
