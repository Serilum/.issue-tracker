/*
 * This is the latest source code of Manure.
 * Minecraft version: 1.19.3, mod version: 1.3.
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

package com.natamus.manure.events;

import com.natamus.manure.config.ConfigHandler;
import com.natamus.manure.items.ManureItems;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CopyOnWriteArrayList;

public class ManureDropEvent {
    private static CopyOnWriteArrayList<Animal> loadedAnimals = new CopyOnWriteArrayList<Animal>();

    public static void onServerTick(MinecraftServer server) {
        if (server.getTickCount() % ConfigHandler.manureDropDelayTicks != 0) {
            return;
        }

        for (Animal animal : loadedAnimals) {
            if (!animal.isAlive()) {
                loadedAnimals.remove(animal);
                continue;
            }

            animal.level.addFreshEntity(new ItemEntity(animal.level, animal.getX(), animal.getY()+0.5, animal.getZ(), new ItemStack(ManureItems.MANURE, 1)));
        }
    }

    public static void onEntityJoin(Entity entity, ServerLevel world) {
        if (entity instanceof Animal) {
            loadedAnimals.add((Animal)entity);
        }
    }

    public static void onEntityLeave(Entity entity, ServerLevel world) {
        if (entity instanceof Animal) {
            loadedAnimals.remove((Animal)entity);
        }
    }
}