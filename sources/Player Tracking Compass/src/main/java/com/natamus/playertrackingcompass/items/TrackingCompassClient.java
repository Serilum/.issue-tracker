/*
 * This is the latest source code of Player Tracking Compass.
 * Minecraft version: 1.19.3, mod version: 2.0.
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

package com.natamus.playertrackingcompass.items;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrackingCompassClient implements ItemPropertyFunction {

	@SubscribeEvent
	public static void models(FMLClientSetupEvent e) {
		ItemProperties.register(CompassVariables.TRACKING_COMPASS, new ResourceLocation("angle"), new TrackingCompassClient());
	}

	private double prevAngle = 0.0D;
	private double prevWobble = 0.0D;
	private long prevWorldTime = 0L;

	/**
	 * Calculates the compass angle from an item stack and an entity/item frame
	 *
	 * @param stack The item stack
	 * @param world The world
	 * @param livingEntity The entity
	 * @return The angle
	 */
	@Override
	public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity livingEntity, int i) {
		boolean isLiving = livingEntity != null;

		if (!isLiving && !stack.isFramed()) {
			return 0;
		}

		Entity entity = isLiving ? livingEntity : stack.getFrame();

		if (world == null) {
			world = (ClientLevel)entity.level;
		}

		if (CompassVariables.trackingTarget == null) {
			return 0;
		}
		double angle;

		double entityAngle = isLiving ? entity.getYRot() : getFrameAngle((ItemFrame) entity);
		entityAngle /= 360.0D;
		entityAngle = Mth.positiveModulo(entityAngle, 1.0D);
		double posAngle = getPosToAngle(CompassVariables.trackingTarget, entity);
		posAngle /= Math.PI * 2D;
		angle = 0.5D - (entityAngle - 0.25D - posAngle);

		if (isLiving) {
			angle = wobble(world, angle);
		}

		return Mth.positiveModulo((float) angle, 1.0F);
	}

	/**
	 * Adds wobbliness based on the previous angle and the specified angle
	 *
	 * @param world The world
	 * @param angle The current angle
	 * @return The new, wobbly angle
	 */
	private double wobble(Level world, double angle) {
		long worldTime = world.getGameTime();
		if (worldTime != prevWorldTime) {
			prevWorldTime = worldTime;
			double angleDifference = angle - prevAngle;
			angleDifference = Mth.positiveModulo(angleDifference + 0.5D, 1.0D) - 0.5D;
			prevWobble += angleDifference * 0.1D;
			prevWobble *= 0.8D;
			prevAngle = Mth.positiveModulo(prevAngle + prevWobble, 1.0D);
		}
		return prevAngle;
	}

	/**
	 * Gets the facing direction of an item frame in degrees
	 *
	 * @param entity The entity instance of the item frame
	 * @return The angle
	 */
	private double getFrameAngle(ItemFrame entity) {
		return Mth.wrapDegrees(180 + entity.getDirection().get2DDataValue() * 90);
	}

	/**
	 * Gets the angle from an entity to the specified position in radians
	 *
	 * @param pos The position
	 * @param entity The entity
	 * @return The angle
	 */
	private double getPosToAngle(int[] pos, Entity entity) {
		return Math.atan2(pos[2] - entity.getZ(), pos[0] - entity.getX());
	}
}
