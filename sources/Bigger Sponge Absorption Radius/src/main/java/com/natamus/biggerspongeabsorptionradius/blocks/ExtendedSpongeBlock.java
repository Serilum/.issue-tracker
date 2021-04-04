/*
 * This is the latest source code of Bigger Sponge Absorption Radius.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bigger Sponge Absorption Radius ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.biggerspongeabsorptionradius.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.natamus.collective.functions.BlockPosFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ExtendedSpongeBlock extends Block {
	private static final List<Material> spongematerials = new ArrayList<Material>(Arrays.asList(Material.SPONGE));
	
	public ExtendedSpongeBlock(Properties properties) {
		super(properties);
	}
	
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (oldState.getBlock() != state.getBlock()) {
			this.tryAbsorb(worldIn, pos);
		}
	}
	
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		this.tryAbsorb(worldIn, pos);
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
	}
	
	protected void tryAbsorb(World worldIn, BlockPos pos) {
		if (this.absorb(worldIn, pos)) {
			worldIn.setBlockState(pos, Blocks.WET_SPONGE.getDefaultState(), 2);
			worldIn.playEvent(2001, pos, Block.getStateId(Blocks.WATER.getDefaultState()));
		}
	}

	private boolean absorb(World worldIn, BlockPos pos) {
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
				BlockPos blockpos1 = blockpos.offset(direction);
				BlockState blockstate = worldIn.getBlockState(blockpos1);
				Block block = blockstate.getBlock();
				FluidState ifluidstate = worldIn.getFluidState(blockpos1);
				Material material = blockstate.getMaterial();
				if (ifluidstate.isTagged(FluidTags.WATER) || blockstate.getMaterial().equals(Material.SPONGE)) {
					if (block instanceof IBucketPickupHandler && ((IBucketPickupHandler)block).pickupFluid(worldIn, blockpos1, blockstate) != Fluids.EMPTY) {
						++i;
						if (j < absorpdistance) {
							queue.add(new Tuple<>(blockpos1, j + 1));
						}
					} 
					else if (block instanceof FlowingFluidBlock) {
						worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
						++i;
						if (j < absorpdistance) {
							queue.add(new Tuple<>(blockpos1, j + 1));
						}
					}
					else if (material == Material.OCEAN_PLANT || material == Material.SEA_GRASS) {
						TileEntity tileentity = block.hasTileEntity(blockstate) ? worldIn.getTileEntity(blockpos1) : null;
						spawnDrops(blockstate, worldIn, blockpos1, tileentity);
						worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
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
					worldIn.setBlockState(spongepos, Blocks.WET_SPONGE.getDefaultState());
				}
			}
			return true;
		}
		return false;
	}
}
