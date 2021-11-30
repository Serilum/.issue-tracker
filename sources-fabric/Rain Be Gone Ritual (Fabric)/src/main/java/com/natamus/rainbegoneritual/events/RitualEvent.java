/*
 * This is the latest source code of Rain Be Gone Ritual.
 * Minecraft version: 1.18.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Rain Be Gone Ritual ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.rainbegoneritual.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RitualEvent {
	private static Pair<Date, BlockPos> lastritual = null;
	
	public static InteractionResult onClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		if (!world.isRaining()) {
			return InteractionResult.PASS;
		}
		
		if (!WorldFunctions.isOverworld(world)) {
			return InteractionResult.PASS;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
        	BlockPos cp = BlockPosFunctions.getBlockPosFromHitResult(hitResult).below();
        	Block clickblock = world.getBlockState(cp).getBlock();
        	if (clickblock instanceof RotatedPillarBlock == false) {
        		return InteractionResult.PASS;
        	}
        	
        	BlockPos firepos = cp.above();
        	Block prefireblock = world.getBlockState(firepos).getBlock();
        	if (prefireblock.equals(Blocks.AIR)) {
        		List<BlockPos> cauldronposses = new ArrayList<BlockPos>();
        		
        		Iterator<BlockPos> it = BlockPos.betweenClosed(cp.getX()-1, cp.getY(), cp.getZ()-1, cp.getX()+1, cp.getY(), cp.getZ()+1).iterator();
        		while (it.hasNext()) {
        			BlockPos np = it.next();
        			if (world.getBlockState(np).getBlock() instanceof AbstractCauldronBlock) {
        				cauldronposses.add(np.immutable());
        			}
        		}
        		
        		if (cauldronposses.size() < 4) {
        			return InteractionResult.PASS;
        		}
        		
        		Vec3 firevec = new Vec3(firepos.getX(), firepos.getY(), firepos.getZ());
        		
        		lastritual = new Pair<Date, BlockPos>(new Date(), firepos.immutable());
        		world.explode(null, DamageSource.explosion((Explosion)null), null, firevec.x, firevec.y, firevec.z, 3.0f, false, Explosion.BlockInteraction.NONE);
        		for (BlockPos cauldronpos : cauldronposses) {
        			world.setBlock(cauldronpos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), 3);  		
        		}
        		
        		world.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
        		world.setBlockAndUpdate(cp, Blocks.AIR.defaultBlockState());
        		
        		LevelData info = world.getLevelData();
        		info.setRaining(false);
        	}
		}
		
		return InteractionResult.PASS;
	}
	
	public static float onExplosionDamage(Level world, Entity entity, DamageSource source, float damageAmount) {
		if (world.isClientSide) {
			return damageAmount;
		}
		
		if (entity instanceof Player == false) {
			return damageAmount;
		}
		
		if (lastritual == null) {
			return damageAmount;
		}
		
		if (source.isExplosion()) {
			Player player = (Player)entity;
			BlockPos ppos = player.blockPosition();
			
			Date now = new Date();
			Date lastdate = lastritual.getFirst();
			long ms = (now.getTime()-lastdate.getTime());
			if (ms > 1000) {
				lastritual = null;
				return damageAmount;
			}
			
			BlockPos ritualpos = lastritual.getSecond();
			if (ppos.closerThan(ritualpos, 10.0)) {
				return 0.0F;
			}
		}
		
		return damageAmount;
	}
}
