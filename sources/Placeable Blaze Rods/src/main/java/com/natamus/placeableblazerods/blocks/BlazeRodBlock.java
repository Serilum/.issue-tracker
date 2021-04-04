/*
 * This is the latest source code of Placeable Blaze Rods.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Placeable Blaze Rods ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.placeableblazerods.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlazeRodBlock extends DirectionalBlock {
	protected static final VoxelShape BLAZE_ROD_VERTICAL_AABB = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
	protected static final VoxelShape BLAZE_ROD_NS_AABB = Block.makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);
	protected static final VoxelShape BLAZE_ROD_EW_AABB = Block.makeCuboidShape(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);

	public BlazeRodBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
	}
	
	   @Deprecated
	   @Override
	   public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		      return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.BLAZE_ROD, 1)));
	   }
	   
	   public static List<ItemStack> getDrops(BlockState state, ServerWorld worldIn, BlockPos pos, @Nullable TileEntity tileEntityIn) {
		      return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.BLAZE_ROD, 1)));
	   }

	   public static List<ItemStack> getDrops(BlockState state, ServerWorld worldIn, BlockPos pos, @Nullable TileEntity tileEntityIn, Entity entityIn, ItemStack stack) {
		      return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.BLAZE_ROD, 1)));
	   }

	   /**
	    * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	    * blockstate.
	    * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
	    * fine.
	    */
	   public BlockState rotate(BlockState state, Rotation rot) {
	      return state.with(FACING, rot.rotate(state.get(FACING)));
	   }

	   /**
	    * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	    * blockstate.
	    * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	    */
	   public BlockState mirror(BlockState state, Mirror mirrorIn) {
	      return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
	   }

	   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      switch(state.get(FACING).getAxis()) {
	      case X:
	      default:
	         return BLAZE_ROD_EW_AABB;
	      case Z:
	         return BLAZE_ROD_NS_AABB;
	      case Y:
	         return BLAZE_ROD_VERTICAL_AABB;
	      }
	   }

	   public BlockState getStateForPlacement(BlockItemUseContext context) {
	      Direction direction = context.getFace();
	      BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
	      return blockstate.getBlock() == this && blockstate.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
	   }

	   /**
	    * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
	    * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
	    * of whether the block can receive random update ticks
	    */
	   @OnlyIn(Dist.CLIENT)
	   public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
	      Direction direction = stateIn.get(FACING);
	      double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
	      if (rand.nextInt(5) == 0) {
	         worldIn.addParticle(ParticleTypes.END_ROD, d0 + (double)direction.getXOffset() * d3, d1 + (double)direction.getYOffset() * d3, d2 + (double)direction.getZOffset() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
	      }

	   }

	   protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	      builder.add(FACING);
	   }

	   /**
	    * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
	    */
	   public PushReaction getPushReaction(BlockState state) {
	      return PushReaction.NORMAL;
	   }
}