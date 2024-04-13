package org.cneko.sudo.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.server.players.StoredUserEntry;
import org.cneko.sudo.api.SudoPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

import static org.cneko.sudo.SudoMeta.META;

@Mixin(ServerOpList.class)
public class ServerOpListMixin {
    @Inject(method = "createEntry",at = @At("RETURN"), cancellable = true)
    private void onCreateEntry(JsonObject jsonObject, CallbackInfoReturnable<StoredUserEntry<GameProfile>> cir) {
        cir.setReturnValue(new ServerOpListEntry(jsonObject));
        cir.cancel();
    }



    // 根据玩家名字获取玩家UUID
    @Unique
    private UUID getUUID(String playerName) {
        ServerPlayer player = META.getMinecraftServer().getPlayerList().getPlayerByName(playerName);
        return player != null ? player.getUUID() : null;
    }


    @Unique
    public int getLevel(String playerName) {
        return SudoPlayer.getSudoLevel(META.getMinecraftServer().getPlayerList().getPlayerByName(playerName));
    }

    @Unique
    public JsonObject convertToJson(List<String> playerNames) {
        JsonObject jsonObject = new JsonObject();

        for (String playerName : playerNames) {
            int level = getLevel(playerName);
            UUID uuid = getUUID(playerName);

            // 构造玩家信息的JsonObject
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("uuid", uuid != null ? uuid.toString() : "");
            playerObject.addProperty("level", level);
            playerObject.addProperty("bypassesPlayerLimit", false);

            // 将玩家信息添加到主JsonObject中，以玩家名字为键
            jsonObject.add(playerName, playerObject);
        }

        return jsonObject;
    }
}
