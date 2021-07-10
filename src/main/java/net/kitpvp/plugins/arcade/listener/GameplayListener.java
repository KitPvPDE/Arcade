package net.kitpvp.plugins.arcade.listener;

import lombok.RequiredArgsConstructor;
import net.kitpvp.gameapi.event.GameStatusChangeEvent;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.kitpvp.modules.listener.GlobalEvent;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class GameplayListener implements Listener {

    private final ArcadePlugin plugin;

    @GlobalEvent
    public void onJoin(PlayerJoinEvent event, User user) {
        if (this.plugin.getGame().getState() != ArcadeCategory.LOBBY) {
            if (!user.isInVanish()) {
                event.getPlayer().kickPlayer("You cannot join running games");
                return;
            }
        } else {
            this.plugin.getGame().addParticipant(user.getPlayerId());
        }
    }

    @GlobalEvent
    public void onQuit(PlayerQuitEvent event, User user) {
        if (this.plugin.getGame().isParticipant(user.getPlayerId())) {
            this.plugin.getGame().removeParticipant(user.getPlayerId());
        }
    }

    @GlobalEvent(priority = EventPriority.LOW)
    public void onStatusSwitch(GameStatusChangeEvent event) {
        if (this.plugin.getGame().getParticipants().size() <= this.plugin.getGame().getConfiguration().getMinPlayers()) {
            event.setCancelled(true);
        }
    }
}
