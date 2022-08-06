/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.realisticbees.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.realisticbees.config.ConfigHandler;

import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.Bee;

public class CustomBeeRenderer extends BeeRenderer {
	public CustomBeeRenderer(EntityRendererProvider.Context p_i226033_1_) {
		super(p_i226033_1_);
		shadowRadius *= ConfigHandler.GENERAL.beeSizeModifier.get().floatValue();
	}
	   
	@Override
	protected void scale(Bee p_225620_1_, PoseStack p_225620_2_, float p_225620_3_) {
		float scale = ConfigHandler.GENERAL.beeSizeModifier.get().floatValue();
		p_225620_2_.scale(scale, scale, scale);
	}
}