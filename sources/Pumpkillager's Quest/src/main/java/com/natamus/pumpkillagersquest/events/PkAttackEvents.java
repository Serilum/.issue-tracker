/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 2.3.
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

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.pumpkillagersquest.pumpkillager.Conversations;
import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PkAttackEvents {
    @SubscribeEvent
    public void onArrowShoot(ArrowNockEvent e) {
        Level level = e.getLevel();
        if (level.isClientSide) {
            return;
        }

        if (Data.allPumpkillagers.get(level).size() == 0) {
            return;
        }

        Player player = e.getEntity();
        BlockPos pPos = player.blockPosition();

        boolean nearPumpkillager = false;
        for (Villager pumpkillager : Data.allPumpkillagers.get(level)) {
            if (pumpkillager.getTags().contains(Reference.MOD_ID + ".isweakened")) {
                continue;
            }

            if (BlockPosFunctions.withinDistance(pPos, pumpkillager.blockPosition(), 30)) {
                nearPumpkillager = true;
                break;
            }
        }

        if (nearPumpkillager) {
            ItemStack bowStack = e.getBow();
            ItemStack projectileStack = player.getProjectile(bowStack);
            if (!projectileStack.isEmpty()) {
                if (projectileStack.getItem() instanceof SpectralArrowItem) {
                    if (!player.getTags().contains(Reference.MOD_ID + ".aimforfeet")) {
                        Conversations.sendJaxMessage(level, player, player.getName().getString() + "! Aim for his feet!", ChatFormatting.GRAY, 10);
                        player.getTags().add(Reference.MOD_ID + ".aimforfeet");
                    }
                }
            }
        }
    }
}
