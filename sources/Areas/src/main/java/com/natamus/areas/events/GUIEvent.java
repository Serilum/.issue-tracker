/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.areas.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.areas.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.UUID;

public class GUIEvent extends Gui {
	public static String hudmessage = "";
	public static String rgb = "";
	public static int gopacity = 0;
	
	private static String currentmessage = "";
	private static String currentrandom = "";
	
	private Minecraft mc;
	
	public GUIEvent(Minecraft mcIn, ItemRenderer itemRenderer) {
		super(mcIn, itemRenderer);
		mc = mcIn;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGuiOverlayEvent.Post e){
		if (!hudmessage.equals("")) {
			Font fontRender = mc.font;
			Window scaled = mc.getWindow();
			int width = scaled.getGuiScaledWidth();

			double stringWidth = fontRender.width(hudmessage);
			
			if (gopacity <= 0) {
				gopacity = 0;
				hudmessage = "";
				return;
			}
			else if (gopacity > 255) {
				gopacity = 255;
			}
			
			Color colour = new Color(ConfigHandler.HUD.HUD_RGB_R.get(), ConfigHandler.HUD.HUD_RGB_G.get(), ConfigHandler.HUD.HUD_RGB_B.get(), gopacity);
			if (rgb != "") {
				String[] rgbs = rgb.split(",");
				if (rgbs.length == 3) {
					try {
						int r = Integer.parseInt(rgbs[0]);
						if (r < 0) {
							r = 0;
						}
						else if (r > 255) {
							r = 255;
						}
						
						int g = Integer.parseInt(rgbs[1]);
						if (g < 0) {
							g = 0;
						}
						else if (g > 255) {
							g = 255;
						}
						
						int b = Integer.parseInt(rgbs[2]);
						if (b < 0) {
							b = 0;
						}
						else if (b > 255) {
							b = 255;
						}
						
						colour = new Color(r, g, b, gopacity);
					}
					catch(IllegalArgumentException ex) {
						rgb = "";
						hudmessage = "";
					}
				}
			}
			
			PoseStack posestack = e.getPoseStack();
			posestack.pushPose();
			
			RenderSystem.enableBlend(); // GL11.glEnable(GL11.GL_BLEND);
			RenderSystem.blendFunc(0x302, 0x303); //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			float modifier = (ConfigHandler.HUD.HUD_FontSizeScaleModifier.get().floatValue() + 0.5F);
			posestack.scale(modifier, modifier, modifier);
			
			fontRender.draw(posestack, hudmessage, (int)(Math.round((width / 2) / modifier) - stringWidth/2), ConfigHandler.HUD.HUDMessageHeightOffset.get(), colour.getRGB());
			
			posestack.popPose();
			
			if (currentmessage != hudmessage) {
				currentmessage = hudmessage;
				setHUDFade(UUID.randomUUID().toString());
			}
		}
	}
	
	public void setHUDFade(String random) {
		currentrandom = random;
		
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( ConfigHandler.HUD.HUDMessageFadeDelayMs.get() ); }
	            catch (InterruptedException ie)  {}
	        	
	        	if (currentrandom.equals(random)) {
	        		startFadeOut(random);
	        	}
	        }
	    } ).start();
	}
	
	public void startFadeOut(String random) {
		if (!currentrandom.equals(random)) {
			return;
		}
		
		if (gopacity < 0) {
			hudmessage = "";
			rgb = "";
			return;
		}
		
		gopacity -= 10;
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( 50 ); }
	            catch (InterruptedException ie)  {}
	        	startFadeOut(random);
	        }
	    } ).start();		
	}
}