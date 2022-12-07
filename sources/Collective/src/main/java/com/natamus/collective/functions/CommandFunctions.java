/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.25.
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

package com.natamus.collective.functions;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CommandFunctions {
    public static String getRawCommandOutput(ServerLevel serverLevel, @Nullable Vec3 vec, String command) {
        BaseCommandBlock bcb = new BaseCommandBlock() {
            @Override
            public @NotNull ServerLevel getLevel() {
                return serverLevel;
            }

            @Override
            public void onUpdated() { }

            @Override
            public @NotNull Vec3 getPosition() {
                return Objects.requireNonNullElseGet(vec, () -> new Vec3(0, 0, 0));
            }

            @Override
            public @NotNull CommandSourceStack createCommandSourceStack() {
                return new CommandSourceStack(this, getPosition(), Vec2.ZERO, serverLevel, 2, "dev", Component.literal("dev"), serverLevel.getServer(), null);
            }

            @Override
            public boolean performCommand(Level pLevel) {
                if (!pLevel.isClientSide) {
                    if ("Searge".equalsIgnoreCase(this.getCommand())) {
                        this.setLastOutput(Component.literal("#itzlipofutzli"));
                        this.setSuccessCount(1);
                    }
                    else {
                        this.setSuccessCount(0);
                        MinecraftServer minecraftserver = this.getLevel().getServer();
                        if (!StringUtil.isNullOrEmpty(this.getCommand())) {
                            try {
                                this.setLastOutput(null);
                                CommandSourceStack commandsourcestack = this.createCommandSourceStack().withCallback((p_45417_, p_45418_, p_45419_) -> {
                                    if (p_45418_) {
                                        this.setSuccessCount(this.getSuccessCount()+1);
                                    }

                                });
                                minecraftserver.getCommands().performPrefixedCommand(commandsourcestack, this.getCommand());
                            }
                            catch (Throwable throwable) {
                                CrashReport crashreport = CrashReport.forThrowable(throwable, "Executing command block");
                                CrashReportCategory crashreportcategory = crashreport.addCategory("Command to be executed");
                                crashreportcategory.setDetail("Command", this::getCommand);
                                crashreportcategory.setDetail("Name", () -> this.getName().getString());
                                throw new ReportedException(crashreport);
                            }
                        }
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        };

        bcb.setCommand(command);
        bcb.setTrackOutput(true);
        bcb.performCommand(serverLevel);

        return bcb.getLastOutput().getString();
    }
}
