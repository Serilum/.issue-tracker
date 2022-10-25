/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 2.1.
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
import com.natamus.pumpkillagersquest.api.PumpkillagerSummonEvent;
import com.natamus.pumpkillagersquest.config.ConfigHandler;
import com.natamus.pumpkillagersquest.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Actions {
    public static void sendDistanceMessage(Level level, Villager pumpkillager, Player player) {
        Set<String> pumpkillagerTags = pumpkillager.getTags();
        if (pumpkillagerTags.contains(Reference.MOD_ID + ".isleaving")) {
            return;
        }

        if (pumpkillagerTags.contains(Reference.MOD_ID + ".initialencounter")) {
            Conversations.addEmptyMessage(level, pumpkillager, player, 0);
            Conversations.addMessage(level, pumpkillager, player, "Fine. Just leave me here.", ChatFormatting.WHITE, 0);
            Conversations.addMessage(level, pumpkillager, player, "Goodbye, " + player.getName().getString() + ".", ChatFormatting.WHITE, 0);
        }
        else if (pumpkillagerTags.contains(Reference.MOD_ID + ".finalform")) {
            Conversations.addEmptyMessage(level, pumpkillager, player, 0);
            Conversations.addMessage(level, pumpkillager, player, "There's no running from my new world order, " + player.getName().getString() + ".", ChatFormatting.WHITE, 0);
            Conversations.addMessage(level, pumpkillager, player, "I will find you again. Goodbye.", ChatFormatting.WHITE, 0);
        }
    }

    public static void givePlayerQuestbook(Level level, Villager pumpkillager, Player player) {
        if (!(pumpkillager.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof WrittenBookItem) || pumpkillager.getTags().contains(Reference.MOD_ID + ".questbookgiven")) {
            return;
        }
        pumpkillager.getTags().add(Reference.MOD_ID + ".questbookgiven");
        player.getTags().add(Reference.MOD_ID + ".questbookgiven");

        pumpkillager.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        Conversations.addEmptyMessage(level, pumpkillager, player, 50);
        Conversations.addMessage(level, pumpkillager, player, "Thank you for accepting my quest. Take this book, it will give you more information.", ChatFormatting.WHITE, 60, Data.getQuestbook(), "give");

        Conversations.addEmptyMessage(level, pumpkillager, player, 2000);
        if (!player.getTags().contains(Reference.MOD_ID + ".wasgiventnt")) {
            Conversations.addMessage(level, pumpkillager, player, "--Before I leave, take this. It might come in handy.", ChatFormatting.WHITE, 2010, new ItemStack(Items.TNT, 1), "give");
            Conversations.addEmptyMessage(level, pumpkillager, player, 4000);
            player.getTags().add(Reference.MOD_ID + ".wasgiventnt");
        }
        else {
            Conversations.addMessage(level, pumpkillager, player, "I've already given you some tnt.", ChatFormatting.WHITE, 2010);
        }

        Conversations.addMessage(level, pumpkillager, player, "Bye!", ChatFormatting.WHITE, 4010);

        Scheduler.scheduleCharacterLeave(level, pumpkillager, 5000);
    }

    public static ItemStack generatePrisonAndCoordinatePaper(Level level, Villager pumpkillager, Player player) {
        String coordinateString = "";

        BlockPos prisonerCampCoordinates = Util.getPrisonerCampCoordinates(level, pumpkillager, player);
        if (prisonerCampCoordinates != null) {
            coordinateString = prisonerCampCoordinates.getX() + ", " + prisonerCampCoordinates.getY() + ", " + prisonerCampCoordinates.getZ();
        }

        ItemStack paperStack = new ItemStack(Items.PAPER, 1);
        paperStack.setHoverName(Component.translatable(QuestData.questOneCoordinatePaperPrefix + coordinateString + "."));

        CompletableFuture.runAsync(() -> {
            GenerateStructure.generatePrisonerCamp(level, player, prisonerCampCoordinates, 40);
        });

        return paperStack;
    }

    public static void makePrisonerGuardsStepOffHorse(Level level, Villager prisoner, Player targetPlayer) {
        for (Entity entityAround : level.getEntities(null, new AABB(prisoner.getX()-30, prisoner.getY()-30, prisoner.getZ()-30, prisoner.getX()+30, prisoner.getY()+30, prisoner.getZ()+30))) {
            if (entityAround instanceof SkeletonHorse) {
                SkeletonHorse skeletonHorse = (SkeletonHorse)entityAround;
                skeletonHorse.unRide();
                break;
            }
        }
    }

    public static void processPrisonerItemGeneration(Level level, Villager prisoner, Player player, int msDelay) {
        new Thread(() -> {
            try  { Thread.sleep( msDelay ); }
            catch (InterruptedException ignored)  {}

            Vec3 pvec = prisoner.position();

            level.explode(null, new DamageSource("explosion").setExplosion(), null, pvec.x, pvec.y, pvec.z, 3.0f, false, Explosion.BlockInteraction.NONE);

            Conversations.startTalking(level, prisoner, player, 6);
        }).start();
    }

    public static void startFinalBossSequence(Level level, Player player, BlockPos centerPos, List<Pair<BlockPos, BlockState>> candlePositions) {
        Vec3 centerVec = new Vec3(centerPos.getX() + 0.5, centerPos.getY(), centerPos.getZ()+0.5);

        level.explode(null, new DamageSource("explosion").setExplosion(), null, centerVec.x, centerVec.y, centerVec.z, 3.0f, false, Explosion.BlockInteraction.NONE);
        level.setBlock(centerPos, Blocks.AIR.defaultBlockState(), 3);

        for (Pair<BlockPos, BlockState> candlePair : candlePositions) {
            int i = 0;
            //level.setBlock(candlePair.getFirst(), candlePair.getSecond().setValue(CandleBlock.LIT, false), 3);
        }

        Villager pumpkillager = Manage.createPumpkillager(level, centerPos, player, VillagerProfession.WEAPONSMITH, SpookyHeads.getEvilJackoLantern(1), Data.defaultPumpkillagerColour, "3.0", false);
        pumpkillager.getTags().add(Reference.MOD_ID + ".nodamage");
        pumpkillager.getTags().add(Reference.MOD_ID + ".finalform");

        level.setBlock(centerPos.below(), Blocks.OBSIDIAN.defaultBlockState(), 3);
        level.addFreshEntity(pumpkillager);

        MinecraftForge.EVENT_BUS.post(new PumpkillagerSummonEvent(player, pumpkillager, centerPos, PumpkillagerSummonEvent.Type.FINAL_BOSS));

        Conversations.startTalking(level, pumpkillager, player, 7);
    }

    public static void pumpkillagerLightning(Level level, Villager pumpkillager, Player targetPlayer) {
        new Thread(() -> {
            try  { Thread.sleep( 5000 ); }
            catch (InterruptedException ignored) {}

            if (pumpkillager.isRemoved() || !Data.allPumpkillagers.get(level).contains(pumpkillager)) {
                return;
            }

            if (!pumpkillager.getTags().contains(Reference.MOD_ID + ".summoninglightning")) {
                return;
            }

            level.getServer().execute(() -> {
                BlockPos playerPos = targetPlayer.blockPosition();

                new Thread(() -> {
                    try { Thread.sleep(1000); }
                    catch (InterruptedException ignored) {}

                    Data.lightningTasks.get(level).put(pumpkillager, () -> {
                        if (!pumpkillager.getTags().contains(Reference.MOD_ID + ".firstlightning")) {
                            Conversations.sendJaxMessage(level, targetPlayer, targetPlayer.getName().getString() + "! The Pumpkillager is summoning lightning at your position! It looks like it's delayed, so just keep moving!", ChatFormatting.WHITE, 10);
                            pumpkillager.getTags().add(Reference.MOD_ID + ".firstlightning");
                        }

                        Util.spawnLightning(level, playerPos, targetPlayer, targetPlayer, false);
                    });
                }).start();
            });

            pumpkillagerLightning(level, pumpkillager, targetPlayer);
        }).start();
    }

    public static void startWeakenedBossEvent(Level level, Villager pumpkillager, Player player) {
        Conversations.addEmptyMessage(level, null, player, 1500);
        Conversations.sendJaxMessage(level, player, "You did it! Now finish him!", ChatFormatting.GRAY, 1510);

        ServerBossEvent serverBossEvent = new ServerBossEvent(Component.translatable(Data.pumpkillagerName), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
        Data.pumpkillagerBossEvents.put(pumpkillager, serverBossEvent);

        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            serverBossEvent.addPlayer(serverPlayer);
        }

        Conversations.startTalking(level, pumpkillager, player, 8);
    }

    public static void shrinkAndKillPumpkillager(Level level, Villager pumpkillager, Player targetPlayer) {
        pumpkillager.getTags().remove(Reference.MOD_ID + ".summoninglightning");
        pumpkillager.getTags().add(Reference.MOD_ID + ".iskilled");

        if (!level.isClientSide) {
            ServerBossEvent serverBossEvent = Data.pumpkillagerBossEvents.get(pumpkillager);
            if (serverBossEvent != null) {
                serverBossEvent.setProgress(0);
                serverBossEvent.setVisible(false);
                serverBossEvent.removeAllPlayers();

                Data.pumpkillagerBossEvents.remove(pumpkillager);
            }
        }

        ItemStack feetStack = pumpkillager.getItemBySlot(EquipmentSlot.FEET);
        if (feetStack.getItem().equals(Items.BARRIER)) {
            feetStack.setCount(10);
        }

        if (targetPlayer != null) {
            targetPlayer.getTags().remove(Reference.MOD_ID + ".aimforfeet");

            Conversations.addPostDeathMessage(level, pumpkillager, targetPlayer, "", ChatFormatting.WHITE);
            Conversations.addPostDeathMessage(level, pumpkillager, targetPlayer, "I can't believe you've defeated me.. Goodbye, cruel world.", ChatFormatting.WHITE);
        }
    }

    public static void turnPumpkillagerIntoHead(Level level, Villager pumpkillager, Player targetPlayer) {
        BlockPos headPos = pumpkillager.blockPosition().immutable();

        float floatRotation = pumpkillager.getYHeadRot() + 180F;
        if (floatRotation > 360F) {
            floatRotation -= 360F;
        }

        PlayerHeadBlock playerHeadBlock = (PlayerHeadBlock)Blocks.PLAYER_HEAD;

        int intRotation = Mth.floor((double) (floatRotation * 16.0F / 360.0F) + 0.5D) & 15;
        BlockState playerHeadBlockState = playerHeadBlock.defaultBlockState().setValue(SkullBlock.ROTATION, intRotation);

        level.setBlock(headPos, playerHeadBlockState, 3);
        playerHeadBlock.setPlacedBy(level, headPos, playerHeadBlockState, null, SpookyHeads.getEvilJackoLantern(1));

        level.playSound(null, pumpkillager.getX(), pumpkillager.getY(), pumpkillager.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.AMBIENT, 2.0F, 1.0F);
        pumpkillager.remove(RemovalReason.KILLED);

        new Thread(() -> {
            try  { Thread.sleep( 500 ); }
            catch (InterruptedException ignored)  {}

            level.getServer().execute(() -> {
                spawnPostFinalBossPrisoner(level, headPos, targetPlayer);
            });
        }).start();
    }

    public static void spawnPostFinalBossPrisoner(Level level, BlockPos pumpkillagerPos, Player targetPlayer) {
        BlockPos prisonerPos = pumpkillagerPos.above(2).immutable();
        Vec3 prisonerVec = new Vec3(prisonerPos.getX()+0.5, prisonerPos.getY(), prisonerPos.getZ()+0.5);

        Villager prisoner = Prisoner.createPrisoner(level, prisonerPos, targetPlayer, VillagerProfession.NITWIT, SpookyHeads.getCarvedPumpkin(1), ChatFormatting.GOLD, true);

        prisoner.getBrain().removeAllBehaviors();
        prisoner.setNoGravity(true);

        prisoner.getTags().add(Reference.MOD_ID + ".lookingatplayer");
        prisoner.getTags().add(Reference.MOD_ID + ".afterfinal");
        prisoner.getTags().add(Reference.MOD_ID + ".isknownto." + targetPlayer.getName().getString());
        targetPlayer.getTags().add(Reference.MOD_ID + ".completedquest");

        level.addFreshEntity(prisoner);

        level.explode(null, new DamageSource("explosion").setExplosion(), null, prisonerVec.x, prisonerVec.y, prisonerVec.z, 3.0f, false, Explosion.BlockInteraction.NONE);

        if (!level.isClientSide) {
            ExperienceOrb.award((ServerLevel)level, new Vec3(pumpkillagerPos.getX()+0.5, pumpkillagerPos.getY(), pumpkillagerPos.getZ()+0.5), ConfigHandler.GENERAL.experienceAmountRewardFinalBoss.get());
        }

        Conversations.startTalking(level, prisoner, targetPlayer, 9);
    }
}
