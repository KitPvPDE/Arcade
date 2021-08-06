package net.kitpvp.plugins.arcade.game;

import static net.kitpvp.plugins.kitpvpcore.utils.YamlUtils.locationFromConfig;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.kitpvpcore.utils.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ArcadeConfiguration {

    @Getter
    private final int lobbyTime, maxPlayers, minPlayers;

    @Getter
    private final JNRConfiguration jnrConfiguration;

    public ArcadeConfiguration(ArcadePlugin plugin) {
        plugin.saveResource("config.yml", true);

        FileConfiguration configuration = plugin.getConfig();
        // general configuration
        this.lobbyTime = configuration.getInt("arcade.lobby.time", 60);
        this.maxPlayers = configuration.getInt("arcade.max-players", 100);
        this.minPlayers = configuration.getInt("arcade.min-players", 2);

        // lobby configuration

        // jnr configuration
        this.jnrConfiguration = new JNRConfiguration(plugin);
    }

    @Getter
    public static class JNRConfiguration {

        private final Location jnrStart;
        private final Location jnrSpawn;
        private final Location deathMatchFallBackSpawn;
        private final List<Location> deathMatchSpawns;
        private final List<Integer> checkpoints;
        private final int jumpTime;
        private final int checkpointDelay;
        private final boolean colorBlocks;

        public JNRConfiguration(ArcadePlugin plugin) {
            FileConfiguration configuration = plugin.getConfig();

            World world = Bukkit.getWorlds().get(0);

            this.jnrStart = locationFromConfig(configuration, "arcade.jnr.start", world);
            this.jnrSpawn = locationFromConfig(configuration, "arcade.jnr.spawn", world);
            this.deathMatchFallBackSpawn = locationFromConfig(configuration, "arcade.jnr.deathmatch.fallback.spawn", world);
            this.deathMatchSpawns = configuration.getStringList("arcade.jnr.deathmatch.spawns").stream()
                .map(input -> SpigotUtils.locationFromString(input, world)).collect(Collectors.toList());
            this.jumpTime = configuration.getInt("arcade.jnr.jumpTime", 300);
            this.checkpointDelay = configuration.getInt("arcade.jnr.checkpoint.delay", 10);
            this.checkpoints = configuration.getIntegerList("arcade.jnr.checkpoints");
            this.colorBlocks = configuration.getBoolean("arcade.jnr.colorBlocksForEachPlayer", true);
        }

    }
}
