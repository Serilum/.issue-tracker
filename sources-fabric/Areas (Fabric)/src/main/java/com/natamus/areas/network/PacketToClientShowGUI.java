/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.1.
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

package com.natamus.areas.network;

import com.natamus.areas.events.GUIEvent;
import com.natamus.areas.objects.Variables;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class PacketToClientShowGUI {
	public static FriendlyByteBuf createBuffer(String message, String rgb) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		buf.writeUtf(message);
		buf.writeUtf(rgb);
		return buf;
	}

	public static void registerHandle() {
		ClientPlayNetworking.registerGlobalReceiver(Variables.networkchannel, (client, handler, buf, responseSender) -> {
			String hudmessagevalue = buf.readUtf();
			String rgbvalue = buf.readUtf();

			client.execute(() -> {
				GUIEvent.hudmessage = hudmessagevalue;
				GUIEvent.rgb = rgbvalue;
				GUIEvent.gopacity = 255;
			});
		});
	}
}
