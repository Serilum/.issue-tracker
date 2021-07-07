/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.16.5, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Areas ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.areas.objects.AreaObject;
import com.natamus.areas.objects.Variables;
import com.natamus.areas.util.Util;
import com.natamus.collective.functions.FABFunctions;
import com.natamus.collective.functions.StringFunctions;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CommandAreas {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("areas")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				PlayerEntity player = source.getPlayerOrException();
				World world = player.getCommandSenderWorld();
				
				Vector3d pvec = player.position();
				boolean sentfirst = false;
				
				List<BlockPos> signsaround = FABFunctions.getAllTileEntityPositionsNearbyEntity(TileEntityType.SIGN, 200, world, player);
				for (BlockPos signpos : signsaround) {
					TileEntity te = world.getBlockEntity(signpos);
					if (te instanceof SignTileEntity) {
						if (Util.hasZonePrefix((SignTileEntity)te)) {
							if (!sentfirst) {
								StringFunctions.sendMessage(player, "Area sign positions around you:", TextFormatting.DARK_GREEN);
								sentfirst = true;
							}
							String prefix = "a";
							if (Variables.areasperworld.get(world).containsKey(signpos)) {
								AreaObject ao = Variables.areasperworld.get(world).get(signpos);
								prefix = ao.areaname + " a";
							}
							
							double distance = Math.round(Math.sqrt(signpos.distSqr(pvec.x, pvec.y, pvec.z, true)) * 100.0) / 100.0;
							String blocksaway = " (" + distance + " blocks)";
							
							StringFunctions.sendMessage(player, " " + prefix + "t x=" + signpos.getX() + ", y=" + signpos.getY() + ", z=" + signpos.getZ() + "." + blocksaway, TextFormatting.YELLOW);
						}
					}
				}
				
				if (!sentfirst) {
					StringFunctions.sendMessage(player, "There are no area signs around you.", TextFormatting.DARK_GREEN);
				}
				
				return 1;
			})
		);
    }

}
