/*
 * This is the latest source code of GUI Compass.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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

		String format = ConfigHandler.guiCompassFormat.getValue();
		String toshow = "";

		if (format.contains("F")) {
			float degrees = Mth.wrapDegrees(player.getYRot());
			if (degrees < 0) {
				degrees += 360;
			}

			int facing = Math.round(degrees/45);
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