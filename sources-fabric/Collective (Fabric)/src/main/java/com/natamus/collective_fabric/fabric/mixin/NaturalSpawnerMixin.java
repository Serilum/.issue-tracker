/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.48.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveSpawnEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {
	@ModifyVariable(method = "Lnet/minecraft/world/level/NaturalSpawner;spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V", at = @At(value= "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/NaturalSpawner;getMobForSpawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/world/entity/Mob;", ordinal = 0))
	private static Mob NaturalSpawner_spawnCategoryForPosition(Mob mob, MobCategory mobCategory, ServerLevel serverLevel, ChunkAccess chunkAccess, BlockPos blockPos, NaturalSpawner.SpawnPredicate spawnPredicate, NaturalSpawner.AfterSpawnCallback afterSpawnCallback) {
		if (mob != null) {
			mob.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		}
		
		if (!CollectiveSpawnEvents.MOB_CHECK_SPAWN.invoker().onMobCheckSpawn(mob, serverLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, MobSpawnType.NATURAL)) {
			mob = null;
		}
		
		return mob;
	}
	
/*	@ModifyVariable(method = "Lnet/minecraft/world/level/NaturalSpawner;spawnMobsForChunkGeneration(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/level/biome/Biome;IILjava/util/Random;)V", at = @At(value= "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;moveTo(DDDFF)V", ordinal = 0))
	private static Entity NaturalSpawner_spawnMobsForChunkGeneration(Entity entity2, ServerLevelAccessor serverLevelAccessor, Biome biome, int i, int j, Random random) {
		if (entity2 instanceof Mob == false) {
			return entity2;
		}
		
		Mob mob = (Mob)entity2;
		Vec3 vec = mob.position();
		if (!CollectiveSpawnEvents.MOB_CHECK_SPAWN.invoker().onMobCheckSpawn(mob, (ServerLevel)WorldFunctions.getWorldIfInstanceOfAndNotRemote(serverLevelAccessor), vec.x, vec.y, vec.z, null, MobSpawnType.CHUNK_GENERATION)) {
			return null;
		}
		
		return entity2;
	}*/
}
