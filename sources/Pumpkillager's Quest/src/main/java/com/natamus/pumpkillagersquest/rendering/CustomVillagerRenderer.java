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

package com.natamus.pumpkillagersquest.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class CustomVillagerRenderer extends VillagerRenderer {
	public CustomVillagerRenderer(EntityRendererProvider.Context p_i226033_1_) {
		super(p_i226033_1_);
	}
	   
	@Override
	protected void scale(@NotNull Villager villager, @NotNull PoseStack pMatrixStack, float pPartialTickTime) {
		if (!Util.isPumpkillager(villager)) {
			return;
		}

		ItemStack footStack = villager.getItemBySlot(EquipmentSlot.FEET);
		if (!(footStack.getItem().equals(Items.BARRIER))) {
			return;
		}

		String scaleFloatString = footStack.getHoverName().getString();

		float scale;
		try {
			scale = Float.parseFloat(scaleFloatString);
		}
		catch (NumberFormatException ex) { return; }

		pMatrixStack.scale(scale, scale, scale);
	}
}