package net.kitpvp.plugins.arcade.listener;

import lombok.RequiredArgsConstructor;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.kitpvp.modules.listener.annotations.Calls;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class GameListener implements Listener {

    private final ArcadePlugin plugin;

    @EventHandler
    @Calls(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent event) {
        User user = User.getUser(event.getPlayer());
        this.plugin.getEventRegister().callEvent(event, user, this.plugin.getGame().getState());
    }

    @EventHandler
    @Calls(PlayerQuitEvent.class)
    public void onQuit(PlayerQuitEvent event) {
        User user = User.getUser(event.getPlayer());
        this.plugin.getEventRegister().callEvent(event, user, this.plugin.getGame().getState());
    }
}
