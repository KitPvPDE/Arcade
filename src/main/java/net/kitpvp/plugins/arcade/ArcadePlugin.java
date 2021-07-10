package net.kitpvp.plugins.arcade;

import lombok.Getter;
import net.kitpvp.lavendle.LavendlePlugin;
import net.kitpvp.plugins.arcade.factory.ArcadeCategoryFactory;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.status.LobbyStatus;
import net.kitpvp.plugins.kitpvp.modules.listener.factory.LambdaListenerFactory;
import net.kitpvp.plugins.kitpvp.modules.listener.register.GlobalEventRegister;
import net.kitpvp.plugins.kitpvpcore.KitPvPCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@LavendlePlugin(name = "arcade")
public class ArcadePlugin extends JavaPlugin {

    @Getter
    private KitPvPCore core;
    @Getter
    private GlobalEventRegister<ArcadeCategory> eventRegister;
    @Getter
    private ArcadeGame game;

    @Override
    public void onLoad() {
        this.core = KitPvPCore.getInstance();
        this.core.setUserFactory(ArcadeUser.USER_FACTORY);
        this.eventRegister = new GlobalEventRegister<>(ArcadeCategory::shortOrdinal);
        this.core.getListenerRegister().registerFactory(LambdaListenerFactory.newListenerFactory(this.eventRegister,
                ArcadeCategoryFactory.INSTANCE));
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
    }

    @Override
    public void onDisable() {

    }
}
