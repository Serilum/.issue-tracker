/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.0.
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

import com.natamus.collective.functions.MessageFunctions;
import com.natamus.pumpkillagersquest.pumpkillager.Manage;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.QuestData;
import com.natamus.pumpkillagersquest.util.Reference;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber
public class PkEntityEvents {
    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent e) {
        Level level = e.getLevel();
        Entity entity = e.getEntity();
        if (entity.getTags().contains(Reference.MOD_ID + ".removed")) {
            e.setCanceled(true);
            return;
        }

        Set<String> entityTags = entity.getTags();

        if (entityTags.contains(Reference.MOD_ID + ".summoned")) {
            if (entityTags.contains(Reference.MOD_ID + ".justadded")) {
                entity.getTags().remove(Reference.MOD_ID + ".justadded");
                return;
            }
            e.setCanceled(true);
        }
        else if (Util.isPumpkillager(entity)) {
            Villager pumpkillager = (Villager)entity;

            if (entityTags.contains(Reference.MOD_ID + ".justadded")) {
                entity.getTags().remove(Reference.MOD_ID + ".justadded");

                if (!Data.allPumpkillagers.get(level).contains(pumpkillager)) {
                    Data.allPumpkillagers.get(level).add(pumpkillager);
                }
                return;
            }

            Manage.resetPlacedBlocks(level, pumpkillager.blockPosition());
            e.setCanceled(true);
        }
        else if (Util.isPrisoner(entity)) {
            Villager prisoner = (Villager)entity;

            if (entityTags.contains(Reference.MOD_ID + ".justadded") || entityTags.contains(Reference.MOD_ID + ".persistent")) {
                entity.getTags().remove(Reference.MOD_ID + ".justadded");

                if (!Data.allPrisoners.get(level).contains(prisoner)) {
                    Data.allPrisoners.get(level).add(prisoner);
                }
                return;
            }

            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityLeave(EntityLeaveLevelEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Villager) {
            Level level = e.getLevel();
            if (Util.isPumpkillager(entity)) {
                Villager pumpkillager = (Villager)entity;

                if (!level.isClientSide) {
                    if (entity.getTags().contains(Reference.MOD_ID + ".finalform")) {
                        ServerLevel serverLevel = (ServerLevel)level;
                        for (Entity serverEntity : serverLevel.getAllEntities()) {
                            if (serverEntity.getTags().contains(Reference.MOD_ID + ".summoned")) {
                                if (serverEntity instanceof LivingEntity) {
                                    Manage.yeetLivingEntityIntoSky(level, (LivingEntity)serverEntity);
                                }
                            }
                        }
                    }
                }

                if (Data.allPumpkillagers.containsKey(level)) {
                    Data.allPumpkillagers.get(level).remove(pumpkillager);
                }

                Data.pumpkillagerPositions.remove(pumpkillager);
                Data.pumpkillagerPlayerTarget.remove(pumpkillager);
                Data.pumpkillagerBossEvents.remove(pumpkillager);
            }
            else if (Util.isPrisoner(entity)) {
                if (Data.allPrisoners.containsKey(level)) {
                    Data.allPrisoners.get(level).remove((Villager) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent e) {
        Player player = e.getEntity();
        Level level = player.level;
        if (level.isClientSide) {
            return;
        }

        ItemStack itemStack = e.getItem().getItem();
        if (!itemStack.getItem().equals(Items.PAPER)) {
            return;
        }

        String itemName = itemStack.getHoverName().getString();
        if (!itemName.contains(QuestData.questOneCoordinatePaperPrefix)) {
            return;
        }

        if (itemName.endsWith(".")) {
            MessageFunctions.sendMessage(player, "As you pick up the piece of paper, you see coordinates to a prisoner camp written on it. You feel bad about unleashing the Pumpkillager back into the world. Maybe a prisoner can help you stop him?", ChatFormatting.GRAY, true);
            itemStack.setHoverName(Component.translatable(itemName.replace(".", "")));
        }

        MessageFunctions.sendMessage(player, itemName, ChatFormatting.GRAY, true);
    }

    @SubscribeEvent
    public void onEntityHitByLightning(EntityStruckByLightningEvent e) {
        Entity entity = e.getEntity();
        if (Util.isPumpkillager(entity) || Util.isPrisoner(entity)) {
            e.setCanceled(true);
        }
    }
}
