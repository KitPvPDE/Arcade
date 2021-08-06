package net.kitpvp.plugins.arcade.item;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.listener.jnr.GameplayListener.ArcadeItemListener;
import net.kitpvp.plugins.arcade.util.jnr.CheckpointHelper;
import net.kitpvp.plugins.kitpvp.modules.items.ClickableItem;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.lavendle.LavendleItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

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
            .callback(player -> {
                SessionBlock jnrSession = ArcadeUser.getUser(player).getSession(ArcadeCategory.JNR);
                // check if the player has no checkpoint if so set a new one at his current location
                CheckpointHelper.setCustomCheckpointAtCurrentLocation(player, jnrSession);
            })
            .build();

        LavendleItem[] CHECKPOINT_1 = new LavendleItem[]{
            new LavendleItem(Material.GOLD_HELMET),
            new LavendleItem(Material.CHAINMAIL_CHESTPLATE),
            new LavendleItem(Material.GOLD_LEGGINGS),
            new LavendleItem(Material.GOLD_BOOTS),
            new LavendleItem(Material.STONE_SWORD),
            new LavendleItem(Material.TNT, 3)
        };
        LavendleItem[] CHECKPOINT_2 = new LavendleItem[]{
            new LavendleItem(Material.CHAINMAIL_HELMET),
            new LavendleItem(Material.CHAINMAIL_CHESTPLATE),
            new LavendleItem(Material.CHAINMAIL_LEGGINGS),
            new LavendleItem(Material.CHAINMAIL_BOOTS),
            new LavendleItem(Material.IRON_SWORD),
            new LavendleItem(Material.WEB, 4)
        };
        LavendleItem[] CHECKPOINT_3 = new LavendleItem[]{
            new LavendleItem(Material.CHAINMAIL_HELMET),
            new LavendleItem(Material.IRON_CHESTPLATE),
            new LavendleItem(Material.CHAINMAIL_LEGGINGS),
            new LavendleItem(Material.CHAINMAIL_BOOTS),
            new LavendleItem(Material.DIAMOND_SWORD),
            new LavendleItem(Material.FLINT_AND_STEEL)
        };
        LavendleItem[] CHECKPOINT_4 = new LavendleItem[]{
            new LavendleItem(Material.CHAINMAIL_HELMET),
            new LavendleItem(Material.IRON_CHESTPLATE),
            new LavendleItem(Material.IRON_LEGGINGS),
            new LavendleItem(Material.CHAINMAIL_BOOTS),
            new LavendleItem(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_ALL, 1)
        };
    }
}
