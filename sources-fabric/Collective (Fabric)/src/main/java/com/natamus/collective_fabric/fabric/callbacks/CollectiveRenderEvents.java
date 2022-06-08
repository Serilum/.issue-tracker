/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.27.
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

package com.natamus.collective_fabric.fabric.callbacks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;

public class CollectiveRenderEvents {
	private CollectiveRenderEvents() { }

	public static final Event<CollectiveRenderEvents.Render_Specific_Hand> RENDER_SPECIFIC_HAND = EventFactory.createArrayBacked(CollectiveRenderEvents.Render_Specific_Hand.class, callbacks -> (interactionHand, poseStack, itemStack) -> {
		for (CollectiveRenderEvents.Render_Specific_Hand callback : callbacks) {
			if (!callback.onRenderSpecificHand(interactionHand, poseStack, itemStack)) {
				return false;
			}
		}

		return true;
	});

	public static final Event<CollectiveRenderEvents.Set_Entity_Hitbox_Size> SET_ENTITY_HITBOX = EventFactory.createArrayBacked(CollectiveRenderEvents.Set_Entity_Hitbox_Size.class, callbacks -> (entity, pose, oldSize, newSize, oldEyeHeight, newEyeHeight) -> {
		for (CollectiveRenderEvents.Set_Entity_Hitbox_Size callback : callbacks) {
			Pair<EntityDimensions, Float> resultpair = callback.setEntityHitboxSize(entity, pose, oldSize, newSize, oldEyeHeight, newEyeHeight);
			if (resultpair != null) {
				return resultpair;
			}
		}

		return null;
	});

	@FunctionalInterface
	public interface Render_Specific_Hand {
		boolean onRenderSpecificHand(InteractionHand interactionHand, PoseStack poseStack, ItemStack itemStack);
	}

	@FunctionalInterface
	public interface Set_Entity_Hitbox_Size {
		Pair<EntityDimensions, Float> setEntityHitboxSize(Entity entity, Pose pose, EntityDimensions oldSize, EntityDimensions newSize, float oldEyeHeight, float newEyeHeight);
	}
}
