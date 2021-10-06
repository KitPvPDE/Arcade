package net.kitpvp.plugins.arcade.util.jnr;

import java.util.List;
import java.util.Map;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.item.Checkpoint;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes.JNR;
import net.kitpvp.plugins.arcade.util.Pair;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.lavendle.LavendleItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class CheckpointHelper {

    public static boolean hasVisitedCheckpoint(SessionBlock sessionBlock, int checkpointNumber) {
        // retrieve all checkpoints that this session has seen
        Map<Integer, Block> checkpoints = sessionBlock.getAttr(JNR.CHECKPOINT_HISTORY);
        return checkpoints.containsKey(checkpointNumber);
    }

    public static void applyCheckpointItems(Player player, int checkpointNumber) {
        Checkpoint checkpoint = Checkpoint.byOrdinal(checkpointNumber);
        // check if this checkpoint really exists
        if (checkpoint == null) {
            return;
        }
        // check if it's not the first checkpoint
        if (checkpointNumber != 0) {
            Checkpoint previousCheckpoint = Checkpoint.byOrdinal(checkpointNumber - 1);
            // should never be null but yeah
            if (previousCheckpoint != null) {
                for (LavendleItem item : previousCheckpoint.getItems()) {
                    // check if the item should be removed
                    if (!item.hasItemMeta() || !item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                        player.getInventory().removeItem(item);
                    }
                }
            }
        }
        // add all items corresponding to the checkpoint
        player.getInventory().addItem(checkpoint.getItems());
    }

    public static void setCustomCheckpointAtCurrentLocation(Player player, SessionBlock sessionBlock) {
        Block targetCheckpoint = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        // take the last checkpoint of the player
        Pair<Block, Integer> lastCheckPoint = sessionBlock.getAttr(JNR.LAST_CHECKPOINT);
        // if the player tries to great a checkpoint while in air we cancel the process
        if (targetCheckpoint.getType() == Material.AIR) {
            return;
        }
        // the player shouldn't create checkpoints on ground level
        if (targetCheckpoint.getY() == ArcadePlugin.getPlugin().getGame().getConfiguration().getJnrConfiguration()
            .getJnrStart().getBlockY()) {
            return;
        }
        // the player should not create the same checkpoint again
        if (lastCheckPoint != null && targetCheckpoint.equals(lastCheckPoint.getFirst())) {
            return;
        }
        // inform the player about his new checkpoint
        Chat.localeResponse(player, "arcade.jnr.checkpoint.set");

        // set the new checkpoint into the attributes
        setNewCheckpoint(targetCheckpoint, sessionBlock.getAttr(JNR.BLOCK_COUNT), sessionBlock);
    }

    public static void setNewCheckpoint(Block checkpoint, int blockCount, SessionBlock sessionBlock) {
        // retrieve all checkpoints that this session has seen
        Map<Integer, Block> checkpoints = sessionBlock.getAttr(JNR.CHECKPOINT_HISTORY);
        // add the new checkpoint into the history
        checkpoints.put(blockCount, checkpoint);
        // update the list in the attributes
        sessionBlock.setAttr(JNR.CHECKPOINT_HISTORY, checkpoints);
        // set the last checkpoint of the player
        sessionBlock.setAttr(JNR.LAST_CHECKPOINT, new Pair<>(checkpoint, blockCount));
    }

    private static <E> E lastOrNull(List<E> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

}
