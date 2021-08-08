package net.kitpvp.plugins.arcade.listener.jnr;

import java.util.List;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.factory.ArcadeEvent;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.arcade.util.Pair;
import net.kitpvp.plugins.arcade.util.jnr.CheckpointHelper;
import net.kitpvp.plugins.arcade.util.jnr.LocationGenerator;
import net.kitpvp.plugins.kitpvp.modules.items.ClickableItem;
import net.kitpvp.plugins.kitpvp.modules.items.ClickableItemListener;
import net.kitpvp.plugins.kitpvp.modules.listener.GlobalEvent;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameplayListener implements Listener {

    private final ArcadePlugin arcadePlugin;
    private final List<Integer> checkpoints;

    public GameplayListener(ArcadePlugin arcadePlugin) {
        this.arcadePlugin = arcadePlugin;
        this.checkpoints = arcadePlugin.getGame().getConfiguration().getJnrConfiguration().getCheckpoints();
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onEntityDamage(EntityDamageEvent event, User user) {
        // we want to enable fall damage in the deathmatch again
        if (this.arcadePlugin.getGlobalSession().getAttr(ArcadeAttributes.JNR.DEATHMATCH)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onItemDrop(PlayerDropItemEvent event, User user) {
        // we don't need to cancel this in the deathmatch as the player is allowed to drop things.
        if (this.arcadePlugin.getGlobalSession().getAttr(ArcadeAttributes.JNR.DEATHMATCH)) {
            return;
        }
        event.setCancelled(true);
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onBlockPlaceEvent(BlockPlaceEvent event, User user) {
        // the player should never be able to place a block in the jnr / deathmatch
        event.setCancelled(true);
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onPlayerMove(PlayerMoveEvent event) {
        // we don't need these checks in the deathmatch anymore
        if (this.arcadePlugin.getGlobalSession().getAttr(ArcadeAttributes.JNR.DEATHMATCH)) {
            return;
        }

        ArcadeUser arcadeUser = ArcadeUser.getUser(event.getPlayer());

        Block standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN);

        SessionBlock jnrSession = arcadeUser.getSession(ArcadeCategory.JNR);

        Block generated = jnrSession.getAttr(ArcadeAttributes.JNR.ACTIVE_BLOCK);

        if (generated != null) {
            // check if the player fell down
            if (standingOn.getY() < generated.getY() - 2 && standingOn.getType() != Material.AIR) {
                List<Pair<Block, Integer>> checkpointHistory = jnrSession.getAttr(
                    ArcadeAttributes.JNR.CHECKPOINT_HISTORY);

                // remove all blocks from the players' history as we generate new ones
                jnrSession.getAttr(ArcadeAttributes.JNR.BLOCK_HISTORY).removeIf(block -> {
                    block.setType(Material.AIR);
                    return true;
                });
                // set the current he had to reach also to air
                jnrSession.getAttr(ArcadeAttributes.JNR.ACTIVE_BLOCK).setType(Material.AIR);

                // the player has no checkpoints set or reached
                if (checkpointHistory.isEmpty()) {

                    //TODO: choose a new spawn for the player
                    Location startLocation = this.arcadePlugin.getGame().getConfiguration().getJnrConfiguration()
                        .getJnrStart();
                    event.getPlayer()
                        .teleport(startLocation);

                    // set the new block to reach for the player to the start block
                    jnrSession.setAttr(ArcadeAttributes.JNR.ACTIVE_BLOCK, startLocation.getBlock());
                    // reset the count of jumps he has completed
                    jnrSession.setAttr(ArcadeAttributes.JNR.BLOCK_COUNT, 0);
                } else {
                    // the newest checkpoint of the player
                    Pair<Block, Integer> lastCheckpoint = checkpointHistory.get(checkpointHistory.size() - 1);

                    // the block corresponding to the last checkpoint
                    Block checkpointBlock = lastCheckpoint.getFirst();

                    // set the block again to stained-glass and color it for the player
                    checkpointBlock.setType(Material.STAINED_GLASS);
                    checkpointBlock.setData(jnrSession.getAttr(ArcadeAttributes.JNR.BLOCK_COLOR).getDyeData());

                    // teleport the player to his last checkpoint
                    event.getPlayer().teleport(checkpointBlock.getRelative(BlockFace.UP).getLocation());

                    // set the new block toi reach to the checkpoints block
                    jnrSession.setAttr(ArcadeAttributes.JNR.ACTIVE_BLOCK, lastCheckpoint.getFirst());

                    // reset the count of jumps the player completed to those that he completed to reach this checkpoint
                    jnrSession.setAttr(ArcadeAttributes.JNR.BLOCK_COUNT, lastCheckpoint.getSecond());
                }
                return;
            }
            // check if the player is on the target block he has to reach
            if (standingOn.getLocation().toVector().equals(generated.getLocation().toVector())) {
                // check if there is no other generation running already
                if (arcadeUser.getSession(ArcadeCategory.JNR).getAttr(ArcadeAttributes.JNR.LOCKED)) {
                    return;
                }

                for (int i = 0; i < this.checkpoints.size(); i++) {
                    // the needed amount of jumps for this checkpoint
                    int checkpoint = this.checkpoints.get(i);
                    // the amount of blocks the player reached
                    int blockCount = jnrSession.getAttr(ArcadeAttributes.JNR.BLOCK_COUNT);

                    // check if the player reached the checkpoint, and he did not visit it before
                    if (blockCount == checkpoint && !CheckpointHelper.hasVisitedCheckpoint(jnrSession, checkpoint)) {
                        Chat.localeResponse(event.getPlayer(), "arcade.jnr.checkpoint.reached");
                        // give the player his items
                        CheckpointHelper.applyCheckpointItems(event.getPlayer(), i);
                        // set the new checkpoint for the player
                        CheckpointHelper.setNewCheckpoint(standingOn, blockCount, jnrSession);
                    }
                }
                // now we can generate a new jump for the player
                LocationGenerator.generateNewJump(arcadeUser,
                    this.arcadePlugin.getGame().getConfiguration().getJnrConfiguration());
            }
        }
    }

    public static class ArcadeItemListener extends ClickableItemListener {

        public ArcadeItemListener(ClickableItem item) {
            super(item);
        }

        @GlobalEvent
        public void onInteract(PlayerInteractEvent event) {
            this.checkEvent(event, User.getUser(event.getPlayer()));
        }
    }
}
