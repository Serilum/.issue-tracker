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

package com.natamus.pumpkillagersquest.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.MessageFunctions;
import com.natamus.pumpkillagersquest.pumpkillager.Actions;
import com.natamus.pumpkillagersquest.pumpkillager.Conversations;
import com.natamus.pumpkillagersquest.pumpkillager.Manage;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.Reference;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class PkTickEvents {
    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.END)) {
            return;
        }

        Level level = e.level;

        if (!Data.messagesToSend.containsKey(level)) {
            Data.entitiesToYeet.put(level, new CopyOnWriteArrayList<LivingEntity>());
            Data.messagesToSend.put(level, new ArrayList<Pair<Player, MutableComponent>>());
            Data.allPumpkillagers.put(level, new CopyOnWriteArrayList<Villager>());
            Data.allPrisoners.put(level, new CopyOnWriteArrayList<Villager>());
            Data.lightningTasks.put(level, new HashMap<Villager, Runnable>());
            return;
        }

        if (!level.isClientSide) {
            if (Data.messagesToSend.get(level).size() > 0) {
                Pair<Player, MutableComponent> messagepair = Data.messagesToSend.get(level).get(0);
                Player player = messagepair.getFirst();
                MutableComponent component = messagepair.getSecond();

                MessageFunctions.sendMessage(player, component);

                Data.messagesToSend.get(level).remove(0);
            }
            if (Data.entitiesToYeet.get(level).size() > 0) {
                for (LivingEntity livingEntity : Data.entitiesToYeet.get(level)) {
                    Vec3 vec = livingEntity.position();
                    if (vec.y >= 200.0) {
                        Data.entitiesToYeet.get(level).remove(livingEntity);
                        if (livingEntity instanceof Villager) {
                            Villager character = (Villager)livingEntity;

                            Data.pumpkillagerPositions.remove(character);
                            Data.allPumpkillagers.get(level).remove(character);
                            Data.allPrisoners.get(level).remove(character);
                        }
                        livingEntity.setRemoved(Entity.RemovalReason.DISCARDED);
                        continue;
                    }

                    livingEntity.setPos(vec.x, vec.y + 1, vec.z);
                }
            }
        }
        if (Data.allPumpkillagers.get(level).size() > 0) {
            for (Villager pumpkillager : Data.allPumpkillagers.get(level)) {
                BlockPos pumpkillagerPos = pumpkillager.blockPosition();
                Component pumpkillagerComponent = pumpkillager.getName();
                String pumpkillagerName = pumpkillagerComponent.getString();

                if (pumpkillagerName.contains("|")) {
                    pumpkillager.setCustomName(Component.literal(Data.pumpkillagerName).withStyle(pumpkillagerComponent.getStyle()));
                }

                Set<String> pumpkillagerTags = pumpkillager.getTags();
                Player targetPlayer = Data.pumpkillagerPlayerTarget.get(pumpkillager);
                if (targetPlayer != null && !pumpkillagerTags.contains(Reference.MOD_ID + ".preventactions")) {
                    if (pumpkillager.tickCount % 20 == 0) {
                        if (pumpkillagerTags.contains(Reference.MOD_ID + ".initialencounter") || pumpkillager.getTags().contains(Reference.MOD_ID + ".finalform")) {
                            if (!BlockPosFunctions.withinDistance(pumpkillagerPos, targetPlayer.blockPosition(), 32)) {
                                Actions.sendDistanceMessage(level, pumpkillager, targetPlayer);

                                pumpkillager.getTags().add(Reference.MOD_ID + ".preventactions");
                                Manage.initiateCharacterLeave(level, pumpkillager);
                                continue;
                            }
                        }
                    }
                }

                if (pumpkillagerTags.contains(Reference.MOD_ID + ".finalform")) {
                    if (!pumpkillagerTags.contains(Reference.MOD_ID + ".isleaving") && !level.isNight()) {
                        if (targetPlayer != null) {
                            Conversations.addEmptyMessage(level, pumpkillager, targetPlayer, 0);
                            Conversations.addMessage(level, pumpkillager, targetPlayer, "You're too late, " + targetPlayer.getName().getString() + ". The night has passed and the magic holding me here has disappeared.", ChatFormatting.WHITE, 0);
                            Conversations.addMessage(level, pumpkillager, targetPlayer, "Bye.", ChatFormatting.WHITE, 0);

                            pumpkillager.getTags().add(Reference.MOD_ID + ".preventactions");
                        }

                        Manage.initiateCharacterLeave(level, pumpkillager);
                        continue;
                    }

                    if (pumpkillagerTags.contains(Reference.MOD_ID + ".summoninglightning")) {
                        if (Data.lightningTasks.get(level).containsKey(pumpkillager)) {
                            Runnable lightningTask = Data.lightningTasks.get(level).get(pumpkillager);
                            Data.lightningTasks.get(level).remove(pumpkillager);
                            level.getServer().execute(lightningTask);
                        }
                    }
                }

                if (pumpkillagerTags.contains(Reference.MOD_ID + ".initialencounter") && !pumpkillagerTags.contains(Reference.MOD_ID + ".isleaving")) {
                    if (pumpkillager.tickCount > 500) {
                        if (targetPlayer != null) {
                            Conversations.addMessage(level, pumpkillager, targetPlayer, "Fine. I see I'm wasting my time here. Goodbye, " + targetPlayer.getName().getString() + ".", ChatFormatting.WHITE, 10);
                        }

                        Manage.initiateCharacterLeave(level, pumpkillager);
                        continue;
                    }
                }

                if (pumpkillager.tickCount % 2 == 0) {
                    ItemStack feetStack = pumpkillager.getItemBySlot(EquipmentSlot.FEET);
                    if (feetStack.getItem().equals(Items.BARRIER)) {
                        float scaleCount = (float)feetStack.getCount();
                        if (scaleCount < 60F) {
                            if (scaleCount >= 10F) {
                                scaleCount -= 10F;
                            }

                            float scaleFloat = Float.parseFloat(feetStack.getHoverName().getString());
                            if (scaleFloat < scaleCount) {
                                feetStack.setHoverName(Component.literal((Util.roundFloat(scaleFloat + 0.025F, 3)) + ""));
                            }
                            else if (scaleFloat > scaleCount && scaleFloat - scaleCount >= 0.05F) {
                                feetStack.setHoverName(Component.literal((Util.roundFloat(scaleFloat - 0.1F, 3)) + ""));
                                if (scaleFloat < 0.2F) {
                                    Actions.turnPumpkillagerIntoHead(level, pumpkillager, Data.pumpkillagerPlayerTarget.get(pumpkillager));
                                    continue;
                                }
                            }
                            else if (!pumpkillagerTags.contains(Reference.MOD_ID + ".isweakened")) {
                                if (targetPlayer != null) {
                                    Villager newpumpkillager = Manage.createPumpkillager(level, pumpkillagerPos, targetPlayer, VillagerProfession.WEAPONSMITH, pumpkillager.getItemBySlot(EquipmentSlot.HEAD), ChatFormatting.RED, feetStack.getHoverName().getString(), true);
                                    level.addFreshEntity(newpumpkillager);

                                    Conversations.startTalking(level, newpumpkillager, targetPlayer, 2);
                                    continue;
                                }
                            }
                        }
                    }
                }

                if (level.isClientSide) {
                    continue;
                }

                if (pumpkillager.tickCount % 20 != 0) {
                    continue;
                }

                if (pumpkillagerTags.contains(Reference.MOD_ID + ".beingyeeted")) {
                    continue;
                }

                Vec3 vec = pumpkillager.position();
                boolean shouldYeet = !vec.equals(Data.pumpkillagerPositions.get(pumpkillager));

                if (targetPlayer == null) {
                    for (Entity ea : level.getEntities(null, new AABB(vec.x - 5, vec.y - 2, vec.z - 5, vec.x + 5, vec.y + 2, vec.z + 5))) {
                        if (ea instanceof Player) {
                            targetPlayer = (Player) ea;
                            break;
                        }
                    }
                }

                pumpkillager.lookAt(EntityAnchorArgument.Anchor.EYES, targetPlayer.position());

                if (shouldYeet) {
                    Manage.pumpkillagerMovedWrongly(level, pumpkillager, targetPlayer);
                }
            }
        }
        if (Data.allPrisoners.get(level).size() > 0) {
            for (Villager prisoner : Data.allPrisoners.get(level)) {
                if (!prisoner.getTags().contains(Reference.MOD_ID + ".lookingatplayer")) {
                    continue;
                }

                Vec3 vec = prisoner.position();
                if (!prisoner.getTags().contains(Reference.MOD_ID + ".isleaving")) {
                    if (Data.prisonerPositions.containsKey(prisoner)) {
                        if (!Data.prisonerPositions.get(prisoner).equals(vec)) {
                            vec = Data.prisonerPositions.get(prisoner);
                            prisoner.setPos(vec);
                        }
                    }
                    else {
                        Data.prisonerPositions.put(prisoner, vec);
                    }
                }

                for (Entity ea : level.getEntities(null, new AABB(vec.x-5, vec.y-2, vec.z-5, vec.x+5, vec.y+2, vec.z+5))) {
                    if (ea instanceof Player) {
                        prisoner.lookAt(EntityAnchorArgument.Anchor.EYES, ea.position());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.END)) {
            return;
        }

        Player player = e.player;
        if (player.tickCount % 100 != 0) {
            return;
        }

        Level level = player.getLevel();
        if (level.isClientSide) {
            return;
        }

        if (!Data.allPrisoners.containsKey(level)) {
            return;
        }

        if (Data.allPrisoners.get(level).size() == 0) {
            return;
        }

        String playerName = player.getName().getString();
        BlockPos playerPos = player.blockPosition();

        for (Villager prisoner : Data.allPrisoners.get(level)) {
            Set<String> prisonerTags = prisoner.getTags();
            if (prisonerTags.contains(Reference.MOD_ID + ".talkedto." + playerName) || prisonerTags.contains(Reference.MOD_ID + ".shoutedtwiceto." + playerName) || prisonerTags.contains(Reference.MOD_ID + ".afterfinal")) {
                continue;
            }

            BlockPos prisonerPos = prisoner.blockPosition();
            if (BlockPosFunctions.withinDistance(playerPos, prisonerPos, 16)) {
                if (prisonerTags.contains(Reference.MOD_ID + ".shoutedto." + playerName)) {
                    Conversations.startTalking(level, prisoner, player, 4);
                }
                else {
                    Conversations.startTalking(level, prisoner, player, 3);
                }
            }
        }
    }
}
