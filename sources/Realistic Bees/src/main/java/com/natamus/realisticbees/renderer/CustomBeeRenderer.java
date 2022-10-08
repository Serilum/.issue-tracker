/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.8.
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