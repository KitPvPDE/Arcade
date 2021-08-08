package net.kitpvp.plugins.arcade.session;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntUnaryOperator;
import net.kitpvp.network.namespace.NamespacedKey;
import net.kitpvp.plugins.arcade.util.Pair;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.Attribute;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.BooleanAttribute;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.IntAttribute;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;

public interface ArcadeAttributes {

    // just count up
    IntUnaryOperator COUNT_UP = (i) -> ++i;

    // stores the next block the player has to reach
    Attribute<Block> JNR_ACTIVE_BLOCK = Attribute.attribute(
        NamespacedKey.key("jnr:active_block"), null);
    // stores all blocks the player has done
    Attribute<List<Block>> JNR_BLOCK_HISTORY = Attribute.attribute(NamespacedKey.key("jnr:block_history"),
        ArrayList::new);
    // looks the session so that we don't generate more than one new block
    BooleanAttribute JNR_LOCKED = BooleanAttribute.attribute(NamespacedKey.key("jnr:block_locked"), false);
    // stores the players glass color
    Attribute<DyeColor> JNR_BLOCK_COLOR = Attribute.attribute(NamespacedKey.key("jnr:block_color"), DyeColor.WHITE);
    // stores how many blocks the player reached
    IntAttribute JNR_BLOCK_COUNT = IntAttribute.attribute(NamespacedKey.key("jnr:block_count"));
    // stores all checkpoints of the player
    Attribute<List<Pair<Block, Integer>>> JNR_CHECKPOINT_HISTORY = Attribute.attribute(NamespacedKey.key("jnr:checkpoint_history"), ArrayList::new);
    // Stores if the game is in the deathmatch, this uses the global session
    BooleanAttribute JNR_DEATHMATCH = BooleanAttribute.attribute(NamespacedKey.key("jnr:deathmatch"), false);


}
