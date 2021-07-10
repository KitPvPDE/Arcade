package net.kitpvp.plugins.arcade.game;

import lombok.Getter;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.kitpvpcore.utils.SpigotUtils;
import net.kitpvp.plugins.kitpvpcore.utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import static net.kitpvp.plugins.kitpvpcore.utils.YamlUtils.locationFromConfig;
import static net.kitpvp.plugins.kitpvpcore.utils.YamlUtils.vectorFromConfig;

public class ArcadeConfiguration {

    @Getter
    private final int lobbyTime, maxPlayers, minPlayers;

    @Getter
    private final Location jnrStart, jnrSpawn;

    public ArcadeConfiguration(ArcadePlugin plugin) {
        plugin.saveDefaultConfig();

        FileConfiguration configuration = plugin.getConfig();
        // general configuration
        this.lobbyTime = configuration.getInt("arcade.lobby.time", 60);
        this.maxPlayers = configuration.getInt("arcade.max-players", 100);
        this.minPlayers = configuration.getInt("arcade.min-players", 2);

        // lobby configuration

        // jnr configuration
        this.jnrStart = locationFromConfig(configuration, "arcade.jnr.start", Bukkit.getWorlds().get(0));
        this.jnrSpawn = locationFromConfig(configuration, "arcade.jnr.spawn", Bukkit.getWorlds().get(0));
    }
}
