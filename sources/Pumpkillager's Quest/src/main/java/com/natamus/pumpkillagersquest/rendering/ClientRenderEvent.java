/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 1.5.
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

package com.natamus.pumpkillagersquest.rendering;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.TaskFunctions;
import com.natamus.pumpkillagersquest.util.QuestData;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientRenderEvent {
	private static Minecraft mc = Minecraft.getInstance();
	public static int timeLeftRenderRitual = 0;
	public static BlockPos centerPos = null;
	public static Block centerBlock = null;
	public static Block ritualBlock = null;
	public static boolean shouldReset = false;

	public static List<Pair<BlockPos, BlockState>> previousStates = new ArrayList<Pair<BlockPos, BlockState>>();
	private static HashMap<BlockPos, SkullBlockEntity> playerHeadEntities = new HashMap<BlockPos, SkullBlockEntity>();

	public static void setTemporaryRitualRender(BlockPos cP, Block cB, Block rB) {
		ClientLevel clientLevel = mc.level;

		TaskFunctions.enqueueImmediateTask(clientLevel, () -> {
			if (previousStates.size() > 0) {
				shouldReset = true;
			}

			centerPos = cP;
			centerBlock = cB;
			ritualBlock = rB;

			if (cP != null) {
				timeLeftRenderRitual = 100;
				mc.player.displayClientMessage(Component.translatable("The magical book shows you a glimpse of the ritual.").withStyle(ChatFormatting.GOLD), true);
			}
		}, true);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e) {
		ClientLevel clientLevel = mc.level;

		if (shouldReset) {
			for (Pair<BlockPos, BlockState> previousPair : previousStates) {
				BlockPos setPos = previousPair.getFirst();
				BlockState setState = previousPair.getSecond();

				clientLevel.setBlock(setPos, setState, 3);

				if (setState.getBlock() instanceof PlayerHeadBlock && playerHeadEntities.containsKey(setPos)) {
					clientLevel.setBlockEntity(playerHeadEntities.get(setPos));
				}
			}

			shouldReset = false;
			previousStates = new ArrayList<Pair<BlockPos, BlockState>>();
			playerHeadEntities = new HashMap<BlockPos, SkullBlockEntity>();
			return;
		}

		if (timeLeftRenderRitual > 0 && centerBlock != null) {
			boolean setStates = previousStates.size() == 0;

			if (setStates) {
				previousStates.add(new Pair<BlockPos, BlockState>(centerPos.immutable(), clientLevel.getBlockState(centerPos)));
				previousStates.add(new Pair<BlockPos, BlockState>(centerPos.below().immutable(), clientLevel.getBlockState(centerPos.below())));
			}
			clientLevel.setBlock(centerPos, centerBlock.defaultBlockState(), 3);

			int i = 0;
			for (BlockPos posAround : BlockPos.betweenClosed(centerPos.getX()-3, centerPos.getY(), centerPos.getZ()-3, centerPos.getX()+3, centerPos.getY(), centerPos.getZ()+3)) {
				if (!posAround.equals(centerPos)) {
					if (setStates) {
						BlockState aroundState = clientLevel.getBlockState(posAround);
						previousStates.add(new Pair<BlockPos, BlockState>(posAround.immutable(), aroundState));

						if (aroundState.getBlock() instanceof PlayerHeadBlock) {
							playerHeadEntities.put(posAround.immutable(), (SkullBlockEntity)clientLevel.getBlockEntity(posAround));
						}
					}

					if (QuestData.questOneRitualBlockPositions.contains(i)) {
						BlockState state = ritualBlock.defaultBlockState();
						if (ritualBlock instanceof CandleBlock) {
							state = state.setValue(CandleBlock.LIT, true);
						}

						clientLevel.setBlock(posAround, state, 3);
					} else {
						clientLevel.setBlock(posAround, Blocks.AIR.defaultBlockState(), 3);
					}
				}

				i++;
			}

			timeLeftRenderRitual-=1;
			if (timeLeftRenderRitual <= 0) {
				setTemporaryRitualRender(null, null, null);
			}
		}
	}

	@SubscribeEvent
	public void onEntitySize(EntityEvent.Size e) {
		Entity entity = e.getEntity();
		if (!Util.isPumpkillager(entity)) {
			return;
		}

		String scaleFloatString = "";

		Villager pumpkillager = (Villager)entity;
		Component pumpkillagerComponent = pumpkillager.getName();
		String pumpkillagerName = pumpkillager.getName().getString();

		ItemStack footStack = pumpkillager.getItemBySlot(EquipmentSlot.FEET);
		if (footStack.getItem().equals(Items.BARRIER)) {
			scaleFloatString = footStack.getHoverName().getString();
		}
		else if (pumpkillagerName.contains("|")) {
			scaleFloatString = pumpkillagerName.split("\\|")[1];
		}

		float scale;
		try {
			scale = Float.parseFloat(scaleFloatString);
		}
		catch (NumberFormatException ex) { return; }

		EntityDimensions size = EntityType.VILLAGER.getDimensions().scale(scale);
		e.setNewSize(size, true);
	}
}
