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

package com.natamus.pumpkillagersquest.util;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.pumpkillagersquest.pumpkillager.Conversations;
import com.natamus.pumpkillagersquest.pumpkillager.Manage;
import com.natamus.pumpkillagersquest.pumpkillager.Summon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class Scheduler {
    public static void scheduleCharacterMessage(Level level, Villager character, Pair<Player, MutableComponent> messagePair, int msDelay) {
        scheduleCharacterMessage(level, character, messagePair, msDelay, null, "");
    }
    public static void scheduleCharacterMessage(Level level, Villager character, Pair<Player, MutableComponent> messagePair, int msDelay, ItemStack itemStack, String itemStackBehaviour) {
        new Thread(() -> {
            try  { Thread.sleep( msDelay ); }
            catch (InterruptedException ignored)  {}

            boolean shouldSendMessage = true;
            Vec3 characterVec = null;
            if (character != null) {
                if (!character.isRemoved()) {
                    Set<String> tags = character.getTags();

                    if (!tags.contains(Reference.MOD_ID + ".iskilled") && !tags.contains(Reference.MOD_ID + ".preventactions")) {
                        characterVec = character.position();

                        if (Util.isPumpkillager(character)) {
                            if (!tags.contains(Reference.MOD_ID + ".removed") && !tags.contains(Reference.MOD_ID + ".beingyeeted")) {
                                if (!characterVec.equals(Data.pumpkillagerPositions.get(character))) {
                                    Manage.pumpkillagerMovedWrongly(level, character, messagePair.getFirst());
                                    return;
                                }
                            }
                        }
                    }
                    else {
                        shouldSendMessage = false;
                    }
                }
                else {
                    shouldSendMessage = false;
                }
            }

            if (shouldSendMessage) {
                Data.messagesToSend.get(level).add(messagePair);
            }

            if (itemStack != null) {
                switch (itemStackBehaviour) {
                    case "hold" -> {
                        if (character != null) {
                            character.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                        }
                    }
                    case "give" -> {
                        Player player = messagePair.getFirst();

                        int amount = itemStack.getCount();
                        String itemName = amount + " " + ItemFunctions.itemToReadableString(itemStack.getItem());
                        if (amount == 1 && itemStack.hasCustomHoverName()) {
                            itemName = itemStack.getHoverName().getString();
                        }

                        Data.messagesToSend.get(level).add(Conversations.createMessagePair(player, Component.translatable("You have been given " + itemName + ".").withStyle(ChatFormatting.GRAY)));
                        ItemFunctions.giveOrDropItemStack(messagePair.getFirst(), itemStack);
                    }
                    case "wear" -> {
                        if (character != null) {
                            if (messagePair.getSecond().getString().contains("freed me from these shackles")) {
                                character.setVillagerData(character.getVillagerData().setType(VillagerType.SNOW).setProfession(VillagerProfession.WEAPONSMITH));
                                character.setCustomName(null);

                                ItemStack feetStack = character.getItemBySlot(EquipmentSlot.FEET);
                                if (feetStack.getItem().equals(Items.BARRIER)) {
                                    feetStack.setCount(3);
                                }
                            }
                            character.setItemSlot(EquipmentSlot.HEAD, itemStack);
                        }
                    }
                    case "drop" -> {
                        if (characterVec != null) {
                            ItemEntity ie = new ItemEntity(level, characterVec.x, characterVec.y + 1, characterVec.z, itemStack);
                            level.addFreshEntity(ie);

                            if (itemStack.getItem().equals(Items.PAPER)) {
                                Player player = messagePair.getFirst();
                                player.getTags().add(Reference.MOD_ID + ".unleashed");
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public static void scheduleCharacterLeave(Level level, Villager character, int msDelay) {
        new Thread(() -> {
            try  { Thread.sleep( msDelay ); }
            catch (InterruptedException ignored)  {}

            character.getTags().remove(Reference.MOD_ID + ".persistent");
            character.getTags().add(Reference.MOD_ID + ".isleaving");

            Manage.initiateCharacterLeave(level, character);
        }).start();
    }

    public static void scheduleMinionSummoning(Level level, Villager pumpkillager, Player targetPlayer, int summonId, int msDelay) {
        if (level.isClientSide) {
            return;
        }

        new Thread(() -> {
            try  { Thread.sleep( msDelay ); }
            catch (InterruptedException ignored)  {}

            level.getServer().execute(() -> {
                Summon.summonFinalBossMinions(level, pumpkillager, targetPlayer, summonId);
            });
        }).start();
    }

    public static void scheduleFireExtuingish(Level level, BlockPos firePos, LivingEntity livingEntityOnFire, boolean healTarget) {
        scheduleFireExtuingish(level, firePos, livingEntityOnFire, healTarget, 0);
    }
    public static void scheduleFireExtuingish(Level level, BlockPos firePos, LivingEntity livingEntityOnFire, boolean healTarget, int loopCount) {
        new Thread(() -> {
            try  { Thread.sleep( 250 ); }
            catch (InterruptedException ignored)  {}

            level.getServer().execute(() -> {
                for (BlockPos apos : BlockPos.betweenClosed(firePos.getX() - 1, firePos.getY() - 1, firePos.getZ() - 1, firePos.getX() + 1, firePos.getY() + 1, firePos.getZ() + 1)) {
                    if (level.getBlockState(apos).getBlock().equals(Blocks.FIRE)) {
                        level.setBlock(apos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }

                if (healTarget) {
                    Vec3 vec = livingEntityOnFire.position();
                    for (Entity ea : level.getEntities(null, new AABB(vec.x - 3, vec.y - 3, vec.z - 3, vec.x + 3, vec.y + 3, vec.z + 3))) {
                        if (ea instanceof LivingEntity) {
                            LivingEntity lea = (LivingEntity)ea;
                            if (lea.isOnFire()) {
                                lea.clearFire();
                                lea.setHealth(lea.getMaxHealth());
                            }
                        }
                    }
                }

                if (loopCount < 4) {
                    scheduleFireExtuingish(level, firePos, livingEntityOnFire, healTarget, loopCount+1);
                }
            });
        }).start();
    }
}
