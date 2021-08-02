package net.kitpvp.plugins.arcade.listener;

import lombok.RequiredArgsConstructor;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.kitpvp.modules.listener.annotation.Calls;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

    @EventHandler
    @Calls(EntityDamageEvent.class)
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            User user = User.getUser((Player) event.getEntity());
            this.plugin.getEventRegister().callEvent(EntityDamageEvent.class, event, user, this.plugin.getGame().getState());
        }
    }

    @EventHandler
    @Calls(PlayerMoveEvent.class)
    public void handlePlayerMove(PlayerMoveEvent event) {
        User user = User.getUser(event.getPlayer());
        this.plugin.getEventRegister().callEvent(PlayerMoveEvent.class, event, user, this.plugin.getGame().getState());
    }

    @EventHandler
    @Calls(BlockPlaceEvent.class)
    public void handleBlockPlace(BlockPlaceEvent event) {
        User user = User.getUser(event.getPlayer());
        this.plugin.getEventRegister().callEvent(BlockPlaceEvent.class, event, user, this.plugin.getGame().getState());
    }

    @EventHandler
    @Calls(PlayerDropItemEvent.class)
    public void handleItemDrop(PlayerDropItemEvent event) {
        User user = User.getUser(event.getPlayer());
        this.plugin.getEventRegister().callEvent(PlayerDropItemEvent.class, event, user, this.plugin.getGame().getState());
    }
}
