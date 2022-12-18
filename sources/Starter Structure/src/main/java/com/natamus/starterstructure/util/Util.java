/*
 * This is the latest source code of Starter Structure.
 * Minecraft version: 1.19.3, mod version: 1.2.
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

package com.natamus.starterstructure.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.*;
import com.natamus.collective.schematic.ParseSchematicFile;
import com.natamus.collective.schematic.ParsedSchematicObject;
import com.natamus.starterstructure.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Util {
    public static HashMap<Level, List<BlockPos>> protectedMap = new HashMap<Level, List<BlockPos>>();

    private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + Reference.MOD_ID;
    private static final File schematicDir = new File(dirpath + File.separator + "schematics");
    private static final File signDataDir = new File(dirpath + File.separator + "signdata");

    private static final Logger logger = LogUtils.getLogger();
    private static final String logPrefix = "[" + Reference.NAME + "] ";

    public static boolean initDirs() {
        if (!schematicDir.isDirectory()) {
            if (!schematicDir.mkdirs()) {
                return false;
            }
        }
        if (!signDataDir.isDirectory()) {
            return signDataDir.mkdirs();
        }
        return true;
    }

    public static void generateSchematic(ServerLevel serverLevel) {
        if (!schematicDir.isDirectory()) {
            if (!initDirs()) {
                logger.info(logPrefix + "Unable to generate directories.");
                return;
            }
        }

        List<File> listOfSchematicFiles = new ArrayList<File>();

        File[] listOfFiles = schematicDir.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().endsWith(".schem") || file.getName().endsWith(".schematic") || file.getName().endsWith(".nbt")) {
                listOfSchematicFiles.add(file);
            }
        }

        if (listOfSchematicFiles.size() == 0) {
            logger.info(logPrefix + "No schematics found to generate the starter structure.");
            return;
        }

        File schematicFile = listOfSchematicFiles.get(GlobalVariables.random.nextInt(listOfSchematicFiles.size()));
        if (!schematicFile.isFile()) {
            logger.info(logPrefix + "Unable to find starter structure file.");
            return;
        }

        int configYOffset = ConfigHandler.GENERAL.generatedStructureYOffset.get();
        boolean automaticCenter = schematicFile.getName().endsWith(".nbt");

        BlockPos spawnPos = serverLevel.getSharedSpawnPos().offset(0, configYOffset, 0).immutable();

        ParsedSchematicObject parsedSchematicObject;
        try (FileInputStream fileInputStream = new FileInputStream(schematicFile)){
            if (ConfigHandler.GENERAL.ignoreTreesDuringStructurePlacement.get()) {
                spawnPos = BlockPosFunctions.getSurfaceBlockPos(serverLevel, spawnPos.getX(), spawnPos.getZ(), true);
            }

            parsedSchematicObject = ParseSchematicFile.getParsedSchematicObject(fileInputStream, serverLevel, spawnPos, configYOffset, false, automaticCenter);
        }
        catch (Exception ex) {
            logger.info(logPrefix + "Exception while attempting to parse schematic file.");
            ex.printStackTrace();
            return;
        }

        if (!parsedSchematicObject.parsedCorrectly) {
            logger.info(logPrefix + "The starter structure object was not parsed correctly.");
            return;
        }

        BlockPos finalSpawnPos = spawnPos;
        MinecraftServer minecraftServer = serverLevel.getServer();

        minecraftServer.execute(() -> {
            List<BlockPos> protectedList = null;
            if (ConfigHandler.GENERAL.protectStructureBlocks.get()) {
                protectedList = new ArrayList<BlockPos>();
            }

            int yoffset = ConfigHandler.GENERAL.generatedStructureYOffset.get();

            logger.info(logPrefix + "Generating starter structure with " + parsedSchematicObject.blocks.size() + " blocks.");
            for (Pair<BlockPos, BlockState> blockPair : parsedSchematicObject.blocks) {
                BlockState blockState = blockPair.getSecond();
                Block block = blockState.getBlock();
                if (block instanceof JigsawBlock || block instanceof StructureBlock) {
                    if (ConfigHandler.GENERAL.generationIgnoreJigsawAndStructureBlocks.get()) {
                        continue;
                    }
                }

                serverLevel.setBlock(blockPair.getFirst(), blockState, 3);

                if (protectedList != null) {
                    protectedList.add(blockPair.getFirst());
                }
            }

            if (protectedList != null) {
                writeProtectedList(serverLevel, protectedList);
            }

            minecraftServer.execute(() -> {
                parsedSchematicObject.placeBlockEntitiesInWorld(serverLevel);

                Registry<EntityType<?>> entityTypeRegistry = serverLevel.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);
                for (Pair<BlockPos, BlockEntity> blockEntityPair : parsedSchematicObject.getBlockEntities(serverLevel)) {
                    BlockPos blockPos = blockEntityPair.getFirst();
                    BlockEntity blockEntity = blockEntityPair.getSecond();
                    if (blockEntity instanceof SignBlockEntity) {
                        SignBlockEntity signBlockEntity = (SignBlockEntity)blockEntity;
                        List<String> signLines = SignFunctions.getSignText(signBlockEntity);

                        String firstLine = signLines.get(0);
                        signLines.remove(0);
                        String signContent = String.join("", signLines);

                        Entity newEntity = null;
                        if (firstLine.contains("[Mob]") || firstLine.contains("[Entity]")) {
                            EntityType<?> entityType = entityTypeRegistry.get(new ResourceLocation(signContent));
                            if (entityType != null) {
                                newEntity = entityType.create(serverLevel);
                            }
                        }
                        else if (firstLine.contains("[NBT]")) {
                            String nbtFilePath = signDataDir + File.separator + signContent + ".txt";

                            File nbtTextFile = new File(nbtFilePath);
                            if (nbtTextFile.isFile()) {
                                int n = 1;
                                while (n >= 0) {
                                    String rawNBT = "";
                                    try {
                                        rawNBT = new String(Files.readAllBytes(Paths.get(nbtFilePath)));

                                        CompoundTag entityCompoundTag = TagParser.parseTag(rawNBT);
                                        Optional<Entity> optionalNewEntity = EntityType.create(entityCompoundTag, serverLevel);
                                        if (optionalNewEntity.isPresent()) {
                                            if (n != 1) {
                                                logger.info(logPrefix + "Unable to parse the " + signContent + ".txt entitydata file. Attempting automatic fix.");
                                            }

                                            newEntity = optionalNewEntity.get();
                                            n = -1;
                                        }
                                    } catch (Exception ex) {
                                        logger.info(logPrefix + "Unable to parse the " + signContent + ".txt entitydata file. Attempting automatic fix.");
                                        try {
                                            attemptEntityDataFileFix(nbtFilePath, rawNBT);
                                        }
                                        catch (IOException ignored) { }
                                    }

                                    n-=1;
                                }
                            }
                        }
                        else {
                            continue;
                        }

                        if (newEntity != null) {
                            newEntity.getTags().add(Reference.MOD_ID + ".protected");
                            newEntity.setPos(blockPos.getX()+0.5, blockPos.getY(), blockPos.getZ()+0.5);
                            serverLevel.addFreshEntity(newEntity);
                            serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }

                if (ConfigHandler.GENERAL.spawnNonSignEntitiesFromSupportedSchematics.get()) {
                    if (parsedSchematicObject.entities.size() > 0) {
                        for (Pair<BlockPos, Entity> entityPair : parsedSchematicObject.entities) {
                            Entity newEntity = entityPair.getSecond();
                            newEntity.getTags().add(Reference.MOD_ID + ".protected");
                            serverLevel.addFreshEntity(newEntity);
                        }
                    }
                }

                minecraftServer.execute(() -> {
                    float spawnAngle = serverLevel.getSharedSpawnAngle();

                    if (!isSpawnablePos(serverLevel, finalSpawnPos)) {
                        List<Integer> absoluteArray = Arrays.asList(-1, 1);

                        for (int i = 0; i <= 10; i++) {
                            for (BlockPos aroundPos : BlockPos.betweenClosed(finalSpawnPos.getX()-i, finalSpawnPos.getY()-i, finalSpawnPos.getZ()-i, finalSpawnPos.getX()+i, finalSpawnPos.getY()+i, finalSpawnPos.getZ()+i)) {
                                BlockPos upPos = aroundPos.above();
                                if (isSpawnablePos(serverLevel, aroundPos) && isSpawnablePos(serverLevel, upPos)) {
                                    serverLevel.setDefaultSpawnPos(aroundPos, spawnAngle);
                                    return;
                                }
                            }
                        }
                    }

                    serverLevel.setDefaultSpawnPos(finalSpawnPos, spawnAngle);
                });
            });
        });
    }

    private static void attemptEntityDataFileFix(String nbtFilePath, String rawNBT) throws IOException {
        String[] rawSplit = rawNBT.split("\\{", 2);
        if (rawSplit.length > 1) {
            String prefix = rawSplit[0];
            String newRawNBT = rawSplit[1];

            String idValue = "";
            if (prefix.contains(":")) {
                String[] prefixSplit = prefix.split(" ");
                for (String word : prefixSplit) {
                    if (word.contains(":")) {
                        idValue = "id:\"" + word + "\",";
                    }
                }
            }

            if (!idValue.equals("")) {
                newRawNBT = "{" + idValue + newRawNBT;
                Files.write(Path.of(nbtFilePath), newRawNBT.getBytes());
            }
        }
    }

    private static boolean isSpawnablePos(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock().equals(Blocks.AIR);
    }

    private static void writeProtectedList(ServerLevel serverLevel, List<BlockPos> protectedList) {
        protectedMap.put(serverLevel, protectedList);

        String protectedPath = WorldFunctions.getWorldPath(serverLevel) + File.separator + "data" + File.separator + Reference.MOD_ID + File.separator + "protection" + File.separator + DimensionFunctions.getSimpleDimensionString(serverLevel);
        File protectedPathDir = new File(protectedPath);

        if (!protectedPathDir.isDirectory()) {
            protectedPathDir.mkdirs();
        }

        try {
            FileWriter writer = new FileWriter(protectedPath + File.separator + "blocks.txt");

            for (BlockPos protectedPos : protectedList) {
                String coordinateString = protectedPos.getX() + "," + protectedPos.getY() + "," + protectedPos.getZ();
                writer.write(coordinateString + "\n");
            }

            writer.close();
        }
        catch (IOException ignored) { }
    }

    public static void readProtectedList(ServerLevel serverLevel) {
        String protectedPath = WorldFunctions.getWorldPath(serverLevel) + File.separator + "data" + File.separator + Reference.MOD_ID + File.separator + "protection" + File.separator + DimensionFunctions.getSimpleDimensionString(serverLevel);
        File protectedPathDir = new File(protectedPath);

        if (!protectedPathDir.isDirectory()) {
            return;
        }

        String protectedFilePath = protectedPath + File.separator + "blocks.txt";
        if (!(new File(protectedFilePath)).isFile()) {
            return;
        }

        try {
            String rawProtectedList = new String(Files.readAllBytes(Paths.get(protectedFilePath)));

            List<BlockPos> newProtectedList = new ArrayList<BlockPos>();
            for (String coordinateString : rawProtectedList.split("\n")) {
                String[] csspl = coordinateString.split(",");
                if (csspl.length == 3) {
                    try {
                        newProtectedList.add(new BlockPos(Integer.parseInt(csspl[0]), Integer.parseInt(csspl[1]), Integer.parseInt(csspl[2])));
                    }
                    catch (NumberFormatException ignored) { }
                }
            }

            if (newProtectedList.size() > 0) {
                protectedMap.put(serverLevel, newProtectedList);
            }
        } catch (IOException ignored) { }
    }
}
