/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.guicompass.events;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.thevanillaexperience.mods.guicompass.config.GUICompassConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUICompassGUIEvent extends Gui {
	private static Minecraft mc;

	public GUICompassGUIEvent(Minecraft mc){
		super(mc);
		GUICompassGUIEvent.mc = mc; 
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGameOverlayEvent.Pre e) {
		ElementType type = e.getType();
		if (type != ElementType.TEXT) {
			return;
		}
		
		if (GUICompassConfigHandler.GENERAL.mustHaveCompassInInventory.get()) {
			boolean found = false;
			Inventory inv = mc.player.getInventory();
			for (int n = 0; n <= 35; n++) {
				if (inv.getItem(n).getItem().equals(Items.COMPASS)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return;
			}
		}
		
		String coordinates = getCoordinates();

		Font fontRender = mc.font;
		Window scaled = mc.getWindow();
		int width = scaled.getGuiScaledWidth();
		
		int stringWidth = fontRender.width(coordinates);
		
		Color colour = new Color(GUICompassConfigHandler.GENERAL.RGB_R.get(), GUICompassConfigHandler.GENERAL.RGB_G.get(), GUICompassConfigHandler.GENERAL.RGB_B.get(), 255);
			
		PoseStack posestack = e.getMatrixStack();
		posestack.pushPose();
		
		int xcoord = 0;
		if (GUICompassConfigHandler.GENERAL.compassPositionIsLeft.get()) {
			xcoord = 5;
		}
		else if (GUICompassConfigHandler.GENERAL.compassPositionIsCenter.get()) {
			xcoord = (width/2) - (stringWidth/2);
		}
		else {
			xcoord = width - stringWidth - 5;
		}

		fontRender.draw(posestack, coordinates, xcoord, GUICompassConfigHandler.GENERAL.compassHeightOffset.get(), colour.getRGB());
		
		posestack.popPose();
	}

	private static List<String> direction = new ArrayList<String>(Arrays.asList("S", "SW", "W", "NW", "N", "NE", "E", "SE"));
	private static String getCoordinates() {
		LocalPlayer player = mc.player;
		BlockPos ppos = player.blockPosition();
		
		int yaw = (int)player.getYHeadRot();
		if (yaw < 0) {
			yaw += 360;
		}
		yaw+=22;
		yaw%=360;

		int facing = yaw/45;
		if (facing < 0) {
			facing = facing * -1;
		}
		
		return direction.get(facing) + ": " + ppos.getX() + ", " + ppos.getY() + ", " + ppos.getZ();
	}
}