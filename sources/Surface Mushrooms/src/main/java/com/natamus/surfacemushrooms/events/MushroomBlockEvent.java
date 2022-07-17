/*
 * This is the latest source code of Surface Mushrooms.
 * Minecraft version: 1.19.0, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Surface Mushrooms ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.surfacemushrooms.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.CompareItemFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MushroomBlockEvent {
    @SubscribeEvent
    public void onMushroomPlace(PlayerInteractEvent.RightClickBlock e) {
        Level world = e.getLevel();
        if (world.isClientSide) {
            return;
        }

        ItemStack itemstack = e.getItemStack();
        Item item = itemstack.getItem();
        Block block = Block.byItem(item);
        if ((!(block instanceof MushroomBlock)) && !CompareItemFunctions.itemIsInRegistryHolder(item, Tags.Items.MUSHROOMS)) {
            return;
        }

        BlockPos pos = e.getPos();
        BlockState state = world.getBlockState(pos);
        if (!state.isSolidRender(world, pos)) { // may place mushroom on block
            return;
        }

        BlockPos above = pos.above();
        Block aboveblock = world.getBlockState(above).getBlock();
        if (aboveblock.equals(Blocks.AIR)) {
            BlockState placestate = block.defaultBlockState();
            world.setBlock(above, placestate, 3);

            Player player = e.getEntity();
            player.swing(e.getHand());

            if (!player.isCreative()) {
                itemstack.shrink(1);
            }

            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
            e.setCanceled(true);
        }
    }
}