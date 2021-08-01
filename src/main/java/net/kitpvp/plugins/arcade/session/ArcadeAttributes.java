package net.kitpvp.plugins.arcade.session;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntUnaryOperator;
import net.kitpvp.network.namespace.NamespacedKey;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.Attribute;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.BooleanAttribute;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.IntAttribute;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;

public interface ArcadeAttributes {

    IntUnaryOperator COUNT_UP = (i) -> ++i;

    // JNR
    Attribute<Block> JNR_ACTIVE_BLOCK = Attribute.attribute(
        NamespacedKey.key("jnr:active_block"), null);
    Attribute<List<Block>> JNR_BLOCK_HISTORY = Attribute.attribute(NamespacedKey.key("jnr:block_history"),
        ArrayList::new);
    Attribute<DyeColor> JNR_BLOCK_COLOR = Attribute.attribute(NamespacedKey.key("jnr:block_color"), DyeColor.WHITE);
    IntAttribute JNR_BLOCK_COUNT = IntAttribute.attribute(NamespacedKey.key("jnr:block_count"));
    Attribute<Block> JNR_LAST_CHECKPOINT = Attribute.attribute(NamespacedKey.key("jnr:last_checkpoint"), null);
    IntAttribute CHECKPOINT_COUNT = IntAttribute.attribute(NamespacedKey.key("jnr:checkpoints"), 0);
    BooleanAttribute JNR_DEATHMATCH = BooleanAttribute.attribute(NamespacedKey.key("jnr:deathmatch"), false);


}
