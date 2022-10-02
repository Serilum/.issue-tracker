/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.2.
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

package com.natamus.areas.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.areas.objects.AreaObject;
import com.natamus.areas.objects.Variables;
import com.natamus.areas.util.Util;
import com.natamus.collective_fabric.functions.FABFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

public class CommandAreas {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("areas")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				Player player = source.getPlayerOrException();
				Level world = player.getCommandSenderWorld();
				
				Vec3 pvec = player.position();
				boolean sentfirst = false;
				
				List<BlockPos> signsaround = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.SIGN, 200, world, player);
				for (BlockPos signpos : signsaround) {
					BlockEntity te = world.getBlockEntity(signpos);
					if (te instanceof SignBlockEntity) {
						if (Util.hasZonePrefix((SignBlockEntity)te)) {
							if (!sentfirst) {
								StringFunctions.sendMessage(player, "Area sign positions around you:", ChatFormatting.DARK_GREEN);
								sentfirst = true;
							}
							String prefix = "a";
							if (Variables.areasperworld.get(world).containsKey(signpos)) {
								AreaObject ao = Variables.areasperworld.get(world).get(signpos);
								prefix = ao.areaname + " a";
							}
							
							double distance = Math.round(Math.sqrt(signpos.distSqr(new Vec3i(pvec.x, pvec.y, pvec.z))) * 100.0) / 100.0;
							String blocksaway = " (" + distance + " blocks)";
							
							StringFunctions.sendMessage(player, " " + prefix + "t x=" + signpos.getX() + ", y=" + signpos.getY() + ", z=" + signpos.getZ() + "." + blocksaway, ChatFormatting.YELLOW);
						}
					}
				}
				
				if (!sentfirst) {
					StringFunctions.sendMessage(player, "There are no area signs around you.", ChatFormatting.DARK_GREEN);
				}
				
				return 1;
			})
		);
    }

}
