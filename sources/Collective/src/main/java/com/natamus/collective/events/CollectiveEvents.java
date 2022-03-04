/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.2, mod version: 4.13.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.events;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.SpawnEntityFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.collective.objects.SAMObject;
import com.natamus.collective.util.Reference;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@EventBusSubscriber
public class CollectiveEvents {
	public static HashMap<ServerLevel, List<Entity>> entitiesToSpawn = new HashMap<ServerLevel, List<Entity>>();
	public static HashMap<ServerLevel, HashMap<Entity, Entity>> entitiesToRide = new HashMap<ServerLevel, HashMap<Entity, Entity>>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		entitiesToSpawn.put(serverworld, new ArrayList<Entity>());
		entitiesToRide.put(serverworld, new HashMap<Entity, Entity>());
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
			
		ServerLevel serverworld = (ServerLevel)world;
		if (entitiesToSpawn.get(serverworld).size() > 0) {
			Entity tospawn = entitiesToSpawn.get(serverworld).get(0);
			
			serverworld.addFreshEntityWithPassengers(tospawn);
			
			if (entitiesToRide.get(world).containsKey(tospawn)) {
				Entity rider = entitiesToRide.get(world).get(tospawn);
				
				rider.startRiding(tospawn);
				
				entitiesToRide.get(world).remove(tospawn);
			}
			
			entitiesToSpawn.get(world).remove(0);
		}
	}
	
	@SubscribeEvent
	public void onMobSpawnerSpawn(LivingSpawnEvent.SpecialSpawn e) {
		Level Level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (Level == null) {
			return;
		}
		
		if (e.getSpawner() != null) {
			if (!e.isCanceled()) {
				e.getEntity().addTag(Reference.MOD_ID + ".spawner");
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinLevel(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (!(entity instanceof LivingEntity)) {
			return;
		}
		
		if (RegisterMod.shouldDoCheck) {
			if (entity instanceof Player) {
				RegisterMod.joinWorldProcess(world, (Player)entity);
			}
		}
		
		if (e.isCanceled()) {
			return;
		}
		
		if (GlobalVariables.samobjects.isEmpty()) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".checked")) {
			return;
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		EntityType<?> entitytype = entity.getType();
		if (entitytype == null || !GlobalVariables.activesams.contains(entitytype)) {
			return;	
		}
		
		boolean isspawner = tags.contains(Reference.MOD_ID + ".spawner");
		
		List<SAMObject> possibles = new ArrayList<SAMObject>();
		for (SAMObject samobject : GlobalVariables.samobjects) {
			if (samobject == null) {
				continue;
			}

			if (samobject.fromtype == null) {
				continue;
			}
			if (samobject.fromtype.equals(entitytype)) {
				if ((samobject.spawner && !isspawner) || (!samobject.spawner && isspawner)) {
					continue;
				}
				possibles.add(samobject);
			}
		}
		
		int size = possibles.size();
		if (size == 0) {
			return;
		}
		
		for (SAMObject sam : possibles) {
			double num = GlobalVariables.random.nextDouble();
			if (num > sam.chance) {
				continue;
			}
			
			Vec3 evec = entity.position();
			if (sam.surface) {
				if (!BlockPosFunctions.isOnSurface(world, evec)) {
					continue;
				}
			}
			
			Entity to = sam.totype.create(world);
			//to.setWorld(Level);
			to.setPos(evec.x, evec.y, evec.z);
			
			boolean ignoremainhand = false;
			if (sam.helditem != null) {
				if (to instanceof LivingEntity) {
					LivingEntity le = (LivingEntity)to;
					if (!le.getMainHandItem().getItem().equals(sam.helditem)) {
						le.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(sam.helditem, 1));
						ignoremainhand = true;
					}
				}
			}
			
			boolean ride = false;
			if (EntityFunctions.isHorse(to) && sam.rideable) {
				AbstractHorse ah = (AbstractHorse)to;
				ah.setTamed(true);
				to = ah;
				
				ride = true;
			}
			else {
				if (ConfigHandler.COLLECTIVE.transferItemsBetweenReplacedEntities.get()) {
					EntityFunctions.transferItemsBetweenEntities(entity, to, ignoremainhand);
				}
			}
			
			if (!(world instanceof ServerLevel)) {
				return;
			}
			
			ServerLevel serverworld = (ServerLevel)world;
			
			if (ride) {
				SpawnEntityFunctions.startRidingEntityOnNextTick(serverworld, to, entity); //entity.startRiding(to);
			}
			else {
				e.setCanceled(true);
			}
			
			to.addTag(Reference.MOD_ID + ".checked");
			SpawnEntityFunctions.spawnEntityOnNextTick(serverworld, to); //serverworld.addFreshEntityWithPassengers(to);

			break;
		}
	}
}