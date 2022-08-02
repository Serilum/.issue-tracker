/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.areas.network;

import java.util.function.Supplier;

import com.natamus.areas.events.GUIEvent;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
			GUIEvent.hudmessage = message;
			GUIEvent.rgb = rgbvalue;
			GUIEvent.gopacity = 255;
		});
		ctx.get().setPacketHandled(true);
	}
}
