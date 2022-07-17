/*
 * This is the latest source code of Hidden Recipe Book.
 * Minecraft version: 1.19.0, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hidden Recipe Book ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hiddenrecipebook.events;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.hiddenrecipebook.config.ConfigHandler;
import com.natamus.hiddenrecipebook.util.Variables;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@EventBusSubscriber
public class BookGUIEvent {
    private static Minecraft mc = null;
    private static Date lastpress = null;
    private static Field imageButton_resourceLocation = ObfuscationReflectionHelper.findField(ImageButton.class, "f_94223_"); // resourceLocation
    private static ScreenEvent.Init.Post lastguipost = null;
    
	private static HashMap<String, ImageButton> recipe_buttons = new HashMap<String, ImageButton>();
	private static boolean showbook = !ConfigHandler.GENERAL.shouldHideRecipeBook.get();
	
    @SubscribeEvent
    public static void onGUIScreen(ScreenEvent.Init.Post e) {
    	String guiname = e.getScreen().getTitle().getString();
    	if (guiname.equalsIgnoreCase("crafting")) {
    		lastguipost = e;
			
    		List<GuiEventListener> widgets = e.getListenersList();
    		
    		ImageButton imagebutton = null;
    		for (GuiEventListener widget : widgets) {
    			if (widget instanceof ImageButton) {
    				imagebutton = (ImageButton)widget;
    				try {
    					ResourceLocation rl = (ResourceLocation) imageButton_resourceLocation.get(imagebutton);
    					if (rl.toString().toLowerCase().contains("recipe_button")) {
    	    				recipe_buttons.put(guiname, imagebutton);
    	    				break;
    					}
    				} catch (Exception ex) { continue; }
    			}
    		}
    		
    		if (showbook) {
    			if (imagebutton == null) {
					if (recipe_buttons.containsKey(guiname)) {
						ImageButton recipe_button = recipe_buttons.get(guiname);
						if (!widgets.contains(recipe_button)) {
							e.addListener(recipe_button);
						}
						
						recipe_button.visible = showbook;
					}
					return;
    			}
    			
    			imagebutton.visible = showbook;
    			return;
    		}
    		
    		if (imagebutton != null) {
    			imagebutton.visible = showbook;
    		}
    	}
    }
    
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onKey(ScreenEvent.KeyPressed e) {
		if (!ConfigHandler.GENERAL.allowRecipeBookToggleHotkey.get()) {
			return;
		}
		
		if (mc == null) {
			mc = Minecraft.getInstance();
		}
		
		if (mc.screen instanceof ChatScreen) {
			return;
		}
		
		if (e.getKeyCode() == Variables.hotkey.getKey().getValue()) {
			if (mc.player != null) {
				Date now = new Date();
				if (lastpress != null) {
					long ms = (now.getTime()-lastpress.getTime());
					if (ms < 1000) {
						return;
					}
				}
				
				lastpress = now;
				
				String message;
				if (showbook) {					
					showbook = false;
					message = "Now hiding recipe book button.";
				}
				else {
					showbook = true;
					message = "Now showing recipe book button.";
				}
				
				if (lastguipost != null) {
					Screen guiscreen = e.getScreen();
					String guiname = guiscreen.getTitle().getString();
					if (recipe_buttons.containsKey(guiname)) {
						ImageButton button = recipe_buttons.get(guiname);
						button.visible = showbook;
					}
				}
				
				if (ConfigHandler.GENERAL.showMessageOnRecipeBookToggle.get()) {
					StringFunctions.sendMessage(mc.player, message, ChatFormatting.DARK_GREEN);
				}
			}
		}
	}
}
