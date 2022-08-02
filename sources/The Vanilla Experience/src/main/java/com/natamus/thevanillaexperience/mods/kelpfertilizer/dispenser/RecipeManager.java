/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.kelpfertilizer.dispenser;

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
