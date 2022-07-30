/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.19.1, mod version: 5.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Just Mob Heads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.justmobheads.events;

import com.mojang.authlib.GameProfile;
import com.natamus.justmobheads.config.ConfigHandler;
import com.natamus.justmobheads.util.HeadData;
import com.natamus.justmobheads.util.MobHeads;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class HeadDropEvent {	
	public static void mobItemDrop(Level world, Entity entity, DamageSource damageSource) {
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler.onlyDropHeadsByChargedCreeper.getValue() || ConfigHandler.onlyDropHeadsByPlayerKill.getValue()) {
			Entity sourceentity = damageSource.getDirectEntity();
			if (ConfigHandler.onlyDropHeadsByChargedCreeper.getValue()) {
				if (sourceentity instanceof Creeper) {
					Creeper creeper = (Creeper)sourceentity;
					if (!creeper.isPowered()) {
						return;
					}
				}
				else {
					return;
				}
			}
			else if (ConfigHandler.onlyDropHeadsByPlayerKill.getValue()) {
				if (sourceentity instanceof Player == false) {
					return;
				}
			}
		}
		
		if (ConfigHandler.onlyAdultMobsDropTheirHead.getValue()) {
			if (entity instanceof TamableAnimal) {
				TamableAnimal te = (TamableAnimal)entity;
				if (te.isBaby()) {
					return;
				}
			}
		}
		
		String mobname = MobHeads.getName(entity);
		System.out.println(mobname);
		if (mobname == "") {
			return;
		}
		
		double extrachance = 0;
		Entity source = damageSource.getEntity();
		if (ConfigHandler.enableLootingEnchant.getValue() && source instanceof Player) {
			Integer looting = EnchantmentHelper.getMobLooting((LivingEntity)source);
			if (looting > 0) {
				extrachance = 0.025 + (looting/100);
			}
		}
		
		String headname = "";
		if (mobname.equals("creeper") || mobname.equals("zombie") || mobname.equals("skeleton")) {
			if (ConfigHandler.enableStandardHeads.getValue()) {
				headname = mobname.substring(0, 1).toUpperCase() + mobname.substring(1) + " Head";
			}
			else {
				return;
			}
		}
		
		double num = Math.random();
		if (ConfigHandler.mobSpecificDropChances.getValue()) {
			double chance = -1;
        	if (headname.equals("")) {
        		if (HeadData.headchances.containsKey(mobname)) {
        			chance = HeadData.headchances.get(mobname);
        		}
        	}
        	else {
        		chance = ConfigHandler.creeperSkeletonZombieDropChance.getValue();
        	}
	        
	        if (chance == -1) {
	        	if (num > ConfigHandler.overallDropChance.getValue() + extrachance) {
	        		return;
	        	}
	        }
	        else if (num > chance + extrachance) {
	        	return;
	        }
		}
		else if (num > ConfigHandler.overallDropChance.getValue() + extrachance) {
			return;
		}
		
		BlockPos pos = entity.blockPosition();
		
		ItemEntity mobhead;
		if (headname.equals("")) {
			ItemStack headstack = MobHeads.getMobHead(mobname, 1);
			if (headstack == null) {
				return;
			}
			
			mobhead = new ItemEntity(world, pos.getX(), pos.getY()+1, pos.getZ(), headstack);
		}
		else {
			mobhead = new ItemEntity(world,pos.getX(), pos.getY()+1, pos.getZ(), MobHeads.getStandardHead(headname));
		}
		
		world.addFreshEntity(mobhead);
	}
	
	public static boolean onPlayerHeadBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		Block block = state.getBlock();
		if (block instanceof SkullBlock || block instanceof WallSkullBlock) {
			if (player.isCreative()) {
				return true;
			}
			
			SkullBlockEntity sbe = (SkullBlockEntity)world.getBlockEntity(pos);
			if (sbe == null) {
				return true;
			}
			
			GameProfile profile = sbe.getOwnerProfile();
			if (profile == null) {
				return true;
			}
			
			UUID uuid = profile.getId();
			if (uuid == null) {
				return true;
			}
			
			String headid = uuid.toString();
			
			String correctheadname = "";
			for (String headname : HeadData.headdata.keySet()) {
				String headnameid = HeadData.headdata.get(headname).getFirst();
				if (headid.equals(headnameid)) {
					correctheadname = headname;
					break;
				}
			}
			
			ItemStack named_headstack = MobHeads.getMobHead(correctheadname, 1);
			if (named_headstack != null ) {
				world.destroyBlock(pos, false);
				
				world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY()+0.5, pos.getZ(), named_headstack));
				return false;
			}
		}
		
		return true;
	}
}
