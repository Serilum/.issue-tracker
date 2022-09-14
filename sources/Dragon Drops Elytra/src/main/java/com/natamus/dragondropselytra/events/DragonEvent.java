/*
 * This is the latest source code of Dragon Drops Elytra.
 * Minecraft version: 1.19.2, mod version: 1.7.
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

package com.natamus.dragondropselytra.events;

import java.util.List;

import com.natamus.collective.functions.StringFunctions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DragonEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof EnderDragon == false) {
			return;
		}
		
		Player player = null;
		BlockPos epos = entity.blockPosition();
		Entity source = e.getSource().getEntity();
		
		if (source == null) {
			List<Entity> entitiesaround = world.getEntities(entity, new AABB(epos.getX()-30, epos.getY()-30, epos.getZ()-30, epos.getX()+30, epos.getY()+30, epos.getZ()+30));
			for (Entity ea : entitiesaround) {
				if (ea instanceof Player) {
					player = (Player)ea;
					break;
				}
			}
		}
		else if (source instanceof Player) {
			player = (Player)source;
		}
		
		StringFunctions.broadcastMessage(world, "It seems like the slain Ender Dragon dropped an elytra! Perhaps it previously belonged to another adventurer?", ChatFormatting.DARK_GREEN);
		
		ItemStack elytrastack = new ItemStack(Items.ELYTRA, 1);
		if (player == null) {
			e.getDrops().add(new ItemEntity(world, epos.getX(), epos.getY()+1, epos.getZ(), elytrastack));
		}
		else {
			BlockPos pos = player.blockPosition();
			ItemEntity elytra = new ItemEntity(world, pos.getX(), pos.getY()+1, pos.getZ(), elytrastack);
			world.addFreshEntity(elytra);
			
			StringFunctions.sendMessage(player, "The elytra dropped at your position!", ChatFormatting.YELLOW);
		}
	}
}
