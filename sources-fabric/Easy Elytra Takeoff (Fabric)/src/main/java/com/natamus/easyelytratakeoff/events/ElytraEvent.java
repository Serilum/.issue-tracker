/*
 * This is the latest source code of Easy Elytra Takeoff.
 * Minecraft version: 1.17.x, mod version: 2.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Easy Elytra Takeoff ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.easyelytratakeoff.events;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ElytraEvent {
	private static Method setFlag = null;
	
	public static InteractionResultHolder<ItemStack> onFirework(Player player, Level world, InteractionHand hand) {
		ItemStack handstack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(handstack);
		}
		
		if (!handstack.getItem().equals(Items.FIREWORK_ROCKET)) {
			return InteractionResultHolder.pass(handstack);
		}
		
		if (player.isFallFlying()) {
			return InteractionResultHolder.pass(handstack);
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
				return InteractionResultHolder.pass(handstack);
			}
		}
		
		if (setFlag == null) {
			for (Method method : Entity.class.getDeclaredMethods()) {
				if (method.toString().contains("setSharedFlag(") || method.toString().contains("m_20115_(")) {
					setFlag = method;
					break;
				}
			}
			if (setFlag == null) {
				return InteractionResultHolder.pass(handstack);
			}
			setFlag.setAccessible(true);
		}
		
		try {
			setFlag.invoke(player, 7, true);
		} catch (Exception ex) { 
			return InteractionResultHolder.pass(handstack);
		}
		
		FireworkRocketEntity efr1 = new FireworkRocketEntity(world, handstack, player);
		FireworkRocketEntity efr2 = new FireworkRocketEntity(world, handstack, player);
		FireworkRocketEntity efr3 = new FireworkRocketEntity(world, handstack, player);
		FireworkRocketEntity efr4 = new FireworkRocketEntity(world, handstack, player);
		FireworkRocketEntity efr5 = new FireworkRocketEntity(world, handstack, player);

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
	    	    	    	    		try {
	    	    	    	    			setFlag.invoke(player, 7, true);
	    	    	    	    		} catch (Exception ex) { 
	    	    	    	    			return;
	    	    	    	    		}
	    	    	    	    		new Thread( new Runnable() {
	    	    	    	    	    	public void run()  {
	    	    	    	    	        	try  { Thread.sleep( 50 ); }
	    	    	    	    	            catch (InterruptedException ie)  {}
	    	    	    	    	        	
	    	    	    	    	        	world.addFreshEntity(efr5);
	    	    	    	    	    	}
	    	    	    	    	    } ).start();
	    	    	    	    	}
	    	    	    	    } ).start();
	    	    	    	}
	    	    	    } ).start();
	    	    	}
	    	    } ).start();
	    	}
	    } ).start();
		
		return InteractionResultHolder.success(handstack);
	}
}
