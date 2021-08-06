package net.kitpvp.plugins.arcade.util.jnr;

import java.util.List;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.item.Checkpoint;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.arcade.util.Pair;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class CheckpointHelper {

    public static boolean hasVisitedCheckpoint(SessionBlock sessionBlock, int checkpointNumber) {
        // retrieve all checkpoints that this session has seen
        List<Pair<Block, Integer>> checkpoints = sessionBlock.getAttr(ArcadeAttributes.JNR_CHECKPOINT_HISTORY);

        for (Pair<Block, Integer> checkpoint : checkpoints) {
            // check if any of the block count of the checkpoints match the searched one
            if (checkpoint.getSecond() == checkpointNumber) {
                return true;
            }
        }
        return false;
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
            if(previousCheckpoint != null) {
                player.getInventory().removeItem(previousCheckpoint.getItems());
            }
        }
        // add all items corresponding to the checkpoint
        player.getInventory().addItem(checkpoint.getItems());
    }

    public static void setCustomCheckpointAtCurrentLocation(Player player, SessionBlock sessionBlock) {
        Block targetCheckpoint = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        // retrieve all checkpoints that this session has seen
        List<Pair<Block, Integer>> checkpointHistory = sessionBlock.getAttr(
            ArcadeAttributes.JNR_CHECKPOINT_HISTORY);

        // take the last checkpoint of the player
        Pair<Block, Integer> lastCheckPoint = lastOrNull(checkpointHistory);
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
        setNewCheckpoint(targetCheckpoint, sessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_COUNT), sessionBlock);
    }

    public static void setNewCheckpoint(Block checkpoint, int blockCount, SessionBlock sessionBlock) {
        // retrieve all checkpoints that this session has seen
        List<Pair<Block, Integer>> checkpoints = sessionBlock.getAttr(ArcadeAttributes.JNR_CHECKPOINT_HISTORY);
        // add the new checkpoint into the history
        checkpoints.add(new Pair<>(checkpoint, blockCount));
        // update the list in the attributes
        sessionBlock.setAttr(ArcadeAttributes.JNR_CHECKPOINT_HISTORY, checkpoints);
    }

    private static <E> E lastOrNull(List<E> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

}
