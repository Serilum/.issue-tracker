/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.16.5, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Cycle Paintings ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.cyclepaintings.events;

import java.util.List;

import com.natamus.cyclepaintings.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PaintingEvent {
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.EntityInteract e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.PAINTING)) {
			return;
		}
		
		Entity target = e.getTarget();
		if (target instanceof PaintingEntity == false) {
			return;
		}
		
		PaintingEntity painting = (PaintingEntity)target;
		PaintingType art = painting.motive;
		
		PaintingType newart = null;
		
		List<PaintingType> similarart = Util.getSimilarArt(art);
		if (similarart.get(similarart.size()-1).equals(art)) {
			newart = similarart.get(0);
		}
		else {
			Boolean choosenext = false;
			for (PaintingType sa : similarart) {
				if (choosenext) {
					newart = sa;
					break;
				}
				if (sa.equals(art)) {
					choosenext = true;
				}
			}
		}
		
		if (newart == null) {
			return;
		}
		
		BlockPos ppos = painting.getPos();
		PaintingEntity newpainting = new PaintingEntity(world, ppos, painting.getMotionDirection());
		
		newpainting.motive = newart;
		newpainting.setPos(ppos.getX(), ppos.getY(), ppos.getZ());
		
		painting.remove();
		world.addFreshEntity(newpainting);
	}
}
