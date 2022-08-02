/*
 * This is the latest source code of Shulker Drops Two.
 * Minecraft version: 1.19.1, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.shulkerdropstwo.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.monster.Shulker;

@Mixin(value = Shulker.class, priority = 1001)
public class ShulkerMixin {

}
