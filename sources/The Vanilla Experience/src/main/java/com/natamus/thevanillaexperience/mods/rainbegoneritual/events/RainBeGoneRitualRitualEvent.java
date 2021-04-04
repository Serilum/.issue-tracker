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

package com.natamus.thevanillaexperience.mods.rainbegoneritual.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RainBeGoneRitualRitualEvent {
	private static Pair<Date, BlockPos> lastritual = null;
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		if (!world.isRaining()) {
			return;
		}
		
		if (!WorldFunctions.isOverworld(world)) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
        	BlockPos cp = e.getPos();
        	Block clickblock = world.getBlockState(cp).getBlock();
        	if (clickblock instanceof RotatedPillarBlock == false) {
        		return;
        	}
        	
        	BlockPos firepos = cp.up();
        	Block prefireblock = world.getBlockState(firepos).getBlock();
        	if (prefireblock.equals(Blocks.AIR)) {
        		List<BlockPos> cauldronposses = new ArrayList<BlockPos>();
        		
        		Iterator<BlockPos> it = BlockPos.getAllInBoxMutable(cp.getX()-1, cp.getY(), cp.getZ()-1, cp.getX()+1, cp.getY(), cp.getZ()+1).iterator();
        		while (it.hasNext()) {
        			BlockPos np = it.next();
        			if (world.getBlockState(np).getBlock().equals(Blocks.CAULDRON)) {
        				cauldronposses.add(np.toImmutable());
        			}
        		}
        		
        		if (cauldronposses.size() < 4) {
        			return;
        		}
        		
        		Vector3d firevec = new Vector3d(firepos.getX(), firepos.getY(), firepos.getZ());
        		
        		lastritual = new Pair<Date, BlockPos>(new Date(), firepos.toImmutable());
        		world.createExplosion(null, new DamageSource("explosion").setExplosion(), null, firevec.x, firevec.y, firevec.z, 3.0f, false, Explosion.Mode.NONE);
        		for (BlockPos cauldronpos : cauldronposses) {
        			BlockState cauldronstate = world.getBlockState(cauldronpos);
        			CauldronBlock cauldronblock = (CauldronBlock)cauldronstate.getBlock();
        			cauldronblock.setWaterLevel(world, cauldronpos, cauldronstate, 3);
        		}
        		
        		world.setBlockState(firepos, Blocks.AIR.getDefaultState());
        		world.setBlockState(cp, Blocks.AIR.getDefaultState());
        		
        		IWorldInfo info = world.getWorldInfo();
        		info.setRaining(false);
        	}
		}
	}
	
	@SubscribeEvent
	public void onExplosionDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		if (lastritual == null) {
			return;
		}
		
		DamageSource source = e.getSource();
		if (source.isExplosion()) {
			PlayerEntity player = (PlayerEntity)entity;
			BlockPos ppos = player.getPosition();
			
			Date now = new Date();
			Date lastdate = lastritual.getFirst();
			long ms = (now.getTime()-lastdate.getTime());
			if (ms > 1000) {
				lastritual = null;
				return;
			}
			
			BlockPos ritualpos = lastritual.getSecond();
			if (ppos.withinDistance(ritualpos, 10.0)) {
				e.setCanceled(true);
			}
		}
	}
}
