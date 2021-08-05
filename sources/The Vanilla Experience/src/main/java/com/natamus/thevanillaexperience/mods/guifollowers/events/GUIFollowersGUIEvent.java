/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.guifollowers.events;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.guifollowers.config.GUIFollowersConfigHandler;
import com.natamus.thevanillaexperience.mods.guifollowers.util.GUIFollowersVariables;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUIFollowersGUIEvent extends IngameGui {
	private static Minecraft mc;

	public GUIFollowersGUIEvent(Minecraft mc){
		super(mc);
		GUIFollowersGUIEvent.mc = mc; 
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGameOverlayEvent.Pre e){
		ElementType type = e.getType();
		if (type != ElementType.TEXT) {
			return;
		}

		FontRenderer fontRender = mc.font;
		MainWindow scaled = mc.getWindow();
		GL11.glPushMatrix();
		
		if (GUIFollowersVariables.activefollowers.size() > 0) {
			MatrixStack ms = new MatrixStack();
			
			int width = scaled.getGuiScaledWidth();
			
			String displaystring = GUIFollowersConfigHandler.GENERAL.followerListHeaderFormat.get();
			
			int stringWidth = fontRender.width(displaystring);
			
			Color colour = new Color(GUIFollowersConfigHandler.GENERAL.RGB_R.get(), GUIFollowersConfigHandler.GENERAL.RGB_G.get(), GUIFollowersConfigHandler.GENERAL.RGB_B.get(), 255);
			
			int xcoord = 0;
			int xoffset = 5;
			if (GUIFollowersConfigHandler.GENERAL.followerListPositionIsLeft.get()) {
				xcoord = 5;
			}
			else if (GUIFollowersConfigHandler.GENERAL.followerListPositionIsCenter.get()) {
				xcoord = (width/2) - (stringWidth/2);
			}
			else {
				xcoord = width - stringWidth - 5;
			}
			
			boolean drawnfirst = false;
			int heightoffset = GUIFollowersConfigHandler.GENERAL.followerListHeightOffset.get();
			
			ClientPlayerEntity player = mc.player;
			String playerdimension = WorldFunctions.getWorldDimensionName(player.getCommandSenderWorld());
			
			List<Entity> toremove = new ArrayList<Entity>();
			Iterator<Entity> it = new ArrayList<Entity>(GUIFollowersVariables.activefollowers).iterator();
			while (it.hasNext()) {
				Entity follower = it.next();
				String followerdimension = WorldFunctions.getWorldDimensionName(follower.getCommandSenderWorld());
				if (!playerdimension.equals(followerdimension)) {
					toremove.add(follower);
					continue;
				}
				
				if (!follower.isAlive() || follower instanceof TameableEntity == false) {
					toremove.add(follower);
					continue;
				}
				
				TameableEntity te = (TameableEntity)follower;
				if (te.isOrderedToSit()) {
					toremove.add(follower);
					continue;
				}
				
				String follower_string = follower.getName().getString();
				if (GUIFollowersConfigHandler.GENERAL.showFollowerHealth.get()) {
					LivingEntity le = (LivingEntity)follower;
					float currenthealth = le.getHealth();
					float maxhealth = le.getMaxHealth();
					
					int percenthealth = (int)((100/maxhealth)*currenthealth);
					if (percenthealth <= 0) {
						toremove.add(follower);
						continue;
					}
					
					String healthformat = GUIFollowersConfigHandler.GENERAL.followerHealthFormat.get();
					follower_string = follower_string + healthformat.replaceAll("<health>", percenthealth + "");
				}
				
				if (GUIFollowersConfigHandler.GENERAL.showFollowerDistance.get()) {
					Vector3d pvec = player.position();
					Vector3d fvec = follower.position();
					
					double distance = pvec.distanceTo(fvec);
					String distanceformat = GUIFollowersConfigHandler.GENERAL.followerDistanceFormat.get();
					follower_string = follower_string + distanceformat.replaceAll("<distance>", String.format("%.2f", distance));
				}
				
				int follower_stringWidth = fontRender.width(follower_string);
				
				if (GUIFollowersConfigHandler.GENERAL.followerListPositionIsCenter.get()) {
					xcoord = (width/2) - (follower_stringWidth/2) - xoffset;
				}
				else if (!GUIFollowersConfigHandler.GENERAL.followerListPositionIsLeft.get()) {
					xcoord = width - follower_stringWidth - 5 - xoffset;
				}
				
				if (!drawnfirst) {
					fontRender.draw(ms, displaystring, xcoord, heightoffset, colour.getRGB());
					drawnfirst = true;
				}
				
				heightoffset += 10;
				fontRender.draw(ms, follower_string, xcoord + xoffset, heightoffset, colour.getRGB());
			}
			
			if (toremove.size() > 0) {
				for (Entity etr : toremove) {
					GUIFollowersVariables.activefollowers.remove(etr);
				}
			}
		}
		
		GL11.glPopMatrix();
	}
}