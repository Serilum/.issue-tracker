/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.3, mod version: 2.3.
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

package com.natamus.pumpkillagersquest.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS} whenever a player summons the
 * pumpkillager. The event is not cancelable and does not have a result.
 */
public class PumpkillagerSummonEvent extends Event {

    private final Player summoner;
    private final Villager pumpkillager;
    private final BlockPos pos;
    private final Type type;

    public PumpkillagerSummonEvent(Player summoner, Villager pumpkillager, BlockPos pos, Type type) {
        this.summoner = summoner;
        this.pumpkillager = pumpkillager;
        this.pos = pos.immutable();
        this.type = type;
    }

    /**
     * Gets the player, who summoned the pumpkillager.
     */
    public Player getSummoner() {
        return summoner;
    }

    /**
     * Gets the pumpkillager entity.
     */
    public Villager getPumpkillager() {
        return pumpkillager;
    }

    /**
     * Gets the center block pos of the summoning ritual.
     */
    public BlockPos getPos() {
        return pos;
    }

    /**
     * Gets the type of pumpkillager that is summoned.
     */
    public Type getType() {
        return type;
    }

    public enum Type {
        INITIAL_SUMMON,
        POST_RITUAL,
        FINAL_BOSS
    }
}