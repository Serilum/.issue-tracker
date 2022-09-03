/*
 * This is the latest source code of Shulker Drops Two.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.shulkerdropstwo.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.monster.Shulker;

@Mixin(value = Shulker.class, priority = 1001)
public class ShulkerMixin {

}
