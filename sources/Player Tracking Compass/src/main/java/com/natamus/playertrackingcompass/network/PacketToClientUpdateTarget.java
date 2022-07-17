/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.19.0, mod version: 2.0.
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

import com.natamus.playertrackingcompass.items.CompassVariables;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketToClientUpdateTarget {
	private int x;
	private int y;
	private int z;

	public PacketToClientUpdateTarget() {}

	public PacketToClientUpdateTarget(BlockPos newTarget) {
		this.x = newTarget.getX();
		this.y = newTarget.getY();
		this.z = newTarget.getZ();
	}

	public PacketToClientUpdateTarget(FriendlyByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			CompassVariables.trackingTarget = new int[]{x, y, z};
		});
		ctx.get().setPacketHandled(true);
	}
}
