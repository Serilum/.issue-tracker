/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.8.
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

package com.natamus.realisticbees.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.*;
import com.natamus.realisticbees.config.ConfigHandler;
import com.natamus.realisticbees.util.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@EventBusSubscriber
public class BeeEvent {
	private static HashMap<Player, Integer> stung_players = new HashMap<Player, Integer>();
	private static HashMap<Player, Date> last_sting_player = new HashMap<Player, Date>();
	private static HashMap<LivingEntity, Vec3> stingerless_bees = new HashMap<LivingEntity, Vec3>();

	@SubscribeEvent
	public static void onBeeCheckSpawn(LivingSpawnEvent.SpecialSpawn e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}

		Entity entity = e.getEntity();
		if (!(entity instanceof Bee)) {
			return;
		}

		entity.addTag("SpawnReason." + e.getSpawnReason().name());
	}

	@SubscribeEvent
	public static void onBeeSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (!(entity instanceof Bee)) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".ignorebee")) {
			return;
		}
		entity.addTag(Reference.MOD_ID + ".ignorebee");

		if (tags.contains("SpawnReason.BUCKET") || tags.contains("SpawnReason.SPAWN_EGG")) {
			return;
		}
		
		BlockPos entitypos = entity.blockPosition();
		if (!world.hasChunk(Mth.floor(entitypos.getX()) >> 4, Mth.floor(entitypos.getZ()) >> 4)) {
			return;
		}
		
		int extrabees = ConfigHandler.GENERAL.extraBeeSpawnsPerBee.get();
		if (extrabees == 0) {
			return;
		}
		
		if (!(world instanceof ServerLevel)) {
			return;
		}
		
		Bee bee = (Bee)entity;
		if (bee.isBaby()) {
			return;
		}
		
		TaskFunctions.enqueueImmediateTask(world, () -> {
        	Vec3 beevec = entity.position();

    		ServerLevel serverworld = (ServerLevel)world;
    		for (int i = 0; i < extrabees; i++) {
    			Bee newbee = EntityType.BEE.create(world);
    			newbee.level = world;
    			newbee.setPos(beevec.x, beevec.y, beevec.z);
    			newbee.addTag(Reference.MOD_ID + ".ignorebee");
    			SpawnEntityFunctions.spawnEntityOnNextTick(serverworld, newbee);
    		}
        }, false);
	}
	
	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.level;
		if (world.isClientSide) {
			return;
		}
		
		if (!(entity instanceof Player)) {
			return;
		}
		
		Player player = (Player)entity;
		stung_players.remove(player);
	}

	@SubscribeEvent
	public static void onEntityDamageTaken(LivingHurtEvent e) {
		Entity target = e.getEntity();
		Level world = target.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity truesource = e.getSource().getEntity();
		if (!(truesource instanceof Bee)) {
			return;
		}
		
		boolean targetisplayer = target instanceof Player;

		if (!ConfigHandler.GENERAL.beesDieFromStingingPlayer.get()) {
			if (targetisplayer) {
				return;
			}
		}
		
		if (!ConfigHandler.GENERAL.beesDieFromStingingMob.get()) {
			if (!targetisplayer) {
				return;
			}
		}
		
		double chance = GlobalVariables.random.nextDouble();
		if (chance > ConfigHandler.GENERAL.chanceBeeLeavesItsStinger.get()) {
			return;
		}
		
		EntityFunctions.addPotionEffect(truesource, MobEffects.UNLUCK, ConfigHandler.GENERAL.timeInSecondsBeeWithoutStingerDies.get()*1000);
		
		if (targetisplayer) {
			Player player = (Player)target;
			
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
			
			if (ConfigHandler.GENERAL.sendStungPlayerWithStingerAMessage.get()) {
				StringFunctions.sendMessage(player, "You have been stung" + times + " with the stinger left behind! You can try and get it out by using some shears as pliers.", ChatFormatting.YELLOW);
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if(entity instanceof Bee) {
			if (!(entity instanceof LivingEntity)) {
				return;
			}
			
			LivingEntity le = (LivingEntity)entity;
			MobEffectInstance badluck = le.getEffect(MobEffects.UNLUCK);
			if (badluck == null) {
				return;
			}
			
			int ticksleft = badluck.getDuration();
			if (ticksleft < 5) {
				stingerless_bees.remove(le);
				le.hurt(DamageSource.STARVE, Float.MAX_VALUE);
				return;
			}
			
			Vec3 beevec = le.position();
			if (!stingerless_bees.containsKey(le)) {
				stingerless_bees.put(le, beevec);
				return;
			}
			
			Vec3 lastvec = stingerless_bees.get(le);
			le.teleportTo(lastvec.x, beevec.y, lastvec.z);
		}
		else if (entity instanceof Player) {
			if (!(entity instanceof LivingEntity)) {
				return;
			}
			
			Player player = (Player)entity;
			if (stung_players.containsKey(player)) {
				LivingEntity le = (LivingEntity)entity;
				MobEffectInstance poison = le.getEffect(MobEffects.POISON);
				if (poison == null) {
					Date now = new Date();
					Date last_sting = last_sting_player.get(player);
					long ms = (now.getTime()-last_sting.getTime());
					if (ms >= ConfigHandler.GENERAL.timeInSecondsStingerPumpsPoison.get()*1000) {
						StringFunctions.sendMessage(player, "All stingers have stopped pumping poison.", ChatFormatting.DARK_GREEN);
						stung_players.remove(player);
						return;
					}
					
					EntityFunctions.addPotionEffect(le, MobEffects.POISON, 5000);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onStingerPull(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Player player = e.getEntity();
		if (stung_players.containsKey(player)) {
			ItemStack hand = e.getItemStack();
			if (!(hand.getItem() instanceof ShearsItem)) {
				return;
			}
			
			double chance = GlobalVariables.random.nextDouble();
			if (chance > ConfigHandler.GENERAL.chanceBeeStingerIsPulledOut.get()) {
				StringFunctions.sendMessage(player, "You failed to get the stinger out!", ChatFormatting.YELLOW);
				return;
			}
			
			int timesstung = stung_players.get(player);
			int newamount = timesstung-1;
			
			if (newamount == 0) {
				StringFunctions.sendMessage(player, "You successfully took all the stingers out.", ChatFormatting.DARK_GREEN);
				stung_players.remove(player);
				return;
			}
			
			String are = "is 1";
			if (newamount > 1) {
				are = "are " + newamount;
			}
			
			StringFunctions.sendMessage(player, "You took out a stinger! There " + are + " left.", ChatFormatting.YELLOW);
			stung_players.put(player, newamount);
		}
	}
}
