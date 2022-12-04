/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.16.
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

package com.natamus.collective.schematic;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ParsedSchematicObject {
    public List<Pair<BlockPos, BlockState>> blocks;
    public List<BlockPos> blockEntityPositions;
    public String parseMessageString;
    public boolean parsedCorrectly;

    public ParsedSchematicObject(List<Pair<BlockPos, BlockState>> b, List<BlockPos> bEP, String pMS, boolean pC) {
        blocks = b;
        blockEntityPositions = bEP;
        parseMessageString = pMS;
        parsedCorrectly = pC;
    }

    public List<Pair<BlockPos, BlockEntity>> getBlockEntities(Level level) {
        List<Pair<BlockPos, BlockEntity>> blockEntities = new ArrayList<Pair<BlockPos, BlockEntity>>();
        for (BlockPos pos : blockEntityPositions) {
            blockEntities.add(new Pair<BlockPos, BlockEntity>(pos, level.getBlockEntity(pos)));
        }
        return blockEntities;
    }
}