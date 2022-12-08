/*
 * This is the latest source code of Biome Spawn Point.
 * Minecraft version: 1.19.3, mod version: 1.6.
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

package com.natamus.biomespawnpoint.util;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.DataFunctions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {
    private static List<String> spawnBiomes = new ArrayList<String>();
    private static List<Biome> processedBiomes = new ArrayList<Biome>();

    private static String dirpath = DataFunctions.getConfigDirectory() + File.separator + Reference.MOD_ID;
    private static File dir = new File(dirpath);
    private static File file = new File(dirpath + File.separator + "spawnbiomes.txt");

    public static void loadSpawnBiomeConfig(Registry<Biome> biomeRegistry) throws Exception {
        PrintWriter writer = null;
        if (!dir.isDirectory() || !file.isFile()) {
            if (dir.mkdirs()) {
                writer = new PrintWriter(dirpath + File.separator + "spawnbiomes.txt", StandardCharsets.UTF_8);
                writer.println("// To generate missing and modded biomes in this list; create a new world once,");
                writer.println("// Any biome listed below without an ! at the start will be randomly chosen to spawn in,");
            }
        }
        else {
            String blcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "spawnbiomes.txt")));
            for (String rawLine : blcontent.split("," )) {
                String line = rawLine.replace("\n", "").trim();
                if (line.startsWith("//")) {
                    continue;
                }

                if (!line.contains(":")) {
                    continue;
                }

                String rlName = line.replace("!", "");
                if (!line.startsWith("!")) {
                    if (!spawnBiomes.contains(rlName)) {
                        spawnBiomes.add(rlName);
                    }
                }

                ResourceLocation resourceLocation = ResourceLocation.tryParse(rlName);

                Biome biome = null;
                try {
                    if (biomeRegistry != null) {
                        biome = biomeRegistry.get(resourceLocation);
                    } else {
                        biome = ForgeRegistries.BIOMES.getDelegate(resourceLocation).orElseThrow().get();
                    }
                }
                catch (Exception ignored) {}

                if (biome != null) {
                    if (!processedBiomes.contains(biome)) {
                        processedBiomes.add(biome);
                    }
                }
            }
        }

        List<Biome> biomeList = new ArrayList<Biome>();
        if (biomeRegistry != null) {
            for (Biome biome : biomeRegistry) {
                biomeList.add(biome);
            }
        }
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (!biomeList.contains(biome)) {
                biomeList.add(biome);
            }
        }

        List<String> biomeRlToAdd = new ArrayList<String>();
        for (Biome biome : biomeList) {
            if (processedBiomes.contains(biome)) {
                continue;
            }

            ResourceLocation rl;
            if (biomeRegistry != null) {
                rl = biomeRegistry.getKey(biome);
            }
            else {
                rl = ForgeRegistries.BIOMES.getKey(biome);
            }

            if (rl == null) {
                continue;
            }

            String name = rl.toString();
            biomeRlToAdd.add(name);
            processedBiomes.add(biome);
        }

        if (biomeRlToAdd.size() > 0) {
            if (writer == null) {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(dirpath + File.separator + "spawnbiomes.txt", true)));
            }

            for (String rlName : biomeRlToAdd) {
                writer.println("!" + rlName + ",");
            }
        }

        if (writer != null) {
            writer.close();
        }
    }

    public static int spawnBiomeListSize() {
        return spawnBiomes.size();
    }

    public static List<String> getSpawnBiomes() {
        return new ArrayList<String>(spawnBiomes);
    }

    public static String getSpawnBiome() {
        return getSpawnBiomes().get(GlobalVariables.random.nextInt(spawnBiomeListSize()));
    }
}
