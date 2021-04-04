/*
 * This is the latest source code of Recipe Commands.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Recipe Commands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.recipecommands.cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.recipecommands.util.Recipes;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CommandRecipes {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("recipes")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayerEntity)
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				sendUsage(source);
				return 1;
			})
			.then(Commands.argument("recipe", ResourceLocationArgument.resourceLocation()).suggests(SuggestionProviders.ALL_RECIPES)
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				try {
					sendRecipe(command);
				}
				catch (CommandSyntaxException ex) {
					StringFunctions.sendMessage(source, "Unable to find recipe.", TextFormatting.RED);
				}
				return 1;
			}))
		);
    	dispatcher.register(Commands.literal("rec")
    			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayerEntity)
    			.executes((command) -> {
    				CommandSource source = command.getSource();
    				
    				sendUsage(source);
    				return 1;
    			})
    			.then(Commands.argument("recipe", ResourceLocationArgument.resourceLocation()).suggests(SuggestionProviders.ALL_RECIPES)
    			.executes((command) -> {
    				CommandSource source = command.getSource();
    				
    				try {
    					sendRecipe(command);
    				}
    				catch (CommandSyntaxException ex) {
    					StringFunctions.sendMessage(source, "Unable to find recipe.", TextFormatting.RED);
    				}
    				return 1;
    			}))
    		);
    }
    
    private static void sendUsage(CommandSource source) {
    	StringFunctions.sendMessage(source, "Recipe Commands Usage:", TextFormatting.DARK_GREEN);
    	StringFunctions.sendMessage(source, " /rec <recipe>", TextFormatting.DARK_GREEN);
    }
    
    @SuppressWarnings("unchecked")
	private static void sendRecipe(CommandContext<CommandSource> command) throws CommandSyntaxException {
    	CommandSource source = command.getSource();
    	PlayerEntity player = source.asPlayer();
    	World world = player.getEntityWorld();
    	if (world.isRemote) {
    		return;
    	}
    	
    	IRecipe<?> recipe = ResourceLocationArgument.getRecipe(command, "recipe");
    	ResourceLocation recipelocation = recipe.getId();
    	String recipename = recipelocation.getPath().toString();
    	
    	List<String> items = new ArrayList<String>();
    	HashMap<String, Integer> itemcount = new HashMap<String, Integer>();
    	
    	List<Ingredient> ingredients = recipe.getIngredients();
    	for (Ingredient ingredient : ingredients) {
    		ItemStack[] possiblestacks = ingredient.getMatchingStacks();
    		if (possiblestacks.length <= 0) {
    			continue;
    		}
    		
    		ItemStack itemstack = possiblestacks[0];
			Item item = itemstack.getItem();
			String itemname = item.toString();
    		if (possiblestacks.length > 1 && !itemname.equalsIgnoreCase("cobblestone")) {
    			Set<ResourceLocation> tags = item.getTags();
    			if (tags.size() > 0) {
    				ResourceLocation tag = tags.iterator().next();
    				itemname = tag.getPath();
    			}
    		}
    		
    		itemname = StringFunctions.capitalizeEveryWord(itemname);
    		
			if (items.contains(itemname)) {
				int currentcount = itemcount.get(itemname);
				itemcount.put(itemname, currentcount+1);
				continue;
			}
			
			items.add(itemname);
			itemcount.put(itemname, 1);
    	}
    	
    	Collections.sort(items);
    	
    	IRecipeSerializer<?> serializer = recipe.getSerializer();
    	ResourceLocation srl = serializer.getRegistryName();

    	List<String> pattern = new ArrayList<String>();
    	HashMap<String, String> itemkeys = new HashMap<String, String>();
    	
    	String shape = "shapeless";
    	if (srl != null) {
	    	if (srl.toString().equalsIgnoreCase("minecraft:crafting_shaped")) {
	    		shape = "shaped";
	    		
	    		if (Recipes.jsonrecipes.containsKey(recipename)) {
	    			String jsonrecipe = Recipes.jsonrecipes.get(recipename);
	    			Gson gson = new Gson();
	    			Map<String, ?> map = gson.fromJson(jsonrecipe, Map.class);
	    			String rawjson = map.toString();
	    			
	    			pattern = (List<String>) map.get("pattern");
	    			
	    			String[] spl1 = rawjson.split("key=\\{");
	    			if (spl1.length > 1) {
	    				String keys = spl1[1].split("\\}},")[0];
	    				for (String keyraw : keys.split(", ")) {
	    					String[] keyspl = keyraw.split("=[\\{,\\[]");
	    					if (keyspl.length <= 1) {
	    						continue;
	    					}
	    					
	    					String key = keyspl[0];
	    					if (Recipes.replacekeys.containsKey(key)) {
	    						key = Recipes.replacekeys.get(key);
	    					}
	    					String itemvalue = keyspl[1].split(":")[1].replaceAll("\\}", "");
	    					itemkeys.put(itemvalue, key);
	    				}
	    			}
	    		}
	    	}
    	}
    	
    	ItemStack output = recipe.getRecipeOutput();
    	String outputname = output.getItem().toString();
    	outputname = StringFunctions.capitalizeEveryWord(outputname.replace("_", " "));
		
    	StringFunctions.sendMessage(source, outputname + " has a " + shape + " recipe.", TextFormatting.DARK_GREEN, true);
    	StringFunctions.sendMessage(source, " Ingredients:", TextFormatting.DARK_GREEN);
    	for (String itemname : items) {
    		int count = itemcount.get(itemname);
    		String todisplayname = itemname;
    		if (shape.equalsIgnoreCase("shaped")) {
    			String shapeditemname = itemname.toLowerCase().replace(" ", "_").split("\\/")[0];
    			if (itemkeys.containsKey(shapeditemname)) {
    				String itemkey = itemkeys.get(shapeditemname);
    				todisplayname += " (" + itemkey + ")";
    			}
    		}
    		
    		todisplayname = todisplayname.replace("_", " ");
    		
        	StringFunctions.sendMessage(source, "  " + count + "x " + todisplayname, TextFormatting.DARK_GREEN);
    	}
    	
    	if (shape.equalsIgnoreCase("shaped")) { 		
    		if (pattern.size() > 0) {
    			StringFunctions.sendMessage(source, " Pattern:", TextFormatting.DARK_GREEN);
    			
        		for (String line : pattern) {
        			for (String toreplace : Recipes.replacekeys.keySet()) {
        				line = line.replaceAll(toreplace, Recipes.replacekeys.get(toreplace));
        			}
        			StringFunctions.sendMessage(source, "  " + line.replace(" ", "_"), TextFormatting.DARK_GREEN);
        		}
    		}
    	}
    }
}