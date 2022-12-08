/*
 * This is the latest source code of GUI Followers.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.guifollowers.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.guifollowers.config.ConfigHandler;
import com.natamus.guifollowers.util.Variables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GUIEvent extends Gui {
	private static Minecraft mc;

	public GUIEvent(Minecraft mc, ItemRenderer itemRenderer){
		super(mc, itemRenderer);
		GUIEvent.mc = mc; 
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGuiOverlayEvent.Pre e){
		Font fontRender = mc.font;
		Window scaled = mc.getWindow();
		PoseStack posestack = e.getPoseStack();
		posestack.pushPose();
		
		if (Variables.activefollowers.size() > 0) {
			int width = scaled.getGuiScaledWidth();
			
			String displaystring = ConfigHandler.GENERAL.followerListHeaderFormat.get();
			
			int stringWidth = fontRender.width(displaystring);
			
			Color colour = new Color(ConfigHandler.GENERAL.RGB_R.get(), ConfigHandler.GENERAL.RGB_G.get(), ConfigHandler.GENERAL.RGB_B.get(), 255);
			
			int xcoord = 0;
			int xoffset = 5;
			if (ConfigHandler.GENERAL.followerListPositionIsLeft.get()) {
				xcoord = 5;
			}
			else if (ConfigHandler.GENERAL.followerListPositionIsCenter.get()) {
				xcoord = (width/2) - (stringWidth/2);
			}
			else {
				xcoord = width - stringWidth - 5;
			}
			
			boolean drawnfirst = false;
			int heightoffset = ConfigHandler.GENERAL.followerListHeightOffset.get();
			
			LocalPlayer player = mc.player;
			String playerdimension = WorldFunctions.getWorldDimensionName(player.getCommandSenderWorld());
			
			List<Entity> toremove = new ArrayList<Entity>();
			Iterator<Entity> it = new ArrayList<Entity>(Variables.activefollowers).iterator();
			while (it.hasNext()) {
				Entity follower = it.next();
				String followerdimension = WorldFunctions.getWorldDimensionName(follower.getCommandSenderWorld());
				if (!playerdimension.equals(followerdimension)) {
					toremove.add(follower);
					continue;
				}
				
				if (!follower.isAlive() || follower instanceof TamableAnimal == false) {
					toremove.add(follower);
					continue;
				}
				
				TamableAnimal te = (TamableAnimal)follower;
				if (te.isInSittingPose()) {
					toremove.add(follower);
					continue;
				}
				
				String follower_string = follower.getName().getString();
				if (ConfigHandler.GENERAL.showFollowerHealth.get()) {
					LivingEntity le = (LivingEntity)follower;
					float currenthealth = le.getHealth();
					float maxhealth = le.getMaxHealth();
					
					int percenthealth = (int)((100/maxhealth)*currenthealth);
					if (percenthealth <= 0) {
						toremove.add(follower);
						continue;
					}
					
					String healthformat = ConfigHandler.GENERAL.followerHealthFormat.get();
					follower_string = follower_string + healthformat.replaceAll("<health>", percenthealth + "");
				}
				
				if (ConfigHandler.GENERAL.showFollowerDistance.get()) {
					Vec3 pvec = player.position();
					Vec3 fvec = follower.position();
					
					double distance = pvec.distanceTo(fvec);
					String distanceformat = ConfigHandler.GENERAL.followerDistanceFormat.get();
					follower_string = follower_string + distanceformat.replaceAll("<distance>", String.format("%.2f", distance));
				}
				
				int follower_stringWidth = fontRender.width(follower_string);
				
				if (ConfigHandler.GENERAL.followerListPositionIsCenter.get()) {
					xcoord = (width/2) - (follower_stringWidth/2) - xoffset;
				}
				else if (!ConfigHandler.GENERAL.followerListPositionIsLeft.get()) {
					xcoord = width - follower_stringWidth - 5 - xoffset;
				}
				
				if (!drawnfirst) {
					fontRender.draw(posestack, displaystring, xcoord, heightoffset, colour.getRGB());
					drawnfirst = true;
				}
				
				heightoffset += 10;
				fontRender.draw(posestack, follower_string, xcoord + xoffset, heightoffset, colour.getRGB());
			}
			
			if (toremove.size() > 0) {
				for (Entity etr : toremove) {
					Variables.activefollowers.remove(etr);
				}
			}
		}
		
		posestack.popPose();
	}
}