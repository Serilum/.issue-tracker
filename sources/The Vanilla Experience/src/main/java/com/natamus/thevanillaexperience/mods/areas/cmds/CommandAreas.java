/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.areas.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.thevanillaexperience.mods.areas.objects.AreaObject;
import com.natamus.thevanillaexperience.mods.areas.objects.AreasVariables;
import com.natamus.thevanillaexperience.mods.areas.util.AreasUtil;
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
				
				PlayerEntity player = source.asPlayer();
				World world = player.getEntityWorld();
				
				Vector3d pvec = player.getPositionVec();
				boolean sentfirst = false;
				
				List<BlockPos> signsaround = FABFunctions.getAllTileEntityPositionsNearbyEntity(TileEntityType.SIGN, 200, world, player);
				for (BlockPos signpos : signsaround) {
					TileEntity te = world.getTileEntity(signpos);
					if (te instanceof SignTileEntity) {
						if (AreasUtil.hasZonePrefix((SignTileEntity)te)) {
							if (!sentfirst) {
								StringFunctions.sendMessage(player, "Area sign positions around you:", TextFormatting.DARK_GREEN);
								sentfirst = true;
							}
							String prefix = "a";
							if (AreasVariables.areasperworld.get(world).containsKey(signpos)) {
								AreaObject ao = AreasVariables.areasperworld.get(world).get(signpos);
								prefix = ao.areaname + " a";
							}
							
							double distance = Math.round(Math.sqrt(signpos.distanceSq(pvec.x, pvec.y, pvec.z, true)) * 100.0) / 100.0;
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
