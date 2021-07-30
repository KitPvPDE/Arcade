package net.kitpvp.plugins.arcade;

import java.util.logging.Level;
import lombok.Getter;
import net.kitpvp.lavendle.LavendlePlugin;
import net.kitpvp.network.api.ServerStatus;
import net.kitpvp.plugins.arcade.factory.ArcadeListenerFactory;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.status.LobbyStatus;
import net.kitpvp.plugins.kitpvp.modules.listener.register.GlobalEventRegister;
import net.kitpvp.plugins.kitpvpcore.KitPvPCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@LavendlePlugin(name = "arcade")
public class ArcadePlugin extends JavaPlugin {

    @Getter
    private static ArcadePlugin plugin;

    @Getter
    private KitPvPCore core;
    @Getter
    private GlobalEventRegister<ArcadeCategory> eventRegister;
    @Getter
    private ArcadeGame game;

    @Override
    public void onLoad() {
        plugin = this;

        this.core = KitPvPCore.getInstance();
        this.core.setUserFactory(ArcadeUser.USER_FACTORY);
        this.eventRegister = GlobalEventRegister.newEventRegister(null);
        this.core.getListenerRegister().registerFactory(new ArcadeListenerFactory(this.eventRegister));
    }

    @Override
    public void onEnable() {
        try {
            this.game = new ArcadeGame(this);
        } catch (Throwable cause) {
            getLogger().log(Level.SEVERE, "Could not create game", cause);
            Bukkit.shutdown();
            return;
        }
        this.game.switchStatus(new LobbyStatus(this.game, this.game.getConfiguration().getLobbyTime()));
        this.core.getListenerRegister().registerListenersRecursive("net.kitpvp.plugins.arcade.listener", this);

        KitPvPCore.getInstance().setServerStatus(ServerStatus.AVAILABLE);
    }

    @Override
    public void onDisable() {

    }
}
