/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.2, mod version: 2.7.
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

package com.natamus.cyclepaintings.events;

import com.natamus.cyclepaintings.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Collections;
import java.util.List;

public class PaintingEvent {
	public static InteractionResult onClick(Player player, Level world, InteractionHand hand, Entity target, EntityHitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!handstack.getItem().equals(Items.PAINTING)) {
			return InteractionResult.PASS;
		}
		
		if (!(target instanceof Painting)) {
			return InteractionResult.PASS;
		}
		
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
			return InteractionResult.PASS;
		}
		
		BlockPos ppos = painting.getPos();
		Painting newpainting = new Painting(world, ppos, painting.getMotionDirection(), Holder.direct(newart));
		
		newpainting.setPos(ppos.getX(), ppos.getY(), ppos.getZ());
		
		painting.remove(RemovalReason.DISCARDED);
		world.addFreshEntity(newpainting);
		
		return InteractionResult.SUCCESS;
	}
}
