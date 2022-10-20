/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.5.
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

import com.mojang.authlib.GameProfile;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.MessageFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.pumpkillagersquest.config.ConfigHandler;
import com.natamus.pumpkillagersquest.pumpkillager.Conversations;
import com.natamus.pumpkillagersquest.pumpkillager.Manage;
import com.natamus.pumpkillagersquest.pumpkillager.RitualCheck;
import com.natamus.pumpkillagersquest.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PkBlockEvents {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent e) {
        Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level.isClientSide) {
            return;
        }

        Player player = e.getPlayer();
        BlockState state = e.getState();
        Block block = state.getBlock();
        BlockPos pos = e.getPos();

        if (block instanceof SkullBlock || block instanceof WallSkullBlock) {
            if (!player.isCreative()) {
                SkullBlockEntity sbe = (SkullBlockEntity) level.getBlockEntity(pos);
                if (sbe != null) {
                    GameProfile profile = sbe.getOwnerProfile();
                    if (profile != null) {
                        UUID uuid = profile.getId();
                        if (uuid != null) {
                            String headid = uuid.toString();

                            String correctheadname = "";
                            for (String headname : SpookyHeads.allHeadData.keySet()) {
                                String headnameid = SpookyHeads.allHeadData.get(headname).getFirst();
                                if (headid.equals(headnameid)) {
                                    correctheadname = headname;
                                    break;
                                }
                            }

                            if (!correctheadname.equals("")) {
                                ItemStack named_headstack = SpookyHeads.getSpookyOrPumpkinHead(correctheadname, 1);

                                if (named_headstack != null) {
                                    e.setCanceled(true);

                                    level.destroyBlock(pos, false);
                                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), named_headstack));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (Data.globalProcessedPoss.contains(pos)) {
            for (Entity ea : level.getEntities(null, new AABB(pos.getX()-2, pos.getY()-2, pos.getZ()-2, pos.getX()+2, pos.getY()+2, pos.getZ()+2))) {
                if (ea instanceof Villager) {
                    if (Util.isPumpkillager(ea)) {
                        Villager pumpkillager = (Villager)ea;

                        MessageFunctions.sendMessage(player, Component.literal(""));
                        Conversations.addMessage(level, pumpkillager, player, "How dare you try to steal my blocks, " + player.getName().getString() + ".", ChatFormatting.WHITE, 0);
                        Conversations.addMessage(level, pumpkillager, player, "I will be back.", ChatFormatting.WHITE, 0);

                        pumpkillager.getTags().add(Reference.MOD_ID + ".preventactions");

                        e.setCanceled(true);
                        Manage.initiateCharacterLeave(level, pumpkillager);
                        return;
                    }
                }
            }
        }

        Set<String> playerTags = player.getTags();
        if (!playerTags.contains(Reference.MOD_ID + ".completedquest") && !playerTags.contains(Reference.MOD_ID + ".unleashed")) {
            if (Util.isPumpkinBlock(block)) {
                if (!Data.pumpkillagerPlayerTarget.containsValue(player)) {
                    if (Util.pumpkinBlockIsClear(level, pos)) {
                        if (GlobalVariables.random.nextDouble() <= ConfigHandler.GENERAL.pumpkillagerSpawnChance.get()) {
                            boolean foundQuestBook = false;

                            Inventory inventory = player.getInventory();
                            for (int i = 0; i < inventory.getContainerSize(); i++) {
                                ItemStack inventoryStack = inventory.getItem(i);
                                if (inventoryStack.getItem() instanceof WrittenBookItem) {
                                    String bookName = inventoryStack.getHoverName().getString();
                                    if (bookName.equals(Data.questBookName)) {
                                        foundQuestBook = true;
                                        break;
                                    }
                                }
                            }

                            if (foundQuestBook) {
                                return;
                            }

                            Manage.spawnPumpkillager(level, player, pos);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onCandleClick(PlayerInteractEvent.RightClickBlock e) {
        Level level = e.getLevel();
        Player player = e.getEntity();

        ItemStack itemStack = e.getItemStack();
        Item item = itemStack.getItem();
        if (item instanceof FlintAndSteelItem) {
            if (!player.getTags().contains(Reference.MOD_ID + ".cansummonfinalform")) {
                return;
            }

            BlockPos pos = e.getPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (!(block instanceof CandleBlock)) {
                return;
            }

            CandleBlock candleBlock = (CandleBlock) block;
            boolean isLit = state.getValue(CandleBlock.LIT);
            if (isLit) {
                return;
            }

            BlockPos pumpkinPos = null;
            for (BlockPos aroundPos : BlockPos.betweenClosed(pos.getX() - 3, pos.getY(), pos.getZ() - 3, pos.getX() + 3, pos.getY(), pos.getZ() + 3)) {
                if (Util.isPumpkinBlock(level.getBlockState(aroundPos).getBlock())) {
                    pumpkinPos = aroundPos.immutable();
                    break;
                }
            }

            if (pumpkinPos == null) {
                return;
            }

            RitualCheck.checkRitualFinalSummoning(level, player, pumpkinPos, pos);
        }
        else if (item instanceof WrittenBookItem) {
            String bookName = itemStack.getDisplayName().getString();
            if (bookName.contains("Pumpkillager")) {
                e.setCanceled(true);
                e.setCancellationResult(InteractionResult.FAIL);

                if (level.isClientSide) {
                    GenerateStructure.generateClientRitualVision(level, player, e.getPos(), itemStack);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent e) {
        Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level == null) {
            return;
        }

        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        BlockState placedState = e.getPlacedBlock();
        if (!Util.isPumpkinBlock(placedState.getBlock())) {
            return;
        }

        Player player = (Player)entity;
        if (!player.getTags().contains(Reference.MOD_ID + ".cansummonfinalform")) {
            return;
        }

        RitualCheck.checkRitualFinalSummoning(level, player, e.getPos(), null);
    }
}
