package org.cneko.sudo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.server.MinecraftServer;
import org.cneko.ctlib.common.util.meta.PluginMeta;
import org.jetbrains.annotations.NotNull;
import net.minecraft.server.dedicated.DedicatedServerProperties;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

public class SudoMeta implements PluginMeta{
    public static SudoMeta META = new SudoMeta();
    private MinecraftServer server;
    private final File dataFolder = new File("sudo");
    private final Logger logger = Logger.getLogger("sudo");
    private final Description description = new Des();
    private final ServerInfo serverInfo = new ServerInfo(server);

    public void setMinecraftServer(MinecraftServer server) {
        this.server = server;
    }
    public MinecraftServer getMinecraftServer(){
        return server;
    }

    @Override
    public @NotNull File getDataFolder() {
        return dataFolder;
    }

    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

    @NotNull
    @Override
    public Description getDescription() {
        return description;
    }

    @NotNull
    @Override
    public Server getServer() {
        return serverInfo;
    }

    private static class Des implements PluginMeta.Description{
        @Override
        public @NotNull String getName() {
            return "Sudo";
        }

        @Override
        public @NotNull String getId() {
            return "sudo";
        }

        @Override
        public String getVersion() {
            Optional<String> version = FabricLoader.getInstance().getModContainer(getId())
                    .map(container -> container.getMetadata().getVersion().getFriendlyString());
            return version.orElse("unknown");
        }
        public ModMetadata getModMetadata(){
            Optional<ModMetadata> modMetadata = FabricLoader.getInstance().getModContainer(getId())
                    .map(ModContainer::getMetadata);
            return modMetadata.orElse(null);
        }

        @Override
        public String getDescription() {
            ModMetadata modMetadata = getModMetadata();
            return modMetadata.getDescription();
        }

        @Override
        public String getWebsite() {
            return "https://github.com/csneko/sudp";
        }

        @Override
        public String[] getAuthors() {
           ModMetadata metadata = getModMetadata();
           Collection<Person> contributors = metadata.getAuthors();
           return contributors.stream().map(Person::getName).toArray(String[]::new);
        }
    }

    public static class ServerInfo implements PluginMeta.Server{
        private MinecraftServer server;
        public ServerInfo(MinecraftServer server) {
            this.server = server;
        }
        public DedicatedServerProperties getProperties() {
            // 服务器配置文件路径
            Path propertiesFilePath = Path.of("server.properties");
            // 通过文件路径创建 DedicatedServerProperties 对象
            DedicatedServerProperties serverProperties = DedicatedServerProperties.fromFile(propertiesFilePath);
            return serverProperties;
        }
        @Override
        public boolean isOnlineMode() {
            return getProperties().onlineMode;
        }

        @Override
        public String getVersion() {
            // 返回服务器版本号
            return FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString(); // 获取Minecraft版本号
        }

        @Override
        public String getName() {
            return getProperties().levelName;
        }

        @Override
        public String getMotd() {
            return getProperties().motd;
        }

        @Override
        public int getPlayerAmount() {
            return server.getPlayerCount();
        }

        @Override
        public int getMaxPlayers() {
            return server.getMaxPlayers();
        }

        @Override
        public GameMode getServerGamemode() {
            return GameMode.valueOf(server.getServerModName());
        }
    }
}
