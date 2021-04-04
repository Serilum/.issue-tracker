/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.justmobheads.util;

import java.util.Arrays;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.HeadFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

public class MobHeads {
	static List<String> horsetypes = Arrays.asList("white", "creamy", "chestnut", "brown", "black", "gray", "dark_brown");
	static List<String> llamatypes = Arrays.asList("creamy", "white", "brown", "gray");
	static List<String> parrottypes = Arrays.asList("red", "blue", "green", "cyan", "gray");
	static List<String> rabbittypes = Arrays.asList("brown", "white", "black", "black_and_white", "gold", "salt_and_pepper");
	static List<String> cattypes = Arrays.asList("tabby", "tuxedo", "red", "siamese", "british_shorthair", "calico", "persian", "ragdoll", "white", "jellie", "black");
	
	public static ItemStack getMobHead(String mobname, Integer amount) {
		Pair<String, String> mobdata = HeadData.headdata.get(mobname);
		if (mobdata == null) {
			return null;
		}
		
		String headname = capitalizeFirst(mobname.replace("_", " ")) + " Head";
		String oldid = mobdata.getFirst();
		String texture = mobdata.getSecond();
		
		ItemStack mobhead = HeadFunctions.getTexturedHead(headname, texture, oldid, amount);
		
		return mobhead;
	}
	
	public static ItemStack getStandardHead(String headname) {
		ItemStack mobhead = new ItemStack(Items.PLAYER_HEAD, 1);
		String mob = headname.toLowerCase().split(" ")[0];
		if (mob.equals("creeper")) {
			mobhead = new ItemStack(Items.CREEPER_HEAD, 1);
		}
		else if (mob.equals("zombie")) {
			mobhead = new ItemStack(Items.ZOMBIE_HEAD, 1);
		}
		else if (mob.equals("skeleton")) {
			mobhead = new ItemStack(Items.SKELETON_SKULL, 1);
		}
		
		mobhead.setDisplayName(new StringTextComponent(headname));
		return mobhead;
	}
	
	public static String getName(Entity entity) {
		String entitystring = EntityFunctions.getEntityString(entity);
		String mobname = entitystring.split("\\[")[0].replace("Entity", "");
		mobname = String.join("_", mobname.split("(?<=.)(?=\\p{Lu})")).toLowerCase();
		
		if (entity instanceof CreeperEntity) {
			CreeperEntity creeper = (CreeperEntity)entity;
			if (creeper.isCharged()) {
				mobname = "charged_creeper";
			}
		}
		else if (entity instanceof CatEntity) {
			CatEntity cat = (CatEntity)entity;
			
			Integer type = cat.getCatType();
			if (type <= cattypes.size()) {
				mobname = cattypes.get(type) + "_cat";
			}
		}
		else if (entity instanceof HorseEntity) {
			HorseEntity horse = (HorseEntity)entity;
			CompoundNBT nbt = horse.serializeNBT();
			Integer type = nbt.getInt("Variant"); // horse.getHorseVariant();
			
			if (type >= 1024) {
				type -= 1024;
			}
			else if (type >= 768) {
				type -= 768;
			}
			else if (type >= 512) {
				type -= 512;
			}
			else if (type >= 256) {
				type -= 256;
			}
			mobname = horsetypes.get(type) + "_horse";
		}
		else if (entity instanceof LlamaEntity) {
			LlamaEntity llama = (LlamaEntity)entity;
			Integer type = llama.getVariant();
			mobname = llamatypes.get(type) + "_" + mobname;
		}
		else if (entity instanceof TraderLlamaEntity) {
			TraderLlamaEntity traderllama = (TraderLlamaEntity)entity;
			Integer type = traderllama.getVariant();
			mobname = llamatypes.get(type) + "_trader_" + mobname;		
		}
		else if (entity instanceof ParrotEntity) {
			ParrotEntity parrot = (ParrotEntity)entity;
			Integer type = parrot.getVariant();
			mobname = parrottypes.get(type) + "_parrot";
		}
		else if (entity instanceof RabbitEntity) {
			RabbitEntity rabbit = (RabbitEntity)entity;
			Integer type = rabbit.getRabbitType();
			if (type < rabbittypes.size()) {
				mobname = rabbittypes.get(type) + "_rabbit";
			}
			else if (type == 99) {
				mobname = "killer_rabbit";
			}
		}
		else if (entity instanceof SheepEntity) {
			SheepEntity sheep = (SheepEntity)entity;
			boolean checktype = true;
			if (sheep.hasCustomName()) {
				String name = sheep.getName().getString();
				if (name.equals("jeb_")) {
					mobname = "jeb_sheep";
					checktype = false;
				}
			}
			
			if (checktype) {
				DyeColor type = sheep.getFleeceColor();
				mobname = type.toString().toLowerCase() + "_sheep";
			}
		}
		else if (entity instanceof MooshroomEntity) {
			MooshroomEntity mooshroom = (MooshroomEntity)entity;
			if (mooshroom.getMooshroomType() == MooshroomEntity.Type.BROWN) {
				mobname = "brown_mooshroom";
			}	
		}
		else if (entity instanceof VillagerEntity) {
			VillagerEntity villager = (VillagerEntity)entity;
			
			VillagerData d = villager.getVillagerData();
			VillagerProfession prof = d.getProfession();
			if (prof.toString() != "none") {
				mobname = prof.toString();
			}
		}
		else if (entity instanceof ZombieVillagerEntity) {
			ZombieVillagerEntity zombievillager = (ZombieVillagerEntity)entity;

			VillagerData d = zombievillager.getVillagerData();
			VillagerProfession prof = d.getProfession();
			if (prof.toString() != "none") {
				mobname = "zombie_" + prof.toString();
			}	
		}
		
		return mobname.toLowerCase();
	}
	
	public static String capitalizeFirst(String string) {
		StringBuffer sb = new StringBuffer(string);
		for(int i=0; i < sb.length(); i++) {
			if(i == 0 || sb.charAt(i-1) == ' ') {
				sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
			}
		}
		return sb.toString();
	}
}
