/*
 * This is the latest source code of Placeable Blaze Rods.
 * Minecraft version: 1.18.2, mod version: 1.4.
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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlazeRodBlock extends DirectionalBlock {
	protected static final VoxelShape BLAZE_ROD_VERTICAL_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
	protected static final VoxelShape BLAZE_ROD_NS_AABB = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);
	protected static final VoxelShape BLAZE_ROD_EW_AABB = Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);

	public BlazeRodBlock(Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}
	
	   @Deprecated
	   @Override
	   public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		      return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.BLAZE_ROD, 1)));
	   }
	   
	   public static List<ItemStack> getDrops(BlockState state, ServerLevel worldIn, BlockPos pos, @Nullable BlockEntity tileEntityIn) {
		      return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.BLAZE_ROD, 1)));
	   }

	   public static List<ItemStack> getDrops(BlockState state, ServerLevel worldIn, BlockPos pos, @Nullable BlockEntity tileEntityIn, Entity entityIn, ItemStack stack) {
		      return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.BLAZE_ROD, 1)));
	   }

	   /**
	    * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	    * blockstate.
	    * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
	    * fine.
	    */
	   public BlockState rotate(BlockState state, Rotation rot) {
	      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	   }

	   /**
	    * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	    * blockstate.
	    * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	    */
	   public BlockState mirror(BlockState state, Mirror mirrorIn) {
	      return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
	   }

	   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
	      switch(state.getValue(FACING).getAxis()) {
	      case X:
	      default:
	         return BLAZE_ROD_EW_AABB;
	      case Z:
	         return BLAZE_ROD_NS_AABB;
	      case Y:
	         return BLAZE_ROD_VERTICAL_AABB;
	      }
	   }

	   public BlockState getStateForPlacement(BlockPlaceContext context) {
	      Direction direction = context.getClickedFace();
	      BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
	      return blockstate.getBlock() == this && blockstate.getValue(FACING) == direction ? this.defaultBlockState().setValue(FACING, direction.getOpposite()) : this.defaultBlockState().setValue(FACING, direction);
	   }

	   /**
	    * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
	    * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
	    * of whether the block can receive random update ticks
	    */
	   @OnlyIn(Dist.CLIENT)
	   public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
	      Direction direction = stateIn.getValue(FACING);
	      double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
	      if (rand.nextInt(5) == 0) {
	         worldIn.addParticle(ParticleTypes.END_ROD, d0 + (double)direction.getStepX() * d3, d1 + (double)direction.getStepY() * d3, d2 + (double)direction.getStepZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
	      }

	   }

	   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	      builder.add(FACING);
	   }

	   /**
	    * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
	    */
	   public PushReaction getPistonPushReaction(BlockState state) {
	      return PushReaction.NORMAL;
	   }
}