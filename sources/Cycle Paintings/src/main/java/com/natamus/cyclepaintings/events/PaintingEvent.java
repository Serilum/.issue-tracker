/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.cyclepaintings.events;

import com.natamus.cyclepaintings.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber
public class PaintingEvent {
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.PAINTING)) {
			return;
		}
		
		Entity target = e.getTarget();
		if (!(target instanceof Painting)) {
			return;
		}
		
		Player player = e.getEntity();
		Painting painting = (Painting)target;
		PaintingVariant art = painting.getVariant().value();
		
		PaintingVariant newart = null;
		
		List<PaintingVariant> similarart = Util.getSimilarArt(art);
		if (player.isCrouching()) {
			Collections.reverse(similarart);
		}
		
		if (similarart.get(similarart.size()-1).equals(art)) {
			newart = similarart.get(0);
		}
		else {
			boolean choosenext = false;
			for (PaintingVariant sa : similarart) {
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
		Painting newpainting = new Painting(world, ppos, painting.getMotionDirection(), Holder.direct(newart));
		
		newpainting.setPos(ppos.getX(), ppos.getY(), ppos.getZ());
		
		painting.remove(RemovalReason.DISCARDED);
		world.addFreshEntity(newpainting);
	}
}
