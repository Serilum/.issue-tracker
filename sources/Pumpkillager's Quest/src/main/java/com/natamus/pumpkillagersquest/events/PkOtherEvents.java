/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.3.
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

package com.natamus.pumpkillagersquest.events;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.pumpkillagersquest.pumpkillager.Conversations;
import com.natamus.pumpkillagersquest.pumpkillager.Manage;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.QuestData;
import com.natamus.pumpkillagersquest.util.Reference;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class PkOtherEvents {
    @SubscribeEvent
    public void onTNTExplode(ExplosionEvent.Detonate e) {
        Level level = e.getLevel();
        if (level.isClientSide) {
            return;
        }

        Entity exploder = e.getExplosion().getExploder();
        if (!(exploder instanceof PrimedTnt)) {
            return;
        }

        BlockPos pos = exploder.blockPosition();
        if (Data.allPrisoners.get(level).size() > 0) {
            for (Villager prisoner : Data.allPrisoners.get(level)) {
                BlockPos prisonerPos = prisoner.blockPosition();
                if (BlockPosFunctions.withinDistance(pos, prisonerPos, 10)) {
                    e.getAffectedBlocks().clear();
                    e.getAffectedEntities().clear();
                }
            }
        }

        if (!(level.getBlockState(pos.north()).getBlock() instanceof RedStoneWireBlock)) {
            return;
        }

        List<Integer> redstonePositionsLeft = new ArrayList<Integer>(QuestData.questOneRitualBlockPositions);

        int i = 0;
        for (BlockPos bpa : BlockPos.betweenClosed(pos.getX()-3, pos.getY(), pos.getZ()-3, pos.getX()+3, pos.getY(), pos.getZ()+3)) {
            if (level.getBlockState(bpa).getBlock() instanceof RedStoneWireBlock) {
                redstonePositionsLeft.remove(Integer.valueOf(i));
            }
            i+=1;
        }

        if (redstonePositionsLeft.size() == 0) {
            e.getAffectedBlocks().clear();
            e.getAffectedEntities().clear();

            Player player = null;
            for (Entity ea : level.getEntities(null, new AABB(pos.getX()-20, pos.getY()-20, pos.getZ()-20, pos.getX()+20, pos.getY()+20, pos.getZ()+20))) {
                if (ea instanceof Player) {
                    player = (Player)ea;
                    break;
                }
            }

            if (player == null) {
                return;
            }

            if (player.getTags().contains(Reference.MOD_ID + ".unleashed")) {
                if (!player.getTags().contains(Reference.MOD_ID + ".completedquest")) {
                    Conversations.addEmptyMessage(level, null, player, 0);
                    Conversations.addMessageWithoutPrefix(level, null, player, "You feel a blast of magic, but nothing happens. The ritual must have been completed already. Maybe a prisoner in the prisoner camp could help find the Pumpkillager.", ChatFormatting.GRAY, 10);
                }
                return;
            }

            Manage.spawnPumpkillager(level, player, pos, 1, 1);
        }
    }

    @SubscribeEvent
    public void onPistonMove(PistonEvent.Pre e) {
        Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level == null) {
            return;
        }

        BlockPos pos = e.getFaceOffsetPos();
        for (Entity ea : level.getEntities(null, new AABB(pos.getX()-3, pos.getY()-3, pos.getZ()-3, pos.getX()+3, pos.getY()+3, pos.getZ()+3))) {
            if (Util.isPumpkillager(ea)) {
                e.setCanceled(true);
                return;
            }
        }
    }
}
