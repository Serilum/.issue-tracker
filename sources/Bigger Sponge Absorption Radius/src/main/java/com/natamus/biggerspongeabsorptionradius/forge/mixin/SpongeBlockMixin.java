/*
 * This is the latest source code of Bigger Sponge Absorption Radius.
 * Minecraft version: 1.19.1, mod version: 2.3.
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

package com.natamus.biggerspongeabsorptionradius.forge.mixin;

import com.google.common.collect.Lists;
import com.natamus.collective.functions.BlockPosFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

@Mixin(value = SpongeBlock.class, priority = 999)
public class SpongeBlockMixin extends Block {
    private static final List<Material> spongematerials = new ArrayList<Material>(List.of(Material.SPONGE));

    public SpongeBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    /**
     * @author Rick South
     * @reason Unable to accomplish with a simple injection.
     */
    @Overwrite
    private boolean removeWaterBreadthFirstSearch(Level worldIn, BlockPos pos) {
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
