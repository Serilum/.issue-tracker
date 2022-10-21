/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.8.
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

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.Reference;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Summon {
    public static void summonFinalBossMinions(Level level, Villager pumpkillager, Player targetPlayer, int summonId) {
        if (level.isClientSide) {
            return;
        }

        if (pumpkillager.getTags().contains(Reference.MOD_ID + ".preventactions")) {
            return;
        }

        switch (summonId) {
            case 0 -> summonFirstWave(level, pumpkillager, targetPlayer);
            case 1 -> summonSecondWave(level, pumpkillager, targetPlayer);
            case 2 -> summonThirdWave(level, pumpkillager, targetPlayer);
            case 3 -> summonFourthWave(level, pumpkillager, targetPlayer);
        }
    }

    public static void checkForNewSummon(Level level, Villager pumpkillager, Player player, float newHealth) {
        float maxHealth = Data.pumpkillagerMaxHealth;
        float healthPercentage = newHealth/maxHealth;

        Set<String> tags = pumpkillager.getTags();
        if (healthPercentage <= 0.75 && !tags.contains(Reference.MOD_ID + ".secondwave")) {
            summonFinalBossMinions(level, pumpkillager, player, 1);
            pumpkillager.getTags().add(Reference.MOD_ID + ".secondwave");
            return;
        }

        if (healthPercentage <= 0.50 && !tags.contains(Reference.MOD_ID + ".thirdwave")) {
            summonFinalBossMinions(level, pumpkillager, player, 2);
            pumpkillager.getTags().add(Reference.MOD_ID + ".thirdwave");
            return;
        }

        if (healthPercentage <= 0.25 && !tags.contains(Reference.MOD_ID + ".fourthwave")) {
            summonFinalBossMinions(level, pumpkillager, player, 3);
            pumpkillager.getTags().add(Reference.MOD_ID + ".fourthwave");
        }
    }

    public static void summonMinionsAround(Level level, Villager pumpkillager, Player player, BlockPos centerPos, List<LivingEntity> minionsToSpawn) {
        if (pumpkillager.getTags().contains(Reference.MOD_ID + ".iskilled")) {
            return;
        }

        List<BlockPos> positions = new ArrayList<BlockPos>(Arrays.asList(centerPos.north(4).immutable(), centerPos.east(4).immutable(), centerPos.south(4).immutable(), centerPos.west(4).immutable()));

        ServerLevel serverLevel = (ServerLevel)level;
        MinecraftServer minecraftServer = serverLevel.getServer();

        int i = 0;
        for (LivingEntity livingEntity : minionsToSpawn) {
            livingEntity.getTags().add(Reference.MOD_ID + ".summoned");
            livingEntity.getTags().add(Reference.MOD_ID + ".justadded");

            BlockPos rawSpawnPos = positions.get(i);
            BlockPos spawnPos = BlockPosFunctions.getSurfaceBlockPos(serverLevel, rawSpawnPos.getX(), rawSpawnPos.getZ());

            livingEntity.setPos(spawnPos.getX()+0.5, spawnPos.getY(), spawnPos.getZ()+0.5);

            if (livingEntity instanceof Mob) {
                ((Mob)livingEntity).setCanPickUpLoot(false);
            }

            level.addFreshEntity(livingEntity);

            Util.spawnLightning(level, spawnPos, livingEntity, player, true);

            i+=1;
        }
    }

    public static void summonFirstWave(Level level, Villager pumpkillager, Player player) {
        BlockPos pos = pumpkillager.blockPosition().immutable();

        ItemStack swordStack = new ItemStack(Items.IRON_SWORD, 1);
        swordStack.enchant(Enchantments.VANISHING_CURSE, 1);

        List<LivingEntity> minionsToSummon = new ArrayList<LivingEntity>();
        for (int i = 0; i < 4; i++) {
            ZombieVillager zombieVillager = EntityType.ZOMBIE_VILLAGER.create(level);
            zombieVillager.setItemInHand(InteractionHand.MAIN_HAND, swordStack.copy());

            zombieVillager.targetSelector.removeAllGoals();
            zombieVillager.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(zombieVillager, Player.class, false));

            minionsToSummon.add(zombieVillager);
        }

        summonMinionsAround(level, pumpkillager, player, pos, minionsToSummon);

        Conversations.addEmptyMessage(level, pumpkillager, player, 0);
        Conversations.addMessage(level, pumpkillager, player, "My zombified followers, forwards!", ChatFormatting.RED, 0);

        pumpkillager.getTags().add(Reference.MOD_ID + ".summoninglightning");
        Actions.pumpkillagerLightning(level, pumpkillager, player);
    }

    public static void summonSecondWave(Level level, Villager pumpkillager, Player player) {
        BlockPos pos = pumpkillager.blockPosition().immutable();

        ItemStack bowStack = new ItemStack(Items.BOW, 1);
        bowStack.enchant(Enchantments.VANISHING_CURSE, 1);

        List<LivingEntity> minionsToSummon = new ArrayList<LivingEntity>();
        for (int i = 0; i < 4; i++) {
            Skeleton skeleton = EntityType.SKELETON.create(level);
            skeleton.setItemInHand(InteractionHand.MAIN_HAND, bowStack.copy());

            skeleton.targetSelector.removeAllGoals();
            skeleton.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(skeleton, Player.class, false));

            minionsToSummon.add(skeleton);
        }

        summonMinionsAround(level, pumpkillager, player, pos, minionsToSummon);

        Conversations.addEmptyMessage(level, pumpkillager, player, 0);
        Conversations.addMessage(level, pumpkillager, player, "Attack, my skeletons!", ChatFormatting.RED, 0);
    }

    public static void summonThirdWave(Level level, Villager pumpkillager, Player player) {
        BlockPos pos = pumpkillager.blockPosition().immutable();

        List<LivingEntity> minionsToSummon = new ArrayList<LivingEntity>();
        for (int i = 0; i < 2; i++) {
            minionsToSummon.add(EntityType.WITCH.create(level));
            minionsToSummon.add(EntityType.PHANTOM.create(level));
        }

        summonMinionsAround(level, pumpkillager, player, pos, minionsToSummon);

        Conversations.addEmptyMessage(level, pumpkillager, player, 0);
        Conversations.addMessage(level, pumpkillager, player, "My loyal witches and your pets, destroy the human!", ChatFormatting.RED, 0);
    }


    public static void summonFourthWave(Level level, Villager pumpkillager, Player player) {
        BlockPos pos = pumpkillager.blockPosition().immutable();

        ItemStack axeStack = new ItemStack(Items.WOODEN_AXE, 1);
        axeStack.enchant(Enchantments.VANISHING_CURSE, 1);

        ItemStack crossbowStack = new ItemStack(Items.CROSSBOW, 1);
        crossbowStack.enchant(Enchantments.VANISHING_CURSE, 1);

        List<LivingEntity> minionsToSummon = new ArrayList<LivingEntity>();
        for (int i = 0; i < 2; i++) {
            Vindicator vindicator = EntityType.VINDICATOR.create(level);
            Pillager pillager = EntityType.PILLAGER.create(level);

            vindicator.setItemInHand(InteractionHand.MAIN_HAND, axeStack.copy());
            pillager.setItemInHand(InteractionHand.MAIN_HAND, crossbowStack.copy());

            vindicator.targetSelector.removeAllGoals();
            pillager.targetSelector.removeAllGoals();

            vindicator.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(vindicator, Player.class, false));
            pillager.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(pillager, Player.class, true));

            minionsToSummon.add(vindicator);
            minionsToSummon.add(pillager);
        }

        summonMinionsAround(level, pumpkillager, player, pos, minionsToSummon);

        Conversations.addEmptyMessage(level, pumpkillager, player, 0);
        Conversations.addMessage(level, pumpkillager, player, "Illagers! Finish that scum!", ChatFormatting.RED, 0);
    }
}
