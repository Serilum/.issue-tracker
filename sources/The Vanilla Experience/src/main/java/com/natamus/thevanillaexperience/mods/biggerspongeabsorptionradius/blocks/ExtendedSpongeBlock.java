/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.biggerspongeabsorptionradius.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.natamus.collective.functions.BlockPosFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

@SuppressWarnings("deprecation")
public class ExtendedSpongeBlock extends Block {
	private static final List<Material> spongematerials = new ArrayList<Material>(Arrays.asList(Material.SPONGE));
	
	public ExtendedSpongeBlock(Properties properties) {
		super(properties);
	}
	
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (oldState.getBlock() != state.getBlock()) {
			this.tryAbsorb(worldIn, pos);
		}
	}
	
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		this.tryAbsorb(worldIn, pos);
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
	}
	
	protected void tryAbsorb(Level worldIn, BlockPos pos) {
		if (this.absorb(worldIn, pos)) {
			worldIn.setBlock(pos, Blocks.WET_SPONGE.defaultBlockState(), 2);
			worldIn.levelEvent(2001, pos, Block.getId(Blocks.WATER.defaultBlockState()));
		}
	}

	private boolean absorb(Level worldIn, BlockPos pos) {
		List<BlockPos> spongepositions = BlockPosFunctions.getBlocksNextToEachOtherMaterial(worldIn, pos, spongematerials);
		int spongecount = spongepositions.size();
		
		int absorpdistance = 6 * spongecount; // default 6
		int maxcount = 64 * spongecount; // default 64
		
		Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
		queue.add(new Tuple<>(pos, 0));
		int i = 0;
		
		while(!queue.isEmpty()) {
			Tuple<BlockPos, Integer> tuple = queue.poll();
			BlockPos blockpos = tuple.getA();
			int j = tuple.getB();
			
			for(Direction direction : Direction.values()) {
				BlockPos blockpos1 = blockpos.relative(direction);
				BlockState blockstate = worldIn.getBlockState(blockpos1);
				Block block = blockstate.getBlock();
				FluidState ifluidstate = worldIn.getFluidState(blockpos1);
				Material material = blockstate.getMaterial();
				if (ifluidstate.is(FluidTags.WATER) || blockstate.getMaterial().equals(Material.SPONGE)) {
					if (blockstate.getBlock() instanceof BucketPickup && !((BucketPickup)blockstate.getBlock()).pickupBlock(worldIn, blockpos1, blockstate).isEmpty()) {
						++i;
						if (j < absorpdistance) {
							queue.add(new Tuple<>(blockpos1, j + 1));
						}
					} 
					else if (block instanceof LiquidBlock) {
						worldIn.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
						++i;
						if (j < absorpdistance) {
							queue.add(new Tuple<>(blockpos1, j + 1));
						}
					}
					else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
						BlockEntity tileentity = blockstate.hasBlockEntity() ? worldIn.getBlockEntity(blockpos1) : null;
						dropResources(blockstate, worldIn, blockpos1, tileentity);
						worldIn.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
						++i;
						if (j < absorpdistance) {
							queue.add(new Tuple<>(blockpos1, j + 1));
						}
					}
				}
			}
			
			if (i > maxcount) {
				break;
			}
		}
		
		if (i > 0) {
			for (BlockPos spongepos : spongepositions) {
				if (worldIn.getBlockState(spongepos).getMaterial().equals(Material.SPONGE)) {
					worldIn.setBlockAndUpdate(spongepos, Blocks.WET_SPONGE.defaultBlockState());
				}
			}
			return true;
		}
		return false;
	}
}
