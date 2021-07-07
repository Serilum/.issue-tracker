/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.realisticbees.events;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.realisticbees.config.RealisticBeesConfigHandler;
import com.natamus.thevanillaexperience.mods.realisticbees.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RealisticBeesBeeEvent {
	private static HashMap<PlayerEntity, Integer> stung_players = new HashMap<PlayerEntity, Integer>();
	private static HashMap<PlayerEntity, Date> last_sting_player = new HashMap<PlayerEntity, Date>();
	private static HashMap<LivingEntity, Vector3d> stingerless_bees = new HashMap<LivingEntity, Vector3d>();
	
	@SubscribeEvent
	public void onBeeSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof BeeEntity == false) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".ignorebee")) {
			return;
		}
		
		BlockPos entitypos = entity.blockPosition();
		if (!world.hasChunk(MathHelper.floor(entitypos.getX()) >> 4, MathHelper.floor(entitypos.getZ()) >> 4)) {
			return;
		}
		
		int extrabees = RealisticBeesConfigHandler.GENERAL.extraBeeSpawnsPerBee.get();
		if (extrabees == 0) {
			return;
		}
		
		Vector3d beevec = entity.position();
		for (int i = 0; i < extrabees; i++) {
			BeeEntity newbee = EntityType.BEE.create(world);
			newbee.setLevel(world);
			newbee.setPos(beevec.x, beevec.y, beevec.z);
			newbee.addTag(Reference.MOD_ID + ".ignorebee");
			world.addFreshEntity(newbee);
		}
		
		entity.addTag(Reference.MOD_ID + ".ignorebee");
	}

	@SubscribeEvent
	public void onEntityDamageTaken(LivingHurtEvent e) {
		Entity target = e.getEntity();
		World world = target.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity truesource = e.getSource().getEntity();
		if (truesource instanceof BeeEntity == false) {
			return;
		}
		
		boolean targetisplayer = false;
		if (target instanceof PlayerEntity) {
			targetisplayer = true;
		}
		
		if (!RealisticBeesConfigHandler.GENERAL.beesDieFromStingingPlayer.get()) {
			if (targetisplayer) {
				return;
			}
		}
		
		if (!RealisticBeesConfigHandler.GENERAL.beesDieFromStingingMob.get()) {
			if (!targetisplayer) {
				return;
			}
		}
		
		Double chance = GlobalVariables.random.nextDouble();
		if (chance > RealisticBeesConfigHandler.GENERAL.chanceBeeLeavesItsStinger.get()) {
			return;
		}
		
		EntityFunctions.addPotionEffect(truesource, Effects.UNLUCK, RealisticBeesConfigHandler.GENERAL.timeInSecondsBeeWithoutStingerDies.get()*1000);
		
		if (targetisplayer) {
			PlayerEntity player = (PlayerEntity)target;
			
			int timesstung = 1;
			if (stung_players.containsKey(player)) {
				timesstung = stung_players.get(player)+1;
			}
			
			String times = "";
			if (timesstung > 1) {
				times = " " + timesstung + " times";
			}
			
			Date now = new Date();
			last_sting_player.put(player, now);
			stung_players.put(player, timesstung);
			
			if (RealisticBeesConfigHandler.GENERAL.sendStungPlayerWithStingerAMessage.get()) {
				StringFunctions.sendMessage(player, "You have been stung" + times + " with the stinger left behind! You can try and get it out by using some shears as pliers.", TextFormatting.YELLOW);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if(entity instanceof BeeEntity) {
			if (entity instanceof LivingEntity == false) {
				return;
			}
			
			LivingEntity le = (LivingEntity)entity;
			EffectInstance badluck = le.getEffect(Effects.UNLUCK);
			if (badluck == null) {
				return;
			}
			
			int ticksleft = badluck.getDuration();
			if (ticksleft < 5) {
				stingerless_bees.remove(le);
				le.hurt(DamageSource.STARVE, Float.MAX_VALUE);
				return;
			}
			
			Vector3d beevec = le.position();
			if (!stingerless_bees.containsKey(le)) {
				stingerless_bees.put(le, beevec);
				return;
			}
			
			Vector3d lastvec = stingerless_bees.get(le);
			le.teleportTo(lastvec.x, beevec.y, lastvec.z);
		}
		else if (entity instanceof PlayerEntity) {
			if (entity instanceof LivingEntity == false) {
				return;
			}
			
			PlayerEntity player = (PlayerEntity)entity;
			if (stung_players.containsKey(player)) {
				LivingEntity le = (LivingEntity)entity;
				EffectInstance poison = le.getEffect(Effects.POISON);
				if (poison == null) {
					Date now = new Date();
					Date last_sting = last_sting_player.get(player);
					long ms = (now.getTime()-last_sting.getTime());
					if (ms >= RealisticBeesConfigHandler.GENERAL.timeInSecondsStingerPumpsPoison.get()*1000) {
						StringFunctions.sendMessage(player, "All stingers have stopped pumping poison.", TextFormatting.DARK_GREEN);
						stung_players.remove(player);
						return;
					}
					
					EntityFunctions.addPotionEffect(le, Effects.POISON, 5000);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onStingerPull(PlayerInteractEvent.RightClickItem e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		if (stung_players.containsKey(player)) {
			ItemStack hand = e.getItemStack();
			if (hand.getItem() instanceof ShearsItem == false) {
				return;
			}
			
			Double chance = GlobalVariables.random.nextDouble();
			if (chance > RealisticBeesConfigHandler.GENERAL.chanceBeeStingerIsPulledOut.get()) {
				StringFunctions.sendMessage(player, "You failed to get the stinger out!", TextFormatting.YELLOW);
				return;
			}
			
			int timesstung = stung_players.get(player);
			int newamount = timesstung-1;
			
			if (newamount == 0) {
				StringFunctions.sendMessage(player, "You successfully took all the stingers out.", TextFormatting.DARK_GREEN);
				stung_players.remove(player);
				return;
			}
			
			String are = "is 1";
			if (newamount > 1) {
				are = "are " + newamount;
			}
			
			StringFunctions.sendMessage(player, "You took out a stinger! There " + are + " left.", TextFormatting.YELLOW);
			stung_players.put(player, newamount);
		}
	}
}
