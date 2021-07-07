/*
 * This is the latest source code of Easy Elytra Takeoff.
 * Minecraft version: 1.16.5, mod version: 1.8.
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ElytraEvent {
	private static Method setFlag = null;
	
	@SubscribeEvent
	public void onFirework(PlayerInteractEvent.RightClickItem e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.FIREWORK_ROCKET)) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
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
			Collection<ModifiableAttributeInstance> atrb = player.getAttributes().getSyncableAttributes();
			for (ModifiableAttributeInstance ai : atrb) {
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
		
		if (setFlag == null) {
			for (Method method : Entity.class.getDeclaredMethods()) {
				if (method.toString().contains("setFlag") || method.toString().contains("setSharedFlag")) {
					setFlag = method;
					break;
				}
			}
			if (setFlag == null) {
				return;
			}
			setFlag.setAccessible(true);
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
