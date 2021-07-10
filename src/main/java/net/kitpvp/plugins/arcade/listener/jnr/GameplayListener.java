package net.kitpvp.plugins.arcade.listener.jnr;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.factory.ArcadeEvent;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.kitpvp.events.movement.PlayerMoveEvent;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.EntityDamageEvent;

public class GameplayListener implements Listener {

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onDmg(EntityDamageEvent event, User user) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {

        }
    }


    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onFallDown(PlayerMoveEvent event, ArcadeUser user) {
        Block standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        Block generated = user.getSession(ArcadeCategory.JNR).getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK);

        if (generated != null) {
            if (standingOn.getY() < generated.getY()) {
                // player fell down

            }
        }
    }
}
