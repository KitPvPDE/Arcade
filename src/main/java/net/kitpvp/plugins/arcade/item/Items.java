package net.kitpvp.plugins.arcade.item;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.listener.jnr.GameplayListener.ArcadeItemListener;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.kitpvp.modules.items.ClickableItem;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.lavendle.LavendleItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Items {

    public interface Lobby {

        ClickableItem PROFILE = ClickableItem.builder()
            .item(new LavendleItem(Material.SKULL_ITEM, 1, 3))
            .name("" + ChatColor.RED + "Profile")
            .callback(player -> player.performCommand("profile"))
            .build();
        ClickableItem STATS = ClickableItem.builder()
            .item(new LavendleItem(Material.BOOK))
            .name("" + ChatColor.RED + "Statistics")
            .callback(player -> player.performCommand("acradestats"))
            .build();
    }

    public interface JNR {

        ClickableItem CHECKPOINT = ClickableItem.builder()
            .item(new LavendleItem(Material.CARPET))
            .name(ChatColor.RED + "Checkpoint")
            .rightClick(true)
            .leftClick(true)
            .listener(ArcadeItemListener::new)
            .callback(player -> Bukkit.broadcastMessage("CALLED PLAYER"))
            .clickTest(playerInteractEvent -> {
                Bukkit.broadcastMessage("CALLED");
                Action action = playerInteractEvent.getAction();
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    return false;
                }
                Player player = playerInteractEvent.getPlayer();
                ArcadeUser arcadeUser = ArcadeUser.getUser(player);

                Block targetCheckPoint = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                SessionBlock userSessionBlock = arcadeUser.getSession(ArcadeCategory.JNR);
                Block sourceCheckPoint = userSessionBlock.getAttr(ArcadeAttributes.JNR_LAST_CHECKPOINT);
                if (targetCheckPoint.getType() == Material.AIR) {
                    Bukkit.broadcastMessage("AIR");
                    return false;
                }
                if (targetCheckPoint.equals(sourceCheckPoint)) {
                    return false;
                }
                Chat.localeResponse(player, "arcade.jnr.checkpoint.set");
                userSessionBlock.setAttr(ArcadeAttributes.JNR_LAST_CHECKPOINT, targetCheckPoint);
                userSessionBlock.setAttr(ArcadeAttributes.CHECKPOINT_COUNT, ArcadeAttributes.COUNT_UP);
                return true;
            })
            .build();
    }
}
