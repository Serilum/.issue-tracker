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

package com.natamus.pumpkillagersquest.util;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.schematic.ParseSchematicFile;
import com.natamus.collective.schematic.ParsedSchematicObject;
import com.natamus.pumpkillagersquest.pumpkillager.Prisoner;
import com.natamus.pumpkillagersquest.rendering.ClientRenderEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class GenerateStructure {
    public static void generatePrisonerCamp(Level level, Player player, BlockPos cPos, int pasteNBlocksAboveSurface) {
        if (Data.modContainer == null) {
            System.out.println("[Pumpkillager's Quest] Error generating prisoner camp: mod container is null.");
            return;
        }

        MinecraftServer minecraftServer = level.getServer();
        BlockPos centerPos = cPos.immutable();

        ChunkPos chunkPos = level.getChunkAt(centerPos).getPos();

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                LevelChunk levelChunk = level.getChunk(chunkPos.x+x, chunkPos.z+z);
                levelChunk.postProcessGeneration();
            }
        }

        boolean isPeaceful = minecraftServer.getWorldData().getDifficulty().equals(Difficulty.PEACEFUL);

        InputStream schematicInputstream = Util.getSchematicsInputStream(minecraftServer, "floating_prisoner_camp");
        if (schematicInputstream == null) {
            System.out.println("[Pumpkillager's Quest] Error generating prisoner camp: inputstream is null.");
            return;
        }

        ParsedSchematicObject parsedSchematicObject = ParseSchematicFile.getParsedSchematicObject(schematicInputstream, level, centerPos, pasteNBlocksAboveSurface, false);

        if (!parsedSchematicObject.parsedCorrectly) {
            System.out.println("[Pumpkillager's Quest] Error generating prisoner camp: schematic object didn't parse.");
            return;
        }

        minecraftServer.execute(() -> {
            for (Pair<BlockPos, BlockState> blockPair : parsedSchematicObject.blocks) {
                level.setBlock(blockPair.getFirst(), blockPair.getSecond(), 3);
            }

            minecraftServer.execute(() -> {
                List<ItemStack> pumpkinHeads = SpookyHeads.getAllPumpkinHeads();
                List<Integer> chestSlotRange = new ArrayList<Integer>(IntStream.rangeClosed(0, 26).boxed().toList());
                List<Integer> randomAmounts = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2));
                List<Item> candleItems = new ArrayList<Item>(Arrays.asList(Items.BLACK_CANDLE, Items.ORANGE_CANDLE, Items.RED_CANDLE, Items.WHITE_CANDLE));
                List<Integer> candleAmounts = new ArrayList<Integer>(Arrays.asList(1, 2, 1, 1, 2, 1, 2, 2, 1, 1, 2));

                for (Pair<BlockPos, BlockEntity> blockEntityPair : parsedSchematicObject.getBlockEntities(level)) {
                    BlockPos blockPos = blockEntityPair.getFirst();
                    BlockEntity blockEntity = blockEntityPair.getSecond();
                    if (blockEntity instanceof ChestBlockEntity) {
                        ChestBlockEntity chestBlockEntity = new ChestBlockEntity(blockPos, blockEntity.getBlockState());
                        chestBlockEntity.setCustomName(Component.translatable("Ritual Storage"));

                        Collections.shuffle(pumpkinHeads);
                        Collections.shuffle(chestSlotRange);
                        Collections.shuffle(candleItems);
                        Collections.shuffle(candleAmounts);

                        for (ItemStack pumpkinHead : pumpkinHeads) {
                            pumpkinHead.setCount(randomAmounts.get(GlobalVariables.random.nextInt(randomAmounts.size())));
                            chestBlockEntity.setItem(chestSlotRange.get(0), pumpkinHead);
                            chestSlotRange.remove(0);
                        }

                        if (candleItems.size() > 0) {
                            int i = 0;
                            for (int remainingChestSlot : chestSlotRange) {
                                chestBlockEntity.setItem(remainingChestSlot, new ItemStack(candleItems.get(0), candleAmounts.get(i)));
                                i += 1;
                            }
                            candleItems.remove(0);
                        }

                        level.setBlockEntity(chestBlockEntity);

                        chestSlotRange = new ArrayList<Integer>(IntStream.rangeClosed(0, 26).boxed().toList());
                    }
                }
                minecraftServer.execute(() -> {
                    int aboveFloorYLevel = pasteNBlocksAboveSurface+18;

                    BlockPos prisonerPos = centerPos.north(11).east(2).above(aboveFloorYLevel).immutable();
                    Villager prisoner = Prisoner.createPrisoner(level, prisonerPos, player, VillagerProfession.NITWIT, SpookyHeads.getCarvedPumpkin(1), ChatFormatting.GOLD, false);

                    prisoner.getTags().add(Reference.MOD_ID + ".persistent");

                    level.addFreshEntity(prisoner);

                    // Ghost Knight
                    BlockPos ghostKnightPos = centerPos.north(3).east(3).above(aboveFloorYLevel).immutable();

                    SkeletonHorse ghostKnightHorse = EntityType.SKELETON_HORSE.create(level);
                    ghostKnightHorse.setPos(ghostKnightPos.getX()+0.5, ghostKnightPos.getY(), ghostKnightPos.getZ()+0.5);
                    ghostKnightHorse.equipSaddle(null);
                    ghostKnightHorse.setTamed(true);

                    LivingEntity ghostKnight;
                    if (!isPeaceful) {
                        ghostKnight = EntityType.HUSK.create(level);

                        ItemStack swordStack = new ItemStack(Items.GOLDEN_SWORD);
                        swordStack.enchant(Enchantments.SHARPNESS, 1);
                        ghostKnight.setItemSlot(EquipmentSlot.MAINHAND, swordStack);
                        ghostKnight.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                    }
                    else {
                        ghostKnight = EntityType.VILLAGER.create(level);
                    }
                    ghostKnight.setItemSlot(EquipmentSlot.HEAD, SpookyHeads.getGhostKnightHead(1));
                    ghostKnight.setPos(ghostKnightPos.getX() + 0.5, ghostKnightPos.getY(), ghostKnightPos.getZ() + 0.5);
                    ghostKnight.setCustomName(Component.translatable("The Ghost Knight"));

                    level.addFreshEntity(ghostKnightHorse);
                    level.addFreshEntity(ghostKnight);


                    // Ghost Rider
                    BlockPos ghostRiderPos = centerPos.south(3).west(3).above(aboveFloorYLevel).immutable();

                    ZombieHorse ghostRiderHorse = EntityType.ZOMBIE_HORSE.create(level);
                    ghostRiderHorse.setPos(ghostRiderPos.getX()+0.5, ghostRiderPos.getY(), ghostRiderPos.getZ()+0.5);
                    ghostRiderHorse.equipSaddle(null);
                    ghostRiderHorse.setTamed(true);

                    LivingEntity ghostRider;
                    if (!isPeaceful) {
                        ghostRider = EntityType.STRAY.create(level);

                        ItemStack bowStack = new ItemStack(Items.BOW);
                        bowStack.enchant(Enchantments.INFINITY_ARROWS, 1);
                        ghostRider.setItemSlot(EquipmentSlot.MAINHAND, bowStack);
                    }
                    else {
                        ghostRider = EntityType.VILLAGER.create(level);
                    }
                    ghostRider.setItemSlot(EquipmentSlot.HEAD, SpookyHeads.getGhostRiderHead(1));
                    ghostRider.setPos(ghostKnightPos.getX() + 0.5, ghostRiderPos.getY(), ghostKnightPos.getZ() + 0.5);
                    ghostRider.setCustomName(Component.translatable("The Ghost Rider"));

                    level.addFreshEntity(ghostRiderHorse);
                    level.addFreshEntity(ghostRider);

                    minecraftServer.execute(() -> {
                        ghostKnight.startRiding(ghostKnightHorse);
                        ghostRider.startRiding(ghostRiderHorse);

                        for (int x = -2; x <= 2; x++) {
                            for (int z = -2; z <= 2; z++) {
                                LevelChunk levelChunk = level.getChunk(chunkPos.x+x, chunkPos.z+z);
                                levelChunk.postProcessGeneration();
                            }
                        }
                    });
                });
            });
        });
    }

    public static void generateClientRitualVision(Level level, Player player, BlockPos clickPos, ItemStack bookstack) {
        if (!level.isClientSide) {
            return;
        }

        BlockPos centerPos = clickPos.above().immutable();

        Block centerBlock = Blocks.TNT;
        Block ritualBlock = Blocks.REDSTONE_WIRE;
        if (!bookstack.getDisplayName().getString().contains("Quest")) {
            centerBlock = Blocks.PUMPKIN;
            ritualBlock = Blocks.CANDLE;
        }

        ClientRenderEvent.setTemporaryRitualRender(centerPos, centerBlock, ritualBlock);
    }
}
