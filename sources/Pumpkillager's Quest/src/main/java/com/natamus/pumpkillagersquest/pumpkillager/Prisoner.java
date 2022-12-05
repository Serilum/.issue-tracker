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

package com.natamus.pumpkillagersquest.pumpkillager;

import com.natamus.pumpkillagersquest.util.Data;
import com.natamus.pumpkillagersquest.util.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Prisoner {
    public static Villager createPrisoner(Level level, BlockPos pos, Player player, VillagerProfession profession, ItemStack headStack, ChatFormatting nameColour, boolean isKnown) {
        Villager prisoner = EntityType.VILLAGER.create(level);

        if (profession == null) {
            prisoner.setVillagerData(prisoner.getVillagerData().setType(VillagerType.SNOW));
        }
        else {
            prisoner.setVillagerData(prisoner.getVillagerData().setType(VillagerType.SNOW).setProfession(profession));
        }

        Vec3 prisonerVec = new Vec3(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        prisoner.setPos(prisonerVec.x, prisonerVec.y, prisonerVec.z);
        prisoner.setItemSlot(EquipmentSlot.HEAD, headStack);

        if (!isKnown) {
            prisoner.setCustomName(Component.translatable(Data.prisonerNameUnknown).withStyle(nameColour));
        }
        else {
            prisoner.setCustomName(Component.translatable(Data.prisonerNameKnown).withStyle(nameColour));
        }

        prisoner.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE));
        prisoner.getTags().add(Reference.MOD_ID + ".prisoner");
        prisoner.getTags().add(Reference.MOD_ID + ".justadded");

        Data.allPrisoners.get(level).add(prisoner);
        Data.prisonerPositions.put(prisoner, prisonerVec);

        return prisoner;
    }

    public static void checkForSpectralArrows(Level level, Villager pumpkillager, Player player) {
        if (player.getTags().contains(Reference.MOD_ID + ".extraarrows")) {
            return;
        }

        boolean foundSpectralArrowsInInventory = false;

        Inventory inventory = player.getInventory();
        for (int i=0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).getItem() instanceof SpectralArrowItem) {
                foundSpectralArrowsInInventory = true;
                break;
            }
        }

        if (foundSpectralArrowsInInventory) {
            return;
        }

        Conversations.sendJaxMessage(level, player, "How could you forget to bring the spectral arrows I gave you? You fool!", ChatFormatting.WHITE, 1000);
        Conversations.sendJaxMessage(level, player, "Luckily I've got some magic energy left. Take these.", ChatFormatting.WHITE, 3000, new ItemStack(Items.SPECTRAL_ARROW, 2), "give");

        player.getTags().add(Reference.MOD_ID + ".extraarrows");
    }
}
