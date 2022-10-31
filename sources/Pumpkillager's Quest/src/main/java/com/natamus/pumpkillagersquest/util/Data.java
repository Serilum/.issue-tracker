/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 2.2.
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

package com.natamus.pumpkillagersquest.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Data {
    public static WeakHashMap<Level, CopyOnWriteArrayList<Villager>> allPumpkillagers = new WeakHashMap<Level, CopyOnWriteArrayList<Villager>>();
    public static WeakHashMap<Level, CopyOnWriteArrayList<Villager>> allPrisoners = new WeakHashMap<Level, CopyOnWriteArrayList<Villager>>();
    public static HashMap<Villager, Player> pumpkillagerPlayerTarget = new HashMap<Villager, Player>();
    public static WeakHashMap<Villager, Vec3> pumpkillagerPositions = new WeakHashMap<Villager, Vec3>();
    public static WeakHashMap<Villager, Vec3> prisonerPositions = new WeakHashMap<Villager, Vec3>();
    public static WeakHashMap<Villager, ServerBossEvent> pumpkillagerBossEvents = new WeakHashMap<Villager, ServerBossEvent>();
    public static WeakHashMap<Level, List<Pair<Player, MutableComponent>>> messagesToSend = new WeakHashMap<Level, List<Pair<Player, MutableComponent>>>();
    public static WeakHashMap<Level, CopyOnWriteArrayList<LivingEntity>> entitiesToYeet = new WeakHashMap<Level, CopyOnWriteArrayList<LivingEntity>>();
    public static WeakHashMap<Level, HashMap<Villager, Runnable>> lightningTasks = new WeakHashMap<Level, HashMap<Villager, Runnable>>();
    public static WeakHashMap<Level, List<LivingEntity>> spawnPumpkin = new WeakHashMap<Level, List<LivingEntity>>();

    public static HashMap<BlockPos, List<Pair<BlockPos, BlockState>>> previousStates = new HashMap<BlockPos, List<Pair<BlockPos, BlockState>>>();
    public static List<BlockPos> globalProcessedPoss = new ArrayList<BlockPos>();

    public static final TagKey<Block> pumpkinTag = BlockTags.create(new ResourceLocation("forge", "crops/pumpkin"));
    public static float pumpkillagerMaxHealth = 300F;
    public static final String pumpkillagerName = "The Pumpkillager";
    public static final String prisonerNameUnknown = "Prisoner";
    public static final String prisonerNameKnown = "Jax o'Saturn";
    public static final String questBookName = "Pumpkillager's Quest";
    public static final String stopPkBookName = "Stopping the Pumpkillager";
    public static final ChatFormatting defaultPumpkillagerColour = ChatFormatting.RED;
    public static final ChatFormatting defaultPrisonerColour = ChatFormatting.GOLD;

    public static MutableComponent addCharacterPrefix(Villager character, Player targetPlayer, MutableComponent component) {
        if (Util.isPumpkillager(character)) {
            return addCharacterPrefix("pumpkillager", targetPlayer, component);
        }
        else if (Util.isPrisoner(character)) {
            if (Util.prisonerIsKnown(character, targetPlayer)) {
                return addCharacterPrefix("knownprisoner", targetPlayer, component);
            }
            return addCharacterPrefix("unknownprisoner", targetPlayer, component);
        }
        return component;
    }
    public static MutableComponent addCharacterPrefix(String characterString, Player targetPlayer, MutableComponent component) {
        MutableComponent messageComponent = component;
        if (characterString.equals("pumpkillager")) {
            messageComponent = Component.literal("<").withStyle(ChatFormatting.WHITE).append(Component.literal(pumpkillagerName).withStyle(defaultPumpkillagerColour)).append(Component.literal(("> ")).withStyle(ChatFormatting.WHITE)).append(component);
        }
        else if (characterString.contains("prisoner")) {
            String prisonerName = prisonerNameUnknown;
            if (characterString.equals("knownprisoner")) {
                prisonerName = prisonerNameKnown;
            }
            messageComponent = Component.literal("<").withStyle(ChatFormatting.WHITE).append(Component.literal(prisonerName).withStyle(defaultPrisonerColour)).append(Component.literal(("> ")).withStyle(ChatFormatting.WHITE)).append(component);
        }
        else if (characterString.contains("Ghost")) {
            messageComponent = Component.literal("<").withStyle(ChatFormatting.WHITE).append(Component.literal(characterString).withStyle(ChatFormatting.RED)).append(Component.literal(("> ")).withStyle(ChatFormatting.WHITE)).append(component);
        }
        return messageComponent;
    }

    public static ItemStack getQuestbook() {
        String nbtstring = QuestData.questbookOneNbtString;

        try {
            CompoundTag newnbt = TagParser.parseTag(nbtstring);
            ItemStack questBook = ItemStack.of(newnbt);
            questBook.setHoverName(Component.translatable(questBookName).withStyle(ChatFormatting.YELLOW));

            return questBook;
        } catch (CommandSyntaxException ex) {
            System.out.println("Unable to get questbook.");
            ex.printStackTrace();
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getStopPkbook() {
        String nbtstring = QuestData.questOneStoppingPumpkillagerBookNbtString;

        try {
            CompoundTag newnbt = TagParser.parseTag(nbtstring);
            ItemStack questBook = ItemStack.of(newnbt);
            questBook.setHoverName(Component.translatable(stopPkBookName).withStyle(ChatFormatting.YELLOW));

            return questBook;
        } catch (CommandSyntaxException ex) {
            System.out.println("Unable to get stopping the pumpkillager book.");
            ex.printStackTrace();
        }

        return ItemStack.EMPTY;
    }
}
