/*
 * This is the latest source code of Recast.
 * Minecraft version: 1.17.1, mod version: 1.5.
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

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RecastEvent {
	private static HashMap<Player, Vec3> recasting = new HashMap<Player, Vec3>();
	private static HashMap<FishingHook, Vec3> lastcastlocation = new HashMap<FishingHook, Vec3>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!recasting.containsKey(player)) {
			return;
		}
		
		ItemStack activestack = null;
		ItemStack mainhand = player.getMainHandItem();
		if (mainhand.getItem() instanceof FishingRodItem == false) {
			ItemStack offhand = player.getOffhandItem();
			if (offhand.getItem() instanceof FishingRodItem == false) {
				recasting.remove(player);
				return;
			}
			activestack = offhand;
		}
		else {
			activestack = mainhand;
		}
		
		Vec3 fbvec = recasting.get(player);
		
		world.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
        
		int k = EnchantmentHelper.getFishingSpeedBonus(activestack);
        int j = EnchantmentHelper.getFishingLuckBonus(activestack);
        
        FishingHook fbe = new FishingHook(player, world, j, k);
        fbe.teleportTo(fbvec.x, fbvec.y, fbvec.z);
        world.addFreshEntity(fbe);
        
        player.awardStat(Stats.ITEM_USED.get(Items.FISHING_ROD));
        
        recasting.remove(player);
        lastcastlocation.put(fbe, fbvec);
	}
	
	@SubscribeEvent
	public void onFishingCatch(ItemFishedEvent e) {
		Player player = e.getPlayer();
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		FishingHook fbe = e.getHookEntity();
		Vec3 fbvec = fbe.position();
		if (lastcastlocation.containsKey(fbe)) {
			Vec3 lastvec = lastcastlocation.get(fbe);
			if (BlockPosFunctions.withinDistance(new BlockPos(fbvec.x, fbvec.y, fbvec.z), new BlockPos(lastvec.x, lastvec.y, lastvec.z), 5)) {
				fbvec = lastvec;
			}
		}
		
		recasting.put(player, fbvec);
		lastcastlocation.remove(fbe);
	}
}
