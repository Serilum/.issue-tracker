/*
 * This is the latest source code of Recast.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Recast ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.recast.events;

import java.util.HashMap;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.BlockPosFunctions;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RecastEvent {
	private static HashMap<PlayerEntity, Vector3d> recasting = new HashMap<PlayerEntity, Vector3d>();
	private static HashMap<FishingBobberEntity, Vector3d> lastcastlocation = new HashMap<FishingBobberEntity, Vector3d>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (!recasting.containsKey(player)) {
			return;
		}
		
		ItemStack activestack = null;
		ItemStack mainhand = player.getHeldItemMainhand();
		if (mainhand.getItem() instanceof FishingRodItem == false) {
			ItemStack offhand = player.getHeldItemOffhand();
			if (offhand.getItem() instanceof FishingRodItem == false) {
				recasting.remove(player);
				return;
			}
			activestack = offhand;
		}
		else {
			activestack = mainhand;
		}
		
		Vector3d fbvec = recasting.get(player);
		
		world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
        
		int k = EnchantmentHelper.getFishingSpeedBonus(activestack);
        int j = EnchantmentHelper.getFishingLuckBonus(activestack);
        
        FishingBobberEntity fbe = new FishingBobberEntity(player, world, j, k);
        fbe.setPositionAndUpdate(fbvec.x, fbvec.y, fbvec.z);
        world.addEntity(fbe);
        
        player.addStat(Stats.ITEM_USED.get(Items.FISHING_ROD));
        
        recasting.remove(player);
        lastcastlocation.put(fbe, fbvec);
	}
	
	@SubscribeEvent
	public void onFishingCatch(ItemFishedEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		FishingBobberEntity fbe = e.getHookEntity();
		Vector3d fbvec = fbe.getPositionVec();
		if (lastcastlocation.containsKey(fbe)) {
			Vector3d lastvec = lastcastlocation.get(fbe);
			if (BlockPosFunctions.withinDistance(new BlockPos(fbvec.x, fbvec.y, fbvec.z), new BlockPos(lastvec.x, lastvec.y, lastvec.z), 3)) {
				fbvec = lastvec;
			}
		}
		
		recasting.put(player, fbvec);
		lastcastlocation.remove(fbe);
	}
}
