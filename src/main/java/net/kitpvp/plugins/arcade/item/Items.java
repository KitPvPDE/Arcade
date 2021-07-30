package net.kitpvp.plugins.arcade.item;

import net.kitpvp.plugins.kitpvp.modules.items.ClickableItem;
import net.kitpvp.plugins.kitpvpcore.lavendle.LavendleItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

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
            .build();
    }
}
