/*
 * This is the latest source code of Smaller Nether Portals.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.smallernetherportals.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.natamus.smallernetherportals.util.Util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PortalEvent {
	private static Map<String, BlockPos> toposes = new HashMap<String, BlockPos>();
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
        	BlockPos clickpos = e.getPos();
        	
        	int obsidiancount = 0;
        	Iterator<BlockPos> it = BlockPos.betweenClosedStream(clickpos.getX()-3, clickpos.getY()-3, clickpos.getZ()-3, clickpos.getX()+3, clickpos.getY()+3, clickpos.getZ()+3).iterator();
        	while (it.hasNext()) {
        		BlockPos np = it.next();
        		if (world.getBlockState(np).getBlock().equals(Blocks.OBSIDIAN)) {
        			obsidiancount+=1;
        		}
        	}
			
        	if (obsidiancount >= 6) {
				new Thread( new Runnable() {
			    	public void run()  {
			        	try  { Thread.sleep( 10 ); }
			            catch (InterruptedException ie)  {}
			        	
			        	BlockPos topos = clickpos;
			        	
			        	Boolean foundportal = false;
			        	Iterator<BlockPos> it = BlockPos.betweenClosedStream(clickpos.getX()-1, clickpos.getY()-1, clickpos.getZ()-1, clickpos.getX()+1, clickpos.getY()+1, clickpos.getZ()+1).iterator();
			        	while (it.hasNext()) {
			        		BlockPos np = it.next();
			        		Block bsblock = world.getBlockState(np).getBlock();
			        		if (bsblock instanceof NetherPortalBlock) {
			        			foundportal = true;
			        		}
			        		else if (bsblock.equals(Blocks.FIRE)) {
			        			if (Util.isAir(world.getBlockState(np.below(1)))) {
			        				topos = np.below(1).immutable();
			        			}
			        			else if (Util.isAir(world.getBlockState(np.below(2)))) {
			        				topos = np.below(2).immutable();
			        			}
			        			else {
			        				topos = np.immutable();
			        			}
			        		}
			        	}
			        	
			        	if (!foundportal) {
			        		if (Util.isAir(world.getBlockState(topos))) {
			        			Util.processSmallerPortal(world, topos.immutable());
			        		}
			        	}
			    	}
			    } ).start();
        	}
		}
	}
	
	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent e) {
		Player player = e.getEntity();
		Level world = player.level;
		if (world.isClientSide) {
			return;
		}
		
		BlockPos pos = player.blockPosition();
		Block block = world.getBlockState(pos).getBlock();
		
		if (block instanceof NetherPortalBlock) {
			return;
		}
		
		String playername = player.getName().getString();
		
		if (!toposes.containsKey(playername)) {
			BlockPos foundpos = Util.findPortalAround(world, pos);
			if (foundpos != null) {
				List<BlockPos> frontblocks = Util.getFrontBlocks(world, foundpos);
				Util.setObsidian(world, frontblocks);
				
	        	toposes.put(playername, frontblocks.get(frontblocks.size()-1).above().immutable());
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		String playername = player.getName().getString();
		if (!toposes.containsKey(playername)) {
			return;
		}
		
		BlockPos topos = toposes.get(playername); 
		
		player.setPortalCooldown(); // reset time until portal
		player.teleportTo(((double)topos.getX())+0.5, (double)topos.getY(), ((double)topos.getZ())+0.5);
		toposes.remove(playername);
	}
}
