/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.15.
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

package com.natamus.collective_fabric.schematic;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.schematic.ParsedSchematicObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParseSchematicFile {
    public static ParsedSchematicObject getParsedSchematicObject(InputStream schematicInputStream, Level level, BlockPos centerPos, int extraYOffset, boolean skipAir) {
        Schematic schematic = new Schematic(schematicInputStream);

        int maxBuildHeight = level.getMaxBuildHeight();
        int length = schematic.getLength();
        int width = schematic.getWidth();
        int height = schematic.getHeight();

        int yoffset = centerPos.getY() + extraYOffset;
        if (yoffset + height > maxBuildHeight) {
            yoffset = maxBuildHeight - height;
        }

        List<Pair<BlockPos, BlockState>> blocks = new ArrayList<Pair<BlockPos, BlockState>>();
        for (SchematicBlockObject blockObject : schematic.getBlocks()) {
            BlockState blockState = blockObject.getState();
            if (skipAir && blockState.getBlock().equals(Blocks.AIR)) {
                continue;
            }

            blocks.add(new Pair<BlockPos, BlockState>(blockObject.getPosition().offset(centerPos.getX() - (width/2), yoffset, centerPos.getZ() - (length/2)).immutable(), blockState));
        }

        List<BlockPos> blockEntityPositions = new ArrayList<BlockPos>();
        for (CompoundTag blockEntityCompoundTag : schematic.getBlockEntities()) {
            blockEntityPositions.add(schematic.getBlockPosFromCompoundTag(blockEntityCompoundTag).offset(centerPos.getX() - (width/2), yoffset, centerPos.getZ() - (length/2)));
        }

        return new ParsedSchematicObject(blocks, blockEntityPositions, "Parsed successfully.", true);
    }
}
