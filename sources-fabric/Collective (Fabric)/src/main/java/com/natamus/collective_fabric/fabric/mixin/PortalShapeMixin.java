/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.7.
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

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective_fabric.functions.WorldFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;

@Mixin(value = PortalShape.class, priority = 1001)
public class PortalShapeMixin {
	@Final @Shadow private LevelAccessor level;
	@Shadow private BlockPos bottomLeft;
	
	@Inject(method = "createPortalBlocks()V", at = @At(value= "HEAD"))
	public void createPortalBlocks(CallbackInfo ci) {
		PortalShape portalshape = (PortalShape)(Object)this;
		CollectiveBlockEvents.ON_NETHER_PORTAL_SPAWN.invoker().onPossiblePortal(WorldFunctions.getWorldIfInstanceOfAndNotRemote(level), bottomLeft, portalshape);
	}
}
