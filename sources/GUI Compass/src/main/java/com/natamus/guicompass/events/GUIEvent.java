/*
 * This is the latest source code of GUI Compass.
 * Minecraft version: 1.19.2, mod version: 2.4.
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

package com.natamus.guicompass.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.guicompass.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CompassItem;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GUIEvent extends Gui {
	private static Minecraft mc;

	public GUIEvent(Minecraft mc, ItemRenderer itemRenderer){
		super(mc, itemRenderer);
		GUIEvent.mc = mc; 
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGuiOverlayEvent.Pre e) {
		if (ConfigHandler.GENERAL.mustHaveCompassInInventory.get()) {
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
		
		Color colour = new Color(ConfigHandler.GENERAL.RGB_R.get(), ConfigHandler.GENERAL.RGB_G.get(), ConfigHandler.GENERAL.RGB_B.get(), 255);
			
		PoseStack posestack = e.getPoseStack();
		posestack.pushPose();
		
		int xcoord;
		if (ConfigHandler.GENERAL.compassPositionIsLeft.get()) {
			xcoord = 5;
		}
		else if (ConfigHandler.GENERAL.compassPositionIsCenter.get()) {
			xcoord = (width/2) - (stringWidth/2);
		}
		else {
			xcoord = width - stringWidth - 5;
		}

		fontRender.draw(posestack, coordinates, xcoord, ConfigHandler.GENERAL.compassHeightOffset.get(), colour.getRGB());
		
		posestack.popPose();
	}

	private static List<String> direction = Arrays.asList("S", "SW", "W", "NW", "N", "NE", "E", "SE", "S");
	private static String getCoordinates() {
		Entity player = mc.getCameraEntity();
		BlockPos ppos = player.blockPosition();

		String format = ConfigHandler.GENERAL.guiCompassFormat.get();
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