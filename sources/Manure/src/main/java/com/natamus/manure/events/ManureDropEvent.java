/*
 * This is the latest source code of Manure.
 * Minecraft version: 1.19.3, mod version: 1.1.
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
import com.natamus.manure.items.RegistryHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.concurrent.CopyOnWriteArrayList;

@EventBusSubscriber
public class ManureDropEvent {
    private static CopyOnWriteArrayList<Animal> loadedAnimals = new CopyOnWriteArrayList<Animal>();

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.END)) {
            return;
        }

        if (e.getServer().getTickCount() % ConfigHandler.GENERAL.manureDropDelayTicks.get() != 0) {
            return;
        }

        for (Animal animal : loadedAnimals) {
            if (!animal.isAlive()) {
                loadedAnimals.remove(animal);
                continue;
            }

            animal.level.addFreshEntity(new ItemEntity(animal.level, animal.getX(), animal.getY()+0.5, animal.getZ(), new ItemStack(RegistryHandler.MANURE, 1)));
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent e) {
        Level level = e.getLevel();
        if (level.isClientSide) {
            return;
        }

        Entity entity = e.getEntity();
        if (entity instanceof Animal) {
            loadedAnimals.add((Animal)entity);
        }
    }

    @SubscribeEvent
    public void onEntityLeave(EntityLeaveLevelEvent e) {
        Level level = e.getLevel();
        if (level.isClientSide) {
            return;
        }

        Entity entity = e.getEntity();
        if (entity instanceof Animal) {
            loadedAnimals.remove((Animal)entity);
        }
    }
}