package org.cneko.sudo.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.SudoPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.commands.OpCommand;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(OpCommand.class)
public class OpCommandMixin {
    @Inject(method = "opPlayers",at = @At("HEAD"))
    private static void onOpPlayers(CommandSourceStack commandSourceStack, Collection<GameProfile> collection, CallbackInfoReturnable<Integer> cir) {
        ServerPlayer player = commandSourceStack.getPlayer();
        // 设置玩家为sudo玩家
        SudoPlayer.setSudoPlayer(player);
    }
}
