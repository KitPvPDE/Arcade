package net.kitpvp.plugins.arcade.listener.lobby;

import lombok.RequiredArgsConstructor;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.factory.ArcadeEvent;
import net.kitpvp.plugins.arcade.item.Items;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

@RequiredArgsConstructor
public class GameplayListener implements Listener {

    private final ArcadePlugin plugin;

    @ArcadeEvent(category = ArcadeCategory.LOBBY)
    public void onJoin(PlayerJoinEvent event, User user) {
        Chat.localeAnnounce("arcade.join.msg",
                user.getNameNick(), this.plugin.getGame().getParticipants().size(), this.plugin.getGame().getConfiguration().getMaxPlayers());

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        inventory.setItem(0, Items.Lobby.STATS.getItem(player));
        inventory.setItem(1, Items.Lobby.PROFILE.getItem(player));
    }

    @ArcadeEvent(category = ArcadeCategory.LOBBY)
    public void onLeave(PlayerQuitEvent event, User user) {
        Chat.localeAnnounce("arcade.quit.msg",
                user.getNameNick(), this.plugin.getGame().getParticipants().size(), this.plugin.getGame().getConfiguration().getMaxPlayers());
    }
}
