package org.cneko.sudo.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.DeOpCommands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.SudoPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(DeOpCommands.class)
public abstract class DeOpCommandsMixin {

    @Inject(method = "deopPlayers",at = @At("HEAD"))
    private static void onDeopPlayers(CommandSourceStack commandSourceStack, Collection<GameProfile> collection, CallbackInfoReturnable<Integer> cir) {
        ServerPlayer player = commandSourceStack.getPlayer();
        // 取消玩家的sudo权限
        SudoPlayer.unsetSudoPlayer(player);
    }
}
