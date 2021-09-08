/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.17.1, mod version: 2.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Realistic Bees ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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