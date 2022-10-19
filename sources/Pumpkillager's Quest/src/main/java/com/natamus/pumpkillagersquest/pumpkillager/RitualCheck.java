/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.1.
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

package com.natamus.pumpkillagersquest.pumpkillager;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.pumpkillagersquest.util.QuestData;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class RitualCheck {
    public static void checkRitualFinalSummoning(Level level, Player player, BlockPos pumpkinPos, BlockPos ignorePos) {
        List<Pair<BlockPos, BlockState>> candlePositions = new ArrayList<Pair<BlockPos, BlockState>>();
        List<Integer> candlePositionsLeft = new ArrayList<Integer>(QuestData.questOneRitualBlockPositions);

        int i = 0;
        for (BlockPos bpa : BlockPos.betweenClosed(pumpkinPos.getX() - 3, pumpkinPos.getY(), pumpkinPos.getZ() - 3, pumpkinPos.getX() + 3, pumpkinPos.getY(), pumpkinPos.getZ() + 3)) {
            BlockState bpaState = level.getBlockState(bpa);
            if (bpaState.getBlock() instanceof CandleBlock) {
                boolean addPosition = bpaState.getValue(CandleBlock.LIT);
                if (!addPosition && ignorePos != null) {
                    if (bpa.equals(ignorePos)) {
                        addPosition = true;
                    }
                }

                if (addPosition) {
                    candlePositionsLeft.remove(Integer.valueOf(i));
                    candlePositions.add(new Pair<BlockPos, BlockState>(bpa.immutable(), bpaState));
                }
            }
            i += 1;
        }

        if (candlePositionsLeft.size() == 0) {
            BlockPos centerPos = pumpkinPos.immutable();
            if (Util.isPumpkinBlock(level.getBlockState(centerPos).getBlock())) {
                boolean shouldStartSequence = level.isNight();

                if (shouldStartSequence) {
                    level.getServer().execute(() -> {
                        Actions.startFinalBossSequence(level, player, centerPos, candlePositions);
                    });
                } else {
                    BlockFunctions.dropBlock(level, centerPos);
                    Conversations.sendJaxMessage(level, player, "Remember, " + player.getName().getString() + ". This ritual can only be completed at night. The magic required is not strong enough during the day.", ChatFormatting.WHITE, 10);
                }
            }
        }
    }
}
