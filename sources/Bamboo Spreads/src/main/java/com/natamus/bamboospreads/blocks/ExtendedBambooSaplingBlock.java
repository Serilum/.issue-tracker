/*
 * This is the latest source code of Bamboo Spreads.
 * Minecraft version: 1.16.5, mod version: 1.9.
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

import java.util.Random;

import com.natamus.bamboospreads.variables.BlockVariables;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
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

public class ExtendedBambooSaplingBlock extends BambooSaplingBlock {

	public ExtendedBambooSaplingBlock(Properties properties) {
		super(properties);
	}

	
	   /**
	    * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
	    */
	   public AbstractBlock.OffsetType getOffsetType() {
	      return AbstractBlock.OffsetType.XZ;
	   }

	   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      Vector3d vector3d = state.getOffset(worldIn, pos);
	      return SHAPE.withOffset(vector3d.x, vector3d.y, vector3d.z);
	   }

	   /**
	    * Performs a random tick on a block.
	    */
	   public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
	      if (random.nextInt(3) == 0 && worldIn.isAirBlock(pos.up()) && worldIn.getLightSubtracted(pos.up(), 0) >= 9) {
	         this.growBamboo(worldIn, pos);
	      }

	   }

	   public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
	      return worldIn.getBlockState(pos.down()).isIn(BlockTags.BAMBOO_PLANTABLE_ON);
	   }

	   /**
	    * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	    * returns its solidified counterpart.
	    * Note that this method should ideally consider only the specific face passed in.
	    */
	   public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
	      if (!stateIn.isValidPosition(worldIn, currentPos)) {
	         return Blocks.AIR.getDefaultState();
	      } else {
	         if (facing == Direction.UP && facingState.isIn(BlockVariables.SPREADING_BAMBOO_BLOCK)) {
	            worldIn.setBlockState(currentPos, BlockVariables.SPREADING_BAMBOO_BLOCK.getDefaultState(), 2);
	         }

	         return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	      }
	   }

	   public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
	      return new ItemStack(Items.BAMBOO);
	   }

	   /**
	    * Whether this IGrowable can grow
	    */
	   @SuppressWarnings("deprecation")
	   public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
	      return worldIn.getBlockState(pos.up()).isAir();
	   }

	   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
	      return true;
	   }

	   public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
	      this.growBamboo(worldIn, pos);
	   }

	   /**
	    * Get the hardness of this Block relative to the ability of the given player
	    * @deprecated call via {@link IBlockState#getPlayerRelativeBlockHardness(EntityPlayer,World,BlockPos)} whenever
	    * possible. Implementing/overriding is fine.
	    */
	   public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
	      return player.getHeldItemMainhand().getItem() instanceof SwordItem ? 1.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	   }

	   protected void growBamboo(World world, BlockPos state) {
	      world.setBlockState(state.up(), BlockVariables.SPREADING_BAMBOO_BLOCK.getDefaultState().with(BambooBlock.PROPERTY_BAMBOO_LEAVES, BambooLeaves.SMALL), 3);
	   }
}
