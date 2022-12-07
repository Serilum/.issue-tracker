/*
 * This is the latest source code of Shulker Drops Two.
 * Minecraft version: 1.19.3, mod version: 2.5.
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

package com.natamus.shulkerdropstwo.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.monster.Shulker;

@Mixin(value = Shulker.class, priority = 1001)
public class ShulkerMixin {

}
