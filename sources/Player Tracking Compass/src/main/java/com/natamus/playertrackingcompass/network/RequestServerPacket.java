/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.playertrackingcompass.network;

import java.util.function.Supplier;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.playertrackingcompass.Main;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class RequestServerPacket {
	public RequestServerPacket() {}

	public RequestServerPacket(FriendlyByteBuf buf) {}

	public void fromBytes(FriendlyByteBuf buf) {}

	public void toBytes(FriendlyByteBuf buf) {}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			BlockPos targetpos = new BlockPos(0, 0, 0);
			
			ServerPlayer serverplayer = ctx.get().getSender();
			BlockPos serverplayerpos = serverplayer.blockPosition();
			BlockPos comparepp = new BlockPos(serverplayerpos.getX(), 1, serverplayerpos.getZ());
			
			ServerPlayer closestplayer = null;
			double closestdistance = 999999999999.0;
			
			ServerLevel world = serverplayer.getLevel();
			for (ServerPlayer oplayer : world.players()) {
				BlockPos oplayerpos = oplayer.blockPosition();
				BlockPos compareop = new BlockPos(oplayerpos.getX(), 1, oplayerpos.getZ());

				double distance = comparepp.distManhattan(compareop);
				if (distance < 10) {
					continue;
				}
				if (distance < closestdistance) {
					closestdistance = distance;
					closestplayer = oplayer;
				}
			}
			
			if (closestplayer != null) {
				targetpos = closestplayer.blockPosition().immutable();
				
				StringFunctions.sendMessage(serverplayer, "The compass is pointing at " + closestplayer.getName().getString() + ".", ChatFormatting.YELLOW);
			}
			else {
				StringFunctions.sendMessage(serverplayer, "Unable to redirect the compass. There are no players around or they're too close.", ChatFormatting.YELLOW);
			}
			Main.network.sendTo(new PacketToClientUpdateTarget(targetpos), serverplayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		});
		ctx.get().setPacketHandled(true);
	}
}
