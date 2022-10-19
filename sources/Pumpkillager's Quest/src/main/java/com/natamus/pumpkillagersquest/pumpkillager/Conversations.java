/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.2.
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
import com.natamus.pumpkillagersquest.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Conversations {
    public static void startTalking(Level level, Villager character, Player targetPlayer, int conversationId) {
        switch (conversationId) {
            case 0 -> startInitialConversation(level, character, targetPlayer);
            case 1 -> startPostRitualConversation(level, character, targetPlayer);
            case 2 -> startPostAncientFormConversation(level, character, targetPlayer);
            case 3 -> startInitialPrisonerShout(level, character, targetPlayer);
            case 4 -> startPostPrisonerShout(level, character, targetPlayer);
            case 5 -> startGettingToKnowPrisoner(level, character, targetPlayer);
            case 6 -> startPrisonerGenerationConversation(level, character, targetPlayer);
            case 7 -> startInitialFinalBossConversation(level, character, targetPlayer);
            case 8 -> bossJustGotWeakenedConversation(level, character, targetPlayer);
            case 9 -> postFinalPrisonerConversation(level, character, targetPlayer);
        }
    }

    public static void startInitialConversation(Level level, Villager pumpkillager, Player targetPlayer) { // 0
        addEmptyMessage(level, pumpkillager, targetPlayer, 0);
        addMessage(level, pumpkillager, targetPlayer, "Hello " + targetPlayer.getName().getString() + ".", ChatFormatting.WHITE, 0);
        addMessage(level, pumpkillager, targetPlayer, "I am the Pumpkillager.", ChatFormatting.WHITE, 2000);
        addMessage(level, pumpkillager, targetPlayer, "I have a quest for you.", ChatFormatting.WHITE, 4000, new ItemStack(Items.WRITTEN_BOOK, 1), "hold");
        addMessage(level, pumpkillager, targetPlayer, "Right-click the book to accept.", ChatFormatting.GRAY, 5000);
    }

    public static void startPostRitualConversation(Level level, Villager pumpkillager, Player targetPlayer) { // 1
        pumpkillager.getTags().add(Reference.MOD_ID + ".nodamage");

        addEmptyMessage(level, pumpkillager, targetPlayer, 500);
        addMessage(level, pumpkillager, targetPlayer, "Hehehehehehe.", ChatFormatting.GRAY, 510);
        addMessage(level, pumpkillager, targetPlayer, "Thank you, " + targetPlayer.getName().getString() + ".", ChatFormatting.WHITE, 3000);
        addMessage(level, pumpkillager, targetPlayer, "You have freed me from these shackles.", ChatFormatting.WHITE, 5000, SpookyHeads.getEvilJackoLantern(1), "wear");
        addMessage(level, pumpkillager, targetPlayer, "My ancient form has been returned.", ChatFormatting.WHITE, 6000);
    }

    public static void startPostAncientFormConversation(Level level, Villager pumpkillager, Player targetPlayer) { // 2
        CompletableFuture.runAsync(() -> {
            ItemStack paperStack = Actions.generatePrisonAndCoordinatePaper(level, pumpkillager, targetPlayer);

            addEmptyMessage(level, pumpkillager, targetPlayer, 0);
            addMessage(level, pumpkillager, targetPlayer, "Are you surprised, mortal?", ChatFormatting.WHITE, 10);
            addMessage(level, pumpkillager, targetPlayer, "I've been stuck in that tiny form for eons.", ChatFormatting.WHITE, 2500);
            addMessage(level, pumpkillager, targetPlayer, "Now that I'm free, I can finally continue my world domination.", ChatFormatting.WHITE, 5000);
            addMessage(level, pumpkillager, targetPlayer, "Goodbye, " + targetPlayer.getName().getString() + ". There's no reward, sorry.", ChatFormatting.WHITE, 7500);

            Conversations.addMessageWithoutPrefix(level, pumpkillager, targetPlayer, "As the Pumpkillager flies away, you see something fall out of his pocket.", ChatFormatting.GRAY, 8490, paperStack, "drop");

            Scheduler.scheduleCharacterLeave(level, pumpkillager, 8500);
        });
    }

    public static void startInitialPrisonerShout(Level level, Villager prisoner, Player targetPlayer) { // 3
        if (prisoner.getTags().contains(Reference.MOD_ID + ".shoutedto." + targetPlayer.getName().getString())) {
            return;
        }
        prisoner.getTags().add(Reference.MOD_ID + ".shoutedto." + targetPlayer.getName().getString());

        addMessage(level, prisoner, targetPlayer, "Hey, over here!", ChatFormatting.WHITE, 0);
        addMessage(level, prisoner, targetPlayer, "I'm inside the tree!", ChatFormatting.WHITE, 4000);
        addMessage(level, prisoner, targetPlayer, "Help me!", ChatFormatting.WHITE, 5500);
    }

    public static void startPostPrisonerShout(Level level, Villager prisoner, Player targetPlayer) { // 4
        Set<String> prisonerTags = prisoner.getTags();
        String targetPlayerName = targetPlayer.getName().getString();

        if (prisonerTags.contains(Reference.MOD_ID + ".isknownto." + targetPlayerName) || prisonerTags.contains(Reference.MOD_ID + ".shoutedtotwice." + targetPlayerName)) {
            return;
        }

        addMessageWithoutPrefix(level, prisoner, targetPlayer, "Right click the prisoner to talk to him.", ChatFormatting.GRAY, 3000);
        prisoner.getTags().add(Reference.MOD_ID + ".shoutedtotwice." + targetPlayerName);
    }

    public static void startGettingToKnowPrisoner(Level level, Villager prisoner, Player targetPlayer) { // 5
        prisoner.getTags().add(Reference.MOD_ID + ".isknownto." + targetPlayer.getName().getString());
        prisoner.getTags().remove(Reference.MOD_ID + ".persistent");

        prisoner.setCustomName(Component.literal(Data.prisonerNameKnown).withStyle(Data.defaultPrisonerColour));

        addEmptyMessage(level, prisoner, targetPlayer, 0);
        addMessage(level, prisoner, targetPlayer, "Hello, my name is " + Data.prisonerNameKnown + ".", ChatFormatting.WHITE, 10);
        addMessage(level, prisoner, targetPlayer, "Did you come here to save me, " + targetPlayer.getName().getString() + "?", ChatFormatting.WHITE, 4000);
        addMessage(level, prisoner, targetPlayer, "I have been locked in here by a creature which called himself the Pumpkillager.", ChatFormatting.WHITE, 8000);
        addMessage(level, prisoner, targetPlayer, "I've heard stories about this mythical being when I was young, but didn't think he'd be real.", ChatFormatting.WHITE, 12000);
        addMessage(level, prisoner, targetPlayer, "Never thought I would get out, until I saw you appear through the bars.", ChatFormatting.WHITE, 16000);
        addMessage(level, prisoner, targetPlayer, "Thanks so much for creating a way out. My magic has disappeared since I've been in here.", ChatFormatting.WHITE, 20000);
        addEmptyMessage(level, prisoner, targetPlayer, 24000);
        addMessage(level, prisoner, targetPlayer, "You'll need this book. It contains ancient knowledge on how to stop him. Read it thoroughly.", ChatFormatting.WHITE, 24010, Data.getStopPkbook(), "give");
        addEmptyMessage(level, prisoner, targetPlayer, 29000);
        addMessage(level, prisoner, targetPlayer, "I believe if you follow that ritual, it should summon the Pumpkillager.", ChatFormatting.WHITE, 29010);
        addMessage(level, prisoner, targetPlayer, "Oh hmm I feel my magic tingle. Let me try something.", ChatFormatting.GRAY, 33500);

        Actions.processPrisonerItemGeneration(level, prisoner, targetPlayer, 36000);
    }

    public static void startPrisonerGenerationConversation(Level level, Villager prisoner, Player targetPlayer) { // 6
        ItemStack bowStack = new ItemStack(Items.BOW, 1);
        bowStack.enchant(Enchantments.POWER_ARROWS, 3);

        ItemStack arrowStack = new ItemStack(Items.SPECTRAL_ARROW, 4);

        addEmptyMessage(level, prisoner, targetPlayer, 1500);
        addMessage(level, prisoner, targetPlayer, "My magic is coming back, thank Notch.", ChatFormatting.GRAY, 1510);
        addEmptyMessage(level, prisoner, targetPlayer, 4000);
        addMessage(level, prisoner, targetPlayer, "Take these arrows, you'll need them to defeat the Pumpkillager.", ChatFormatting.WHITE, 4010, arrowStack, "give");
        addEmptyMessage(level, prisoner, targetPlayer, 8000);
        addMessage(level, prisoner, targetPlayer, "This bow will probably come in useful too.", ChatFormatting.WHITE, 8010, bowStack, "give");
        addEmptyMessage(level, prisoner, targetPlayer, 12000);
        addMessage(level, prisoner, targetPlayer, "Now be quick, before it's too late. Perform the reversed ritual from the book, and destroy that creature once and for all.", ChatFormatting.WHITE, 12010);
        addMessage(level, prisoner, targetPlayer, "You are going to need candles for the ritual. There should be some around here on the island.", ChatFormatting.WHITE, 16000);
        addMessage(level, prisoner, targetPlayer, "I'll be there too when you summon the Pumpkillager. Good bye, and thanks again!", ChatFormatting.WHITE, 20000);

        Scheduler.scheduleCharacterLeave(level, prisoner, 22500);
        targetPlayer.getTags().add(Reference.MOD_ID + ".cansummonfinalform");
    }

    public static void startInitialFinalBossConversation(Level level, Villager pumpkillager, Player targetPlayer) { // 7
        pumpkillager.lookAt(EntityAnchorArgument.Anchor.EYES, targetPlayer.position());

        String suffixa = "";
        String suffixb = "";
        if (targetPlayer.getTags().contains(Reference.MOD_ID + ".diedonce")) {
            suffixa = " AGAIN";
            suffixb = " still";
        }

        addEmptyMessage(level, pumpkillager, targetPlayer, 0);
        addMessage(level, pumpkillager, targetPlayer, "Huh.. how did I get here.", ChatFormatting.GRAY, 10);
        addMessage(level, pumpkillager, targetPlayer, "YOU" + suffixa + "!", ChatFormatting.WHITE, 2500);
        addMessage(level, pumpkillager, targetPlayer, "I" + suffixb + " don't know how you managed to complete this ritual correctly, but you're going to regret it.", ChatFormatting.WHITE, 4500);
        addEmptyMessage(level, pumpkillager, targetPlayer, 8500);
        addMessage(level, pumpkillager, targetPlayer, "FEEL MY WRATH", ChatFormatting.WHITE, 8510);

        Scheduler.scheduleMinionSummoning(level, pumpkillager, targetPlayer, 0, 9000);
    }

    public static void bossJustGotWeakenedConversation(Level level, Villager pumpkillager, Player targetPlayer) { // 8
        addEmptyMessage(level, pumpkillager, targetPlayer, 50);
        addMessage(level, pumpkillager, targetPlayer, "AAAAGHHHHHH", ChatFormatting.GOLD, 60);
        addMessage(level, pumpkillager, targetPlayer, "It hurts! What did you do to me?", ChatFormatting.WHITE, 1000);
    }

    public static void postFinalPrisonerConversation(Level level, Villager prisoner, Player targetPlayer) { // 9
        ItemStack rewardStack = new ItemStack(Items.NETHERITE_SWORD, 1);
        rewardStack.setHoverName(Component.literal("Pumpkalibur").withStyle(ChatFormatting.GOLD));
        rewardStack.enchant(Enchantments.SHARPNESS, 4);
        rewardStack.enchant(Enchantments.SWEEPING_EDGE, 3);

        addEmptyMessage(level, prisoner, targetPlayer, 0);
        addMessage(level, prisoner, targetPlayer, "You did it, " + targetPlayer.getName().getString() + "!", ChatFormatting.WHITE, 500);
        addMessage(level, prisoner, targetPlayer, "With the Pumpkillager defeated, the world has lost its most dangerous threat.", ChatFormatting.WHITE, 4500);
        addMessage(level, prisoner, targetPlayer, "Take this, you've earned it.", ChatFormatting.WHITE, 8500, rewardStack, "give");
        addEmptyMessage(level, prisoner, targetPlayer, 12500);
        addMessage(level, prisoner, targetPlayer, "The citizens of this world will not forget this.", ChatFormatting.WHITE, 12510);
        addMessage(level, prisoner, targetPlayer, "Thank you.", ChatFormatting.WHITE, 16500);

        Scheduler.scheduleCharacterLeave(level, prisoner, 18000);
    }

    // Add Message Functions
    public static void addMessage(Level level, Villager character, Player player, MutableComponent messageComponent, int msDelay) {
        addMessage(level, character, player, messageComponent, msDelay, null, "");
    }
    public static void addMessage(Level level, Villager character, Player player, MutableComponent messageComponent, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        addMessage(level, character, new Pair<Player, MutableComponent>(player, messageComponent), msDelay, itemStack, itemStackBehaviour);
    }
    public static void addMessage(Level level, Villager character, Player player, String message, ChatFormatting colour, int msDelay) {
        addMessage(level, character, player, message, colour, msDelay, null, "");
    }
    public static void addMessage(Level level, Villager character, Player player, String message, ChatFormatting colour, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        addMessage(level, character, createMessagePair(player, message, colour), msDelay, itemStack, itemStackBehaviour);
    }
    public static void addMessage(Level level, Villager character, Pair<Player, MutableComponent> messagePair, int msDelay) {
        addMessage(level, character, messagePair, msDelay, null, "");
    }
    public static void addMessage(Level level, Villager character, Pair<Player, MutableComponent> messagePair, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        if (character != null) {
            if (character.getTags().contains(Reference.MOD_ID + ".iskilled")) {
                return;
            }
        }

        if (msDelay == 0) {
            Data.messagesToSend.get(level).add(Util.modifyMessagePair(messagePair, Data.addCharacterPrefix(character, messagePair.getFirst(), messagePair.getSecond())));
            return;
        }
        Scheduler.scheduleCharacterMessage(level, character, Util.modifyMessagePair(messagePair, Data.addCharacterPrefix(character, messagePair.getFirst(), messagePair.getSecond())), msDelay, itemStack, itemStackBehaviour);
    }

    public static void addPostDeathMessage(Level level, Villager character, Player player, String message, ChatFormatting colour) {
        if (!message.equals("")) {
            Data.messagesToSend.get(level).add(createMessagePair(player, message));
            return;
        }
        addPostDeathMessage(level, character, createMessagePair(player, message, colour));
    }
    public static void addPostDeathMessage(Level level, Villager character, Pair<Player, MutableComponent> messagePair) {
        Data.messagesToSend.get(level).add(Util.modifyMessagePair(messagePair, Data.addCharacterPrefix(character, messagePair.getFirst(), messagePair.getSecond())));
    }

    public static void addEmptyMessage(Level level, Villager character, Player player, int msDelay) {
        addMessageWithoutPrefix(level, character, player, "", ChatFormatting.WHITE, msDelay);
    }
    public static void addMessageWithoutPrefix(Level level, Villager character, Player player, String messageString, ChatFormatting colour, int msDelay) {
        addMessageWithoutPrefix(level, character, player, messageString, colour, msDelay, null, "");
    }
    public static void addMessageWithoutPrefix(Level level, Villager character, Player player, MutableComponent component, int msDelay) {
        addMessageWithoutPrefix(level, character, player, component, msDelay, null, "");
    }
    public static void addMessageWithoutPrefix(Level level, Villager character, Player player, String messageString, ChatFormatting colour, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        Pair<Player, MutableComponent> messagePair = createMessagePair(player, messageString, colour);

        if (character != null) {
            if (character.getTags().contains(Reference.MOD_ID + ".iskilled")) {
                return;
            }
        }

        if (msDelay == 0) {
            Data.messagesToSend.get(level).add(messagePair);
            return;
        }
        Scheduler.scheduleCharacterMessage(level, character, messagePair, msDelay, itemStack, itemStackBehaviour);
    }
    public static void addMessageWithoutPrefix(Level level, Villager character, Player player, MutableComponent component, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        Pair<Player, MutableComponent> messagePair = createMessagePair(player, component);

        if (character != null) {
            if (character.getTags().contains(Reference.MOD_ID + ".iskilled")) {
                return;
            }
        }

        if (msDelay == 0) {
            Data.messagesToSend.get(level).add(messagePair);
            return;
        }
        Scheduler.scheduleCharacterMessage(level, character, messagePair, msDelay, itemStack, itemStackBehaviour);
    }

    // Prisoner Specific
    public static void sendJaxMessage(Level level, Player player, String message, ChatFormatting colour, int msDelay) {
        sendJaxMessage(level, player, message, colour, msDelay, null, "");
    }
    public static void sendJaxMessage(Level level, Player player, String message, ChatFormatting colour, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        Conversations.addMessageWithoutPrefix(level, null, player, Data.addCharacterPrefix("knownprisoner", player, Component.translatable(message).withStyle(colour)), msDelay, itemStack, itemStackBehaviour);
    }

    // Create Message Pairs
    public static Pair<Player, MutableComponent> createMessagePair(Player player, String message, ChatFormatting colour) {
        return new Pair<Player, MutableComponent>(player, Component.translatable(message).withStyle(colour));
    }
    public static Pair<Player, MutableComponent> createMessagePair(Player player, MutableComponent component) {
        return new Pair<Player, MutableComponent>(player, component);
    }
    public static Pair<Player, MutableComponent> createMessagePair(Player player, String messageString) {
        return new Pair<Player, MutableComponent>(player, Component.translatable(messageString));
    }
}
