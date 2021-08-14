package net.kitpvp.plugins.arcade.game;

import lombok.Getter;
import net.kitpvp.gameapi.Game;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.game.status.JnrStatus;
import net.kitpvp.plugins.arcade.game.status.SpeedMushroomStatus;
import net.kitpvp.plugins.kitpvp.modules.listener.register.GlobalEventRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ArcadeGame extends Game<ArcadeCategory> {

    @Getter
    private final ArcadePlugin plugin;
    @Getter
    private final ArcadeConfiguration configuration;
    @Getter
    private final Queue<InGameStatus> modeQueue;
    @Getter
    private ArcadeCategory state;

    public ArcadeGame(ArcadePlugin plugin) {
        this.plugin = plugin;
        this.configuration = new ArcadeConfiguration(plugin);
        this.modeQueue = new ConcurrentLinkedQueue<>();
        this.modeQueue.add(new JnrStatus(this, this.configuration.getJnrConfiguration().getJumpTime())); // for now TODO load randomized subset of all modes
     //   this.modeQueue.add(new SpeedMushroomStatus(this));
    }

    public boolean switchStatus(@NotNull ArcadeGameStatus newStatus) {
        if (super.switchStatus(newStatus)) {
            this.state = newStatus.getArcadeCategory();
            return true;
        }
        return false;
    }

    @Override
    public GlobalEventRegister<ArcadeCategory> getEventRegister() {
        return this.plugin.getEventRegister();
    }
}
