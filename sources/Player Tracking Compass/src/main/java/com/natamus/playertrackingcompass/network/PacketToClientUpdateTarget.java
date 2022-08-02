/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
