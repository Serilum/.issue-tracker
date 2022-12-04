/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.17.
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

package com.natamus.collective_fabric.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.Stat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

//Preliminary, simple Fake Player class
public class FakePlayer extends ServerPlayer {
	    public FakePlayer(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ServerPlayerGameMode serverPlayerGameMode) {
			super(minecraftServer, serverLevel, gameProfile, null);
		}
		@Override public Vec3 position(){ return new Vec3(0, 0, 0); }
	    @Override public BlockPos blockPosition(){ return BlockPos.ZERO; }
	    @Override public void displayClientMessage(Component chatComponent, boolean actionBar){}
	    @Override public void awardStat(Stat<?> par1StatBase, int par2){}
	    //@Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z){}
	    @Override public boolean isInvulnerableTo(DamageSource source){ return true; }
	    @Override public boolean canHarmPlayer(Player player){ return false; }
	    @Override public void die(DamageSource source){ }
	    @Override public void tick(){ }
	    @Override public void updateOptions(ServerboundClientInformationPacket pkt){ }

	    @SuppressWarnings("unused")
		private static class FakePlayerNetHandler extends ServerGamePacketListenerImpl {
	        private static final Connection DUMMY_CONNECTION = new Connection(PacketFlow.CLIENTBOUND);

	        public FakePlayerNetHandler(MinecraftServer server, ServerPlayer player) {
	            super(server, DUMMY_CONNECTION, player);
	        }

	        @Override public void tick() { }
	        @Override public void resetPosition() { }
	        @Override public void disconnect(Component message) { }
	        @Override public void handlePlayerInput(ServerboundPlayerInputPacket packet) { }
	        @Override public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) { }
	        @Override public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) { }
	        @Override public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) { }
	        @Override public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) { }
	        @Override public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packet) { }
	        @Override public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) { }
	        @Override public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packet) { }
	        @Override public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packet) { }
	        @Override public void handlePickItem(ServerboundPickItemPacket packet) { }
	        @Override public void handleRenameItem(ServerboundRenameItemPacket packet) { }
	        @Override public void handleSetBeaconPacket(ServerboundSetBeaconPacket packet) { }
	        @Override public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packet) { }
	        @Override public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet) { }
	        @Override public void handleJigsawGenerate(ServerboundJigsawGeneratePacket packet) { }
	        @Override public void handleSelectTrade(ServerboundSelectTradePacket packet) { }
	        @Override public void handleEditBook(ServerboundEditBookPacket packet) { }
	        @Override public void handleEntityTagQuery(ServerboundEntityTagQuery packet) { }
	        @Override public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery packet) { }
	        @Override public void handleMovePlayer(ServerboundMovePlayerPacket packet) { }
	        @Override public void teleport(double x, double y, double z, float yaw, float pitch) { }
	        @Override public void teleport(double x, double y, double z, float yaw, float pitch, Set<ClientboundPlayerPositionPacket.RelativeArgument> flags) { }
	        @Override public void handlePlayerAction(ServerboundPlayerActionPacket packet) { }
	        @Override public void handleUseItemOn(ServerboundUseItemOnPacket packet) { }
	        @Override public void handleUseItem(ServerboundUseItemPacket packet) { }
	        @Override public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packet) { }
	        @Override public void handleResourcePackResponse(ServerboundResourcePackPacket packet) { }
	        @Override public void handlePaddleBoat(ServerboundPaddleBoatPacket packet) { }
	        @Override public void onDisconnect(Component message) { }
	        @Override public void send(Packet<?> packet) { }
	        @Override public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) { }
	        @Override public void handleChat(ServerboundChatPacket packet) { }
	        @Override public void handleAnimate(ServerboundSwingPacket packet) { }
	        @Override public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) { }
	        @Override public void handleInteract(ServerboundInteractPacket packet) { }
	        @Override public void handleClientCommand(ServerboundClientCommandPacket packet) { }
	        @Override public void handleContainerClose(ServerboundContainerClosePacket packet) { }
	        @Override public void handleContainerClick(ServerboundContainerClickPacket packet) { }
	        @Override public void handlePlaceRecipe(ServerboundPlaceRecipePacket packet) { }
	        @Override public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) { }
	        @Override public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) { }
	        @Override public void handleSignUpdate(ServerboundSignUpdatePacket packet) { }
	        @Override public void handleKeepAlive(ServerboundKeepAlivePacket packet) { }
	        @Override public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) { }
	        @Override public void handleClientInformation(ServerboundClientInformationPacket packet) { }
	        @Override public void handleCustomPayload(ServerboundCustomPayloadPacket packet) { }
	        @Override public void handleChangeDifficulty(ServerboundChangeDifficultyPacket packet) { }
	        @Override public void handleLockDifficulty(ServerboundLockDifficultyPacket packet) { }
	    }
}