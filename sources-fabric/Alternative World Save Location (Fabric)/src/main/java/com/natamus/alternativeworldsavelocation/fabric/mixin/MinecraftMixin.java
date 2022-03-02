/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.x, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Alternative World Save Location ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.alternativeworldsavelocation.fabric.mixin;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.datafixers.DataFixer;
import com.natamus.alternativeworldsavelocation.util.Reference;
import com.natamus.alternativeworldsavelocation.util.Util;
import com.natamus.collective_fabric.functions.ConfigFunctions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.world.level.storage.LevelStorageSource;

@Mixin(value = Minecraft.class, priority = 1001)
public class MinecraftMixin {
	@Shadow private @Final DataFixer fixerUpper;
	@Shadow private @Mutable @Final LevelStorageSource levelSource;
	
    @Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At(value = "TAIL"))
    private void Minecraft(GameConfig i, CallbackInfo ci) {    	
    	boolean changeDefaultWorldSaveLocation = false;
    	boolean changeDefaultWorldBackupLocation = false;
    	String rawsavespath = "";
    	String rawbackupspath = "";
    	
    	List<String> rawconfig = ConfigFunctions.getRawConfigValues(Reference.MOD_ID);
    	for (String rc : rawconfig) {
    		if (rc.contains("changeDefaultWorldSaveLocation")) {
    			changeDefaultWorldSaveLocation = rc.contains("true");
    		}
    		else if (rc.contains("changeDefaultWorldBackupLocation")) {
    			changeDefaultWorldBackupLocation = rc.contains("true");
    		}
    		else if (rc.contains("defaultMinecraftWorldSaveLocation") && rc.contains("\":")) {
    			rawsavespath = rc.split("\":")[1].replace("\"", "").trim();
    		}
    		else if (rc.contains("defaultMinecraftWorldBackupLocation") && rc.contains("\\\":")) {
    			rawbackupspath = rc.split("\":")[1].replace("\"", "").trim();
    		}
    	}
    	
    	if (!changeDefaultWorldSaveLocation) {
    		return;
    	}
    	
    	String savespath = Util.returnSystemSpecificPath(rawsavespath);
    	
    	String backuppath = savespath + File.separator + "_Backups";
    	if (changeDefaultWorldBackupLocation) {
    		backuppath = Util.returnSystemSpecificPath(rawbackupspath);
    	}
    	
    	this.levelSource = new LevelStorageSource(Paths.get(savespath), Paths.get(backuppath), this.fixerUpper);
    }
}
