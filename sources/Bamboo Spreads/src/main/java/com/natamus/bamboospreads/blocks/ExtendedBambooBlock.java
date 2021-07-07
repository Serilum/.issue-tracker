/*
 * This is the latest source code of Bamboo Spreads.
 * Minecraft version: 1.16.5, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bamboo Spreads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.bamboospreads.blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.natamus.bamboospreads.config.ConfigHandler;
import com.natamus.bamboospreads.variables.BlockVariables;
import com.natamus.collective.functions.BlockFunctions;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BambooLeaves;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.block.AbstractBlock.Properties;

public class ExtendedBambooBlock extends BambooBlock {
	public ExtendedBambooBlock(Properties p_i49998_1_) {
		super(p_i49998_1_);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(LEAVES, BambooLeaves.NONE).setValue(STAGE, Integer.valueOf(0)));
	}
	
	  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
	      builder.add(AGE, LEAVES, STAGE);
	   }

	   /**
	    * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
	    */
	   public AbstractBlock.OffsetType getOffsetType() {
	      return AbstractBlock.OffsetType.XZ;
	   }

	   public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
	      return true;
	   }

	   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      VoxelShape voxelshape = state.getValue(LEAVES) == BambooLeaves.LARGE ? LARGE_SHAPE : SMALL_SHAPE;
	      Vector3d vector3d = state.getOffset(worldIn, pos);
	      return voxelshape.move(vector3d.x, vector3d.y, vector3d.z);
	   }

	   public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
	      return false;
	   }

	   public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      Vector3d vector3d = state.getOffset(worldIn, pos);
	      return COLLISION_SHAPE.move(vector3d.x, vector3d.y, vector3d.z);
	   }

	   @Nullable
	   public BlockState getStateForPlacement(BlockItemUseContext context) {
	      FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
	      if (!fluidstate.isEmpty()) {
	         return null;
	      } else {
	         BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().below());
	         if (blockstate.is(BlockTags.BAMBOO_PLANTABLE_ON)) {
	            if (blockstate.is(BlockVariables.SPREADING_BAMBOO_SAPLING_BLOCK)) {
	               return this.defaultBlockState().setValue(AGE, Integer.valueOf(0));
	            } else if (blockstate.is(BlockVariables.SPREADING_BAMBOO_BLOCK)) {
	               int i = blockstate.getValue(AGE) > 0 ? 1 : 0;
	               return this.defaultBlockState().setValue(AGE, Integer.valueOf(i));
	            } else {
	               BlockState blockstate1 = context.getLevel().getBlockState(context.getClickedPos().above());
	               return !blockstate1.is(BlockVariables.SPREADING_BAMBOO_BLOCK) && !blockstate1.is(BlockVariables.SPREADING_BAMBOO_SAPLING_BLOCK) ? BlockVariables.SPREADING_BAMBOO_SAPLING_BLOCK.defaultBlockState() : this.defaultBlockState().setValue(AGE, blockstate1.getValue(AGE));
	            }
	         } else {
	            return null;
	         }
	      }
	   }

	   public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
	      if (!state.canSurvive(worldIn, pos)) {
	         worldIn.destroyBlock(pos, true);
	      }

	   }

	   /**
	    * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
	    * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
	    */
	   public boolean isRandomlyTicking(BlockState state) {
	      return state.getValue(STAGE) == 0;
	   }

	   /**
	    * Performs a random tick on a block.
	    */
	   public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
	      if (state.getValue(STAGE) == 0) {
	         if (worldIn.isEmptyBlock(pos.above()) && worldIn.getRawBrightness(pos.above(), 0) >= 9) {
	            int i = this.getHeightBelowUpToMax(worldIn, pos) + 1;
	            if (i < 16 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(3) == 0)) {
	               this.growBamboo(state, worldIn, pos, random, i);
	               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
	            }
	         }

	      }
	   }

	   public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
	      return worldIn.getBlockState(pos.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
	   }

	   /**
	    * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	    * returns its solidified counterpart.
	    * Note that this method should ideally consider only the specific face passed in.
	    */
	   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
	      if (!stateIn.canSurvive(worldIn, currentPos)) {
	         worldIn.getBlockTicks().scheduleTick(currentPos, this, 1);
	      }

	      if (facing == Direction.UP && facingState.is(BlockVariables.SPREADING_BAMBOO_BLOCK) && facingState.getValue(AGE) > stateIn.getValue(AGE)) {
	         worldIn.setBlock(currentPos, stateIn.cycle(AGE), 2);
	      }

	      return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	   }

	   /**
	    * Whether this IGrowable can grow
	    */
	   public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
	      int i = this.getHeightAboveUpToMax(worldIn, pos);
	      int j = this.getHeightBelowUpToMax(worldIn, pos);
	      return i + j + 1 < 16 && worldIn.getBlockState(pos.above(i)).getValue(STAGE) != 1;
	   }

	   public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
	      return true;
	   }

	   public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
	      int i = this.getHeightAboveUpToMax(worldIn, pos);
	      int j = this.getHeightBelowUpToMax(worldIn, pos);
	      int k = i + j + 1;
	      int l = 1 + rand.nextInt(2);

	      for(int i1 = 0; i1 < l; ++i1) {
	         BlockPos blockpos = pos.above(i);
	         BlockState blockstate = worldIn.getBlockState(blockpos);
	         if (k >= 16 || blockstate.getValue(STAGE) == 1 || !worldIn.isEmptyBlock(blockpos.above())) {
	            return;
	         }

	         this.growBamboo(blockstate, worldIn, blockpos, rand, k);
	         ++i;
	         ++k;
	      }

	   }

	   /**
	    * Get the hardness of this Block relative to the ability of the given player
	    * @deprecated call via {@link IBlockState#getPlayerRelativeBlockHardness(EntityPlayer,World,BlockPos)} whenever
	    * possible. Implementing/overriding is fine.
	    */
	   public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
	      return player.getMainHandItem().getItem() instanceof SwordItem ? 1.0F : super.getDestroyProgress(state, player, worldIn, pos);
	   }

	@Override
	protected void growBamboo(BlockState p_220258_1_, World p_220258_2_, BlockPos p_220258_3_, Random p_220258_4_, int p_220258_5_) {
		if (!p_220258_2_.isClientSide) {
			if (p_220258_4_.nextDouble() <= ConfigHandler.GENERAL.newChuteOnGrowthChance.get()) {
				BlockPos lowestpos = p_220258_3_.immutable();
				for (int yn = p_220258_3_.getY(); yn >= 1; yn--) {
					BlockPos lowerpos = new BlockPos(p_220258_3_.getX(), yn, p_220258_3_.getZ());
					Block lowerbamboo = p_220258_2_.getBlockState(lowerpos).getBlock();
					if (lowerbamboo.equals(BlockVariables.SPREADING_BAMBOO_BLOCK) || lowerbamboo.equals(BlockVariables.SPREADING_BAMBOO_SAPLING_BLOCK)) {
						lowestpos = lowerpos.immutable();
					}
				}
				
				List<BlockPos> potentials = new ArrayList<BlockPos>();
				Iterator<BlockPos> blocksaround = BlockPos.betweenClosed(lowestpos.getX()-1, lowestpos.getY(), lowestpos.getZ()-1, lowestpos.getX()+1, lowestpos.getY(), lowestpos.getZ()+1).iterator();
				while (blocksaround.hasNext()) {
					BlockPos npos = blocksaround.next();
					if (p_220258_2_.getBlockState(npos).getBlock().equals(Blocks.AIR)) {
						Block belowblock = p_220258_2_.getBlockState(npos.below().immutable()).getBlock();
						if (BlockFunctions.isGrowBlock(belowblock)) {
							potentials.add(npos.immutable());
						}
					}
				}
				
				int size = potentials.size();
				if (size > 0) {
					BlockPos spreadpos = potentials.get(p_220258_4_.nextInt(size));
					p_220258_2_.setBlockAndUpdate(spreadpos, BlockVariables.SPREADING_BAMBOO_SAPLING_BLOCK.defaultBlockState());
				}
			}
		}
		
		BlockState blockstate = p_220258_2_.getBlockState(p_220258_3_.below());
		BlockPos blockpos = p_220258_3_.below(2);
		BlockState blockstate1 = p_220258_2_.getBlockState(blockpos);
		BambooLeaves bambooleaves = BambooLeaves.NONE;
		if (p_220258_5_ >= 1) {
			if (blockstate.getBlock() == BlockVariables.SPREADING_BAMBOO_BLOCK && blockstate.getValue(LEAVES) != BambooLeaves.NONE) {
				if (blockstate.getBlock() == BlockVariables.SPREADING_BAMBOO_BLOCK && blockstate.getValue(LEAVES) != BambooLeaves.NONE) {
					bambooleaves = BambooLeaves.LARGE;
					if (blockstate1.getBlock() == BlockVariables.SPREADING_BAMBOO_BLOCK) {
						p_220258_2_.setBlock(p_220258_3_.below(), blockstate.setValue(LEAVES, BambooLeaves.SMALL), 3);
						p_220258_2_.setBlock(blockpos, blockstate1.setValue(LEAVES, BambooLeaves.NONE), 3);
					}
				}
			} 
			else {
				bambooleaves = BambooLeaves.SMALL;
			}
		}
		
		int i = p_220258_1_.getValue(AGE) != 1 && blockstate1.getBlock() != BlockVariables.SPREADING_BAMBOO_BLOCK ? 0 : 1;
		int j = (p_220258_5_ < 11 || !(p_220258_4_.nextFloat() < 0.25F)) && p_220258_5_ != 15 ? 0 : 1;
		p_220258_2_.setBlock(p_220258_3_.above(), this.defaultBlockState().setValue(AGE, Integer.valueOf(i)).setValue(LEAVES, bambooleaves).setValue(STAGE, Integer.valueOf(j)), 3);
	}
	

	   /**
	    * Returns the number of continuous bamboo blocks above the position passed in, up to 16.
	    */
	   protected int getHeightAboveUpToMax(IBlockReader worldIn, BlockPos pos) {
	      int i;
	      for(i = 0; i < 16 && worldIn.getBlockState(pos.above(i + 1)).is(BlockVariables.SPREADING_BAMBOO_BLOCK); ++i) {
	      }

	      return i;
	   }

	   /**
	    * Returns the number of continuous bamboo blocks below the position passed in, up to 16.
	    */
	   protected int getHeightBelowUpToMax(IBlockReader worldIn, BlockPos pos) {
	      int i;
	      for(i = 0; i < 16 && worldIn.getBlockState(pos.below(i + 1)).is(BlockVariables.SPREADING_BAMBOO_BLOCK); ++i) {
	      }

	      return i;
	   }
}