/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.17.1, mod version: 2.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Areas ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas.events;

import java.awt.Color;
import java.util.UUID;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.areas.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUIEvent extends Gui {
	public static String hudmessage = "";
	public static String rgb = "";
	public static int gopacity = 0;
	
	private static String currentmessage = "";
	private static String currentrandom = "";
	
	private Minecraft mc;
	
	public GUIEvent(Minecraft mcIn) {
		super(mcIn);
		mc = mcIn;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGameOverlayEvent.Post e){
		ElementType type = e.getType();
		if (type != ElementType.TEXT) {
			return;
		}

		if (hudmessage != "") {
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
			
			PoseStack posestack = e.getMatrixStack();
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