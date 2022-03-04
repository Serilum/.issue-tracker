/*
 * This is the latest source code of GUI Compass.
 * Minecraft version: 1.19.x, mod version: 2.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of GUI Compass ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.guicompass.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.guicompass.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GUIEvent {
	private static final Minecraft mc = Minecraft.getInstance();

	public static void renderOverlay(PoseStack posestack, float tickDelta){
		if (ConfigHandler.mustHaveCompassInInventory.getValue()) {
			if (!(mc.player.getOffhandItem().getItem() instanceof CompassItem)) {
				boolean found = false;
				Inventory inv = mc.player.getInventory();
				for (int n = 0; n <= 35; n++) {
					if (inv.getItem(n).getItem() instanceof CompassItem) {
						found = true;
						break;
					}
				}
				if (!found) {
					return;
				}
			}
		}
		
		String coordinates = getCoordinates();

		Font fontRender = mc.font;
		Window scaled = mc.getWindow();
		int width = scaled.getGuiScaledWidth();
		
		int stringWidth = fontRender.width(coordinates);
		
		Color colour = new Color(ConfigHandler.RGB_R.getValue(), ConfigHandler.RGB_G.getValue(), ConfigHandler.RGB_B.getValue(), 255);
			
		posestack.pushPose();
		
		int xcoord = 0;
		if (ConfigHandler.compassPositionIsLeft.getValue()) {
			xcoord = 5;
		}
		else if (ConfigHandler.compassPositionIsCenter.getValue()) {
			xcoord = (width/2) - (stringWidth/2);
		}
		else {
			xcoord = width - stringWidth - 5;
		}

		fontRender.draw(posestack, coordinates, xcoord, ConfigHandler.compassHeightOffset.getValue(), colour.getRGB());
		
		posestack.popPose();
	}

	private static List<String> direction = Arrays.asList("S", "SW", "W", "NW", "N", "NE", "E", "SE", "S");
	private static String getCoordinates() {
		Entity player = mc.getCameraEntity();
		BlockPos ppos = player.blockPosition();

		float degrees = Mth.wrapDegrees(player.getYRot());
		if (degrees < 0) {
			degrees += 360;
		}

		int facing = Math.round(degrees/45);

		String format = ConfigHandler.guiCompassFormat.getValue();
		String toshow = "";

		if (format.contains("F")) {
			toshow += direction.get(facing) + ": ";
		}
		if (format.contains("X")) {
			toshow += ppos.getX() + ", ";
		}
		if (format.contains("Y")) {
			toshow += ppos.getY() + ", ";
		}
		if (format.contains("Z")) {
			toshow += ppos.getZ() + ", ";
		}

		return toshow.substring(0, toshow.length() - 2);
	}
}