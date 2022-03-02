/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.18.2, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Player Tracking Compass ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
