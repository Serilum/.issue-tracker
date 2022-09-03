/*
 * This is the latest source code of Easy Elytra Takeoff.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.easyelytratakeoff.events;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@EventBusSubscriber
public class ElytraEvent {
	private static Method setFlag = ObfuscationReflectionHelper.findMethod(Entity.class, "m_20115_", int.class, boolean.class); // setSharedFlag
	
	@SubscribeEvent
	public void onFirework(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.FIREWORK_ROCKET)) {
			return;
		}
		
		Player player = e.getEntity();
		if (player.isFallFlying()) {
			return;
		}
		
		boolean foundelytra = false;
		Iterator<ItemStack> it = player.getArmorSlots().iterator();
		while (it.hasNext()) {
			ItemStack nis = it.next();
			if (nis.getItem() instanceof ElytraItem) {
				foundelytra = true;
				break;
			}
		}
		
		if (!foundelytra) {
			Collection<AttributeInstance> atrb = player.getAttributes().getSyncableAttributes();
			for (AttributeInstance ai : atrb) {
				for (AttributeModifier m : ai.getModifiers()) {
					String name = m.getName().toLowerCase();
					if (name.equals("flight modifier") || name.equals("elytra curio modifier")) {
						if (m.getAmount() >= 1.0) {
							foundelytra = true;
							break;
						}
					}
				}
				if (foundelytra) {
					break;
				}
			}
			if (!foundelytra) {
				return;
			}
		}
		
		try {
			setFlag.invoke(player, 7, true);
		} catch (Exception ex) { 
			return;
		}
		
		FireworkRocketEntity efr1 = new FireworkRocketEntity(world, hand, player);
		FireworkRocketEntity efr2 = new FireworkRocketEntity(world, hand, player);
		FireworkRocketEntity efr3 = new FireworkRocketEntity(world, hand, player);
		FireworkRocketEntity efr4 = new FireworkRocketEntity(world, hand, player);

		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( 10 ); }
	            catch (InterruptedException ie)  {}
	        	
	        	world.addFreshEntity(efr1);
	    		try {
	    			setFlag.invoke(player, 7, true);
	    		} catch (Exception ex) { 
	    			return;
	    		}
	    		new Thread( new Runnable() {
	    	    	public void run()  {
	    	        	try  { Thread.sleep( 10 ); }
	    	            catch (InterruptedException ie)  {}
	    	        	
	    	        	world.addFreshEntity(efr2);
	    	    		try {
	    	    			setFlag.invoke(player, 7, true);
	    	    		} catch (Exception ex) { 
	    	    			return;
	    	    		}
	    	    		new Thread( new Runnable() {
	    	    	    	public void run()  {
	    	    	        	try  { Thread.sleep( 10 ); }
	    	    	            catch (InterruptedException ie)  {}
	    	    	        	
	    	    	        	world.addFreshEntity(efr3);
	    	    	    		try {
	    	    	    			setFlag.invoke(player, 7, true);
	    	    	    		} catch (Exception ex) { 
	    	    	    			return;
	    	    	    		}
	    	    	    		new Thread( new Runnable() {
	    	    	    	    	public void run()  {
	    	    	    	        	try  { Thread.sleep( 30 ); }
	    	    	    	            catch (InterruptedException ie)  {}
	    	    	    	        	
	    	    	    	        	world.addFreshEntity(efr4);
	    	    	    	    	}
	    	    	    	    } ).start();
	    	    	    	}
	    	    	    } ).start();
	    	    	}
	    	    } ).start();
	    	}
	    } ).start();
	}
}
