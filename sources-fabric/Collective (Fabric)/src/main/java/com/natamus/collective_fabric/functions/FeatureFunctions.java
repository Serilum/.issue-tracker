/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.45.
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

package com.natamus.collective_fabric.functions;

import com.natamus.collective_fabric.data.GlobalVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FeatureFunctions {
	public static boolean placeBonusChest(Level world, BlockPos blockposIn) {
		ChunkPos chunkpos = new ChunkPos(blockposIn);
		List<Integer> list = IntStream.rangeClosed(chunkpos.getMinBlockX(), chunkpos.getMaxBlockX()).boxed().collect(Collectors.toList());
		Collections.shuffle(list, GlobalVariables.random);
		List<Integer> list1 = IntStream.rangeClosed(chunkpos.getMinBlockZ(), chunkpos.getMaxBlockZ()).boxed().collect(Collectors.toList());
		Collections.shuffle(list1, GlobalVariables.random);
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
		
		for(Integer integer : list) {
			for(Integer integer1 : list1) {
				blockpos$mutable.set(integer, 0, integer1);
				BlockPos blockpos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable);
				if (world.isEmptyBlock(blockpos) || world.getBlockState(blockpos).getCollisionShape(world, blockpos).isEmpty()) {
					world.setBlock(blockpos, Blocks.CHEST.defaultBlockState(), 2);
					RandomizableContainerBlockEntity.setLootTable(world, GlobalVariables.randomSource, blockpos, BuiltInLootTables.SPAWN_BONUS_CHEST);
					BlockState blockstate = Blocks.TORCH.defaultBlockState();
					
					for(Direction direction : Direction.Plane.HORIZONTAL) {
						BlockPos blockpos1 = blockpos.relative(direction);
						if (blockstate.canSurvive(world, blockpos1)) {
							world.setBlock(blockpos1, blockstate, 2);
						}
					}
					
					return true;
				}
			}
		}
		
		return false;
	}
}
