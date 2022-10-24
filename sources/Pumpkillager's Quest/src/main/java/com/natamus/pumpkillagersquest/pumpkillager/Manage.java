/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 2.0.
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
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.Reference;
import com.natamus.pumpkillagersquest.util.SpookyHeads;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class Manage {
    public static void spawnPumpkillager(Level level, Player player, BlockPos pos) {
        spawnPumpkillager(level, player, pos, 0, 0);
    }
    public static void spawnPumpkillager(Level level, Player player, BlockPos pos, int conversationId, int placementId) {
        if (!Data.allPumpkillagers.containsKey(level)) {
            Data.allPumpkillagers.put(level, new CopyOnWriteArrayList<Villager>());
        }

        Villager pumpkillager = createPumpkillager(level, pos , player, null, SpookyHeads.getJackoLantern(1), ChatFormatting.YELLOW, "1.0", false);

        if (conversationId == 0) {
            pumpkillager.getTags().add(Reference.MOD_ID + ".initialencounter");
        }

        level.addFreshEntity(pumpkillager);

        pumpkillager.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());

        placePumpkillagerBlocks(level, pos, placementId);
        Conversations.startTalking(level, pumpkillager, player, conversationId);
    }

    public static Villager createPumpkillager(Level level, BlockPos pos, Player player, VillagerProfession profession, ItemStack headStack, ChatFormatting nameColour, String scaleFloatString, boolean replace) {
        Villager oldpumpkillager = null;
        Set<String> oldtags = Set.of("");

        if (replace) {
            for (Entity ea : level.getEntities(null, new AABB(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1))) {
                if (Util.isPumpkillager(ea)) {
                    oldpumpkillager = (Villager)ea;
                    oldtags = oldpumpkillager.getTags();
                    break;
                }
            }
        }

        Villager pumpkillager = EntityType.VILLAGER.create(level);

        if (profession == null) {
            pumpkillager.setVillagerData(pumpkillager.getVillagerData().setType(VillagerType.SNOW));
        }
        else {
            pumpkillager.setVillagerData(pumpkillager.getVillagerData().setType(VillagerType.SNOW).setProfession(profession));
        }

        String pumpkillagerName = Data.pumpkillagerName;
        if (!scaleFloatString.equals("1.0")) {
            pumpkillagerName = Data.pumpkillagerName + "|" + scaleFloatString;
        }

        pumpkillager.setPos(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        pumpkillager.setItemSlot(EquipmentSlot.HEAD, headStack);
        pumpkillager.setCustomName(Component.literal(pumpkillagerName).withStyle(nameColour));
        pumpkillager.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE));
        pumpkillager.getBrain().removeAllBehaviors();
        pumpkillager.getTags().add(Reference.MOD_ID + ".justadded");

        EntityFunctions.forceSetHealth(pumpkillager, Data.pumpkillagerMaxHealth);

        for (String tag : oldtags) {
            pumpkillager.getTags().add(tag);
        }

        if (oldpumpkillager != null) {
            pumpkillager.setYHeadRot(oldpumpkillager.getYHeadRot());
            pumpkillager.setYBodyRot(oldpumpkillager.yBodyRot);
            pumpkillager.setXRot(oldpumpkillager.getXRot());
            pumpkillager.setYRot(oldpumpkillager.getYRot());
        }

        ItemStack scaleBarrier = new ItemStack(Items.BARRIER, 64);
        scaleBarrier.setHoverName(Component.literal(scaleFloatString));
        pumpkillager.setItemSlot(EquipmentSlot.FEET, scaleBarrier);

        Data.pumpkillagerPlayerTarget.put(pumpkillager, player);
        Data.allPumpkillagers.get(level).add(pumpkillager);
        Data.pumpkillagerPositions.put(pumpkillager, pumpkillager.position());

        if (oldpumpkillager != null) {
            Data.pumpkillagerPlayerTarget.remove(oldpumpkillager);
            Data.allPumpkillagers.get(level).remove(oldpumpkillager);
            Data.pumpkillagerPositions.remove(oldpumpkillager);

            oldpumpkillager.remove(RemovalReason.DISCARDED);
        }

        return pumpkillager;
    }

    public static void placePumpkillagerBlocks(Level level, BlockPos pos, int placementId) {
        List<BlockPos> processedPoss = new ArrayList<BlockPos>();
        List<Pair<BlockPos, BlockState>> currentStates = new ArrayList<Pair<BlockPos, BlockState>>();

        if (placementId == 0) {
            BlockPos below = pos.below().immutable();

            currentStates.add(new Pair<BlockPos, BlockState>(below, level.getBlockState(below)));
            level.setBlock(below, Blocks.CRYING_OBSIDIAN.defaultBlockState(), 3);
            processedPoss.add(below);

            for (BlockPos ap : Util.getSidePositions(below)) {
                currentStates.add(new Pair<BlockPos, BlockState>(ap, level.getBlockState(ap)));
                level.setBlock(ap, Blocks.OBSIDIAN.defaultBlockState(), 3);
                processedPoss.add(ap);
            }

            for (BlockPos ap : Util.getSidePositions(pos)) {
                currentStates.add(new Pair<BlockPos, BlockState>(ap, level.getBlockState(ap)));
                level.setBlock(ap, Blocks.BLACKSTONE_WALL.defaultBlockState(), 3);
                processedPoss.add(ap);
            }

            for (BlockPos ap : Util.getSidePositions(pos.above())) {
                currentStates.add(new Pair<BlockPos, BlockState>(ap.immutable(), level.getBlockState(ap)));
                level.setBlock(ap, Blocks.SOUL_LANTERN.defaultBlockState(), 3);
                processedPoss.add(ap);
            }
        }
        else if (placementId == 1) {
            BlockPos belowPos = pos.below().immutable();
            currentStates.add(new Pair<BlockPos, BlockState>(belowPos, level.getBlockState(belowPos)));
            level.setBlock(belowPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
            processedPoss.add(belowPos);
        }
        else {
            return;
        }

        Data.globalProcessedPoss.addAll(processedPoss);
        Data.previousStates.put(pos, currentStates);
    }

    public static void resetPlacedBlocks(Level level, Villager pumpkillager) {
        resetPlacedBlocks(level, pumpkillager.blockPosition());
    }
    public static void resetPlacedBlocks(Level level, BlockPos pos) {
        if (Data.previousStates.containsKey(pos)) {
            Collections.reverse(Data.previousStates.get(pos));
            for (Pair<BlockPos, BlockState> replacePair : Data.previousStates.get(pos)) {
                try {
                    level.setBlock(replacePair.getFirst(), replacePair.getSecond(), 3);
                }
                catch (NullPointerException ignored) { }
                Data.globalProcessedPoss.remove(pos);
            }
            Data.previousStates.remove(pos);
        }
    }

    public static void yeetLivingEntityIntoSky(Level level, LivingEntity livingEntity) {
        BlockPos pos = livingEntity.blockPosition();

        livingEntity.getTags().add(Reference.MOD_ID + ".removed");
        livingEntity.setNoGravity(true);
        Data.entitiesToYeet.get(level).add(livingEntity);
    }

    public static void pumpkillagerMovedWrongly(Level level, Entity pumpkillager, Player player) {
        pumpkillagerMovedWrongly(level, (Villager)pumpkillager, player);
    }
    public static void pumpkillagerMovedWrongly(Level level, Villager pumpkillager, Player player) {
        if (!Data.pumpkillagerPositions.containsKey(pumpkillager)) {
            return;
        }

        if (!pumpkillager.getTags().contains(Reference.MOD_ID + ".isleaving")) {
            Vec3 pumpkillagerVec = Data.pumpkillagerPositions.get(pumpkillager);
            pumpkillager.setPos(pumpkillagerVec);
        }
    }

    public static void initiateCharacterLeave(Level level, Villager character) {
        character.getTags().add(Reference.MOD_ID + ".isleaving");

        if (!level.isClientSide) {
            ServerBossEvent serverBossEvent = Data.pumpkillagerBossEvents.get(character);
            if (serverBossEvent != null) {
                serverBossEvent.setProgress(0);
                serverBossEvent.setVisible(false);
                serverBossEvent.removeAllPlayers();

                Data.pumpkillagerBossEvents.remove(character);
            }
        }

        if (Util.isPumpkillager(character)) {
            Manage.resetPlacedBlocks(level, character);
        }
        Manage.yeetLivingEntityIntoSky(level, character);
    }
}
