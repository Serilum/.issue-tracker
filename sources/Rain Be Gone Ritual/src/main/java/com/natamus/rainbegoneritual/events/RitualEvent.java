/*
 * This is the latest source code of Rain Be Gone Ritual.
 * Minecraft version: 1.19.2, mod version: 2.0.
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

package com.natamus.rainbegoneritual.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RitualEvent {
	private static Pair<Date, BlockPos> lastritual = null;
	
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
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
        			return;
        		}
        		
        		Vec3 firevec = new Vec3(firepos.getX(), firepos.getY(), firepos.getZ());
        		
        		lastritual = new Pair<Date, BlockPos>(new Date(), firepos.immutable());
        		world.explode(null, new DamageSource("explosion").setExplosion(), null, firevec.x, firevec.y, firevec.z, 3.0f, false, Explosion.BlockInteraction.NONE);
        		for (BlockPos cauldronpos : cauldronposses) {
        			world.setBlock(cauldronpos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), 3);
        		}
        		
        		world.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
        		world.setBlockAndUpdate(cp, Blocks.AIR.defaultBlockState());
        		
        		LevelData info = world.getLevelData();
        		info.setRaining(false);
        	}
		}
	}
	
	@SubscribeEvent
	public void onExplosionDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Player == false) {
			return;
		}
		
		if (lastritual == null) {
			return;
		}
		
		DamageSource source = e.getSource();
		if (source.isExplosion()) {
			Player player = (Player)entity;
			BlockPos ppos = player.blockPosition();
			
			Date now = new Date();
			Date lastdate = lastritual.getFirst();
			long ms = (now.getTime()-lastdate.getTime());
			if (ms > 1000) {
				lastritual = null;
				return;
			}
			
			BlockPos ritualpos = lastritual.getSecond();
			if (ppos.closerThan(ritualpos, 10.0)) {
				e.setCanceled(true);
			}
		}
	}
}
