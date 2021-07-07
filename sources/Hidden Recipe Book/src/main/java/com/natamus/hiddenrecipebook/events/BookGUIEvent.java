/*
 * This is the latest source code of Hidden Recipe Book.
 * Minecraft version: 1.16.5, mod version: 2.1.
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BookGUIEvent {
    private static Minecraft mc = null;
    private static Date lastpress = null;
    private static Field imageButton_resourceLocation = null;
    private static GuiScreenEvent.InitGuiEvent.Post lastguipost = null;
    
	
	private static HashMap<String, ImageButton> recipe_buttons = new HashMap<String, ImageButton>();
	private static boolean showbook = !ConfigHandler.GENERAL.shouldHideRecipeBook.get();
	
    @SubscribeEvent
    public static void onGUIScreen(GuiScreenEvent.InitGuiEvent.Post e) {
    	String guiname = e.getGui().getTitle().getString();
    	if (guiname.equalsIgnoreCase("crafting")) {
    		lastguipost = e;
    		if (imageButton_resourceLocation == null) {
				for (Field field : ImageButton.class.getDeclaredFields()) {
					if (field.toString().contains("resourceLocation") || field.toString().contains("resourceLocation")) {
						imageButton_resourceLocation = field;
						break;
					}
				}
				if (imageButton_resourceLocation == null) {
					return;
				}
				imageButton_resourceLocation.setAccessible(true);
			}
			
    		List<Widget> widgets = e.getWidgetList();
    		
    		ImageButton imagebutton = null;
    		for (Widget widget : widgets) {
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
							e.addWidget(recipe_button);
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
	public void onKey(GuiScreenEvent.KeyboardKeyPressedEvent e) {
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
					Screen guiscreen = e.getGui();
					String guiname = guiscreen.getTitle().getString();
					if (recipe_buttons.containsKey(guiname)) {
						ImageButton button = recipe_buttons.get(guiname);
						button.visible = showbook;
					}
				}
				
				if (ConfigHandler.GENERAL.showMessageOnRecipeBookToggle.get()) {
					StringFunctions.sendMessage(mc.player, message, TextFormatting.DARK_GREEN);
				}
			}
		}
	}
}
