package net.kitpvp.plugins.arcade.session;

import net.kitpvp.network.namespace.NamespacedKey;
import net.kitpvp.plugins.kitpvp.modules.session.attribute.Attribute;
import org.bukkit.block.Block;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface ArcadeAttributes {

    Attribute<Block> JNR_ACTIVE_BLOCK = Attribute.attribute(
            NamespacedKey.key("jnr:active_block"), null);
    Attribute<Queue<Block>> JNR_BLOCK_HISTORY = Attribute.attribute(
            NamespacedKey.key("jnr:block_history"), ConcurrentLinkedQueue::new);


}
