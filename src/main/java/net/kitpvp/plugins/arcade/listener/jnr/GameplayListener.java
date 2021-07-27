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
    public void onEntityDamage(EntityDamageEvent event, User user) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onPlayerMove(PlayerMoveEvent event, ArcadeUser user) {
        Block standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        Block generated = user.getSession(ArcadeCategory.JNR).getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK);

        System.out.println(generated);
        if (generated != null) {
            if (standingOn.getY() < generated.getY()) {
                System.out.println("Fell down");
                // player fell down
                return;
            }
            if(standingOn.getY() == generated.getY()) {
                //generate new block for the player
                System.out.println("generate new block");


            }
        }
    }
}
