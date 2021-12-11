/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.18.1, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Kelp Fertilizer ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.kelpfertilizer.dispenser;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Items;

public class RecipeManager {
	public static void initDispenserBehavior() {
		try {
			DispenserBlock.registerBehavior(Items.KELP, new BehaviourKelpDispenser(Items.KELP));
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("!!! Kelp Fertilizer: Something went wrong when adding the kelp behaviour to dispensers. Ignoring.");
			return;
		}
	}
}
