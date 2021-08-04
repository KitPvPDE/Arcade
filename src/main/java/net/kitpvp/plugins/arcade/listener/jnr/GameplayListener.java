package net.kitpvp.plugins.arcade.listener.jnr;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.factory.ArcadeEvent;
import net.kitpvp.plugins.arcade.game.ArcadeConfiguration.JNRConfiguration;
import net.kitpvp.plugins.arcade.game.jnr.JNRLevel;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.arcade.util.Pair;
import net.kitpvp.plugins.kitpvp.modules.items.ClickableItem;
import net.kitpvp.plugins.kitpvp.modules.items.ClickableItemListener;
import net.kitpvp.plugins.kitpvp.modules.listener.GlobalEvent;
import net.kitpvp.plugins.kitpvp.modules.listener.annotation.Debug;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.user.User;
import net.kitpvp.plugins.kitpvpcore.utils.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
public class GameplayListener implements Listener {

    private static final Random RANDOM = new Random();
    private static final BlockFace[] BLOCK_FACES = BlockFace.values();

    private static final double MAX_RANGE = 4.7D;
    private static final double MAX_BLOCK_RANGE = 5.1D;

    private final ArcadePlugin arcadePlugin;

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onEntityDamage(EntityDamageEvent event, User user) {
        if (this.arcadePlugin.getGlobalSession().getAttr(ArcadeAttributes.JNR_DEATHMATCH)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onItemDrop(PlayerDropItemEvent event, User user) {
        if (this.arcadePlugin.getGlobalSession().getAttr(ArcadeAttributes.JNR_DEATHMATCH)) {
            return;
        }
        event.setCancelled(true);
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onBlockPlaceEvent(BlockPlaceEvent event, User user) {
        event.setCancelled(true);
    }

    @ArcadeEvent(category = ArcadeCategory.JNR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.arcadePlugin.getGlobalSession().getAttr(ArcadeAttributes.JNR_DEATHMATCH)) {
            return;
        }

        ArcadeUser arcadeUser = ArcadeUser.getUser(event.getPlayer());

        Block standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN);

        SessionBlock jnrSession = arcadeUser.getSession(ArcadeCategory.JNR);

        Block generated = jnrSession.getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK);

        if (generated != null) {
            if (standingOn.getY() < generated.getY() - 3 && standingOn.getType() != Material.AIR) {
                if (jnrSession.getAttr(ArcadeAttributes.JNR_LAST_CHECKPOINT) == null) {
                    jnrSession.getAttr(ArcadeAttributes.JNR_BLOCK_HISTORY).removeIf(block -> {
                        block.setType(Material.AIR);
                        return true;
                    });
                    jnrSession.getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK).setType(Material.AIR);

                    //TODO: choose a new spawn for the player
                    Location startLocation = this.arcadePlugin.getGame().getConfiguration().getJnrConfiguration()
                        .getJnrStart();
                    event.getPlayer()
                        .teleport(startLocation);

                    // reset the done jumps
                    jnrSession.setAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK, startLocation.getBlock());
                    jnrSession.setAttr(ArcadeAttributes.JNR_BLOCK_COUNT, 0);
                } else {
                    Pair<Block, Integer> lastCheckpoint = jnrSession.getAttr(ArcadeAttributes.JNR_LAST_CHECKPOINT);

                    jnrSession.getAttr(ArcadeAttributes.JNR_BLOCK_HISTORY).removeIf(block -> {
                        block.setType(Material.AIR);
                        return true;
                    });
                    jnrSession.getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK).setType(Material.AIR);

                    // replace the old block
                    lastCheckpoint.first.setType(Material.STAINED_GLASS);
                    lastCheckpoint.first.setData(jnrSession.getAttr(ArcadeAttributes.JNR_BLOCK_COLOR).getDyeData());
                    event.getPlayer().teleport(lastCheckpoint.first.getRelative(BlockFace.UP).getLocation());

                    // reset the done jumps
                    jnrSession.setAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK, lastCheckpoint.first);
                    jnrSession.setAttr(ArcadeAttributes.JNR_BLOCK_COUNT, lastCheckpoint.second);
                }
                return;
            }
            if (standingOn.getLocation().toVector().equals(generated.getLocation().toVector())) {
                //generate new block for the player
                if (arcadeUser.getSession(ArcadeCategory.JNR).getAttr(ArcadeAttributes.JNR_LOCKED)) {
                    return;
                }

                this.generateNewJump(arcadeUser);
            }
        }
    }

    private void generateNewJump(ArcadeUser arcadeUser) {
        //fetching old blocks
        SessionBlock currentSessionBlock = arcadeUser.getSession(ArcadeCategory.JNR);
        Block currentBlock = currentSessionBlock.getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK);
        List<Block> blockHistory = currentSessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_HISTORY);

        // generate new block
        JNRLevel jnrLevel = JNRLevel.levelByJumps(currentSessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_COUNT));
        Block generatedBlock = this.generateSafeLocation(currentBlock.getLocation(), jnrLevel).getBlock();

        currentSessionBlock.setAttr(ArcadeAttributes.JNR_LOCKED, true);

        long delay = (long) currentSessionBlock.getAttr(ArcadeAttributes.CHECKPOINT_COUNT) *
            this.arcadePlugin.getGame().getConfiguration().getJnrConfiguration().getCheckpointDelay();

        SpigotUtils.runSyncDelayed(() -> {
            generatedBlock.setType(Material.STAINED_GLASS);
            generatedBlock.setData(currentSessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_COLOR).getDyeData());

            if (blockHistory.size() > 2) {
                blockHistory.get(1).setType(Material.AIR);
                blockHistory.remove(1);
            }

            // update player attributes
            blockHistory.add(currentBlock);
            currentSessionBlock.setAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK, generatedBlock);
            currentSessionBlock.setAttr(ArcadeAttributes.JNR_BLOCK_HISTORY, blockHistory);
            currentSessionBlock.setAttr(ArcadeAttributes.JNR_BLOCK_COUNT, ArcadeAttributes.COUNT_UP);
            currentSessionBlock.setAttr(ArcadeAttributes.JNR_LOCKED, false);
        }, delay);
    }

    //TODO: Check if generated jumps match the level
    private Location generateLocation(Location sourceLocation, JNRLevel level) {
        int height = 1;

        if (level.ordinal() > JNRLevel.EASY.ordinal()) {
            height = RANDOM.nextInt(2);
        }

        double distance = level.getBlockRange() + RANDOM.nextInt(2);

        if (level == JNRLevel.HARD) {
            distance = level.getBlockRange() + RANDOM.nextInt(6) / 10D;
        }

        int degree = RANDOM.nextInt(360);
        double radians = Math.toRadians(degree);
        double cos = distance * Math.cos(radians);
        double sin = distance * Math.sin(radians);

        Location targetLocation = sourceLocation.clone().add(sin, height, cos);

        double distanceSquared = targetLocation.toVector().distanceSquared(sourceLocation.toVector());
        if (distanceSquared > MAX_RANGE * MAX_RANGE) {
            return this.generateLocation(sourceLocation, level);
        }

        double blockDistanceSquard = targetLocation.getBlock().getLocation().toVector()
            .distanceSquared(sourceLocation.toVector());
        if (blockDistanceSquard > MAX_BLOCK_RANGE * MAX_BLOCK_RANGE
            || Math.pow(level.getMinBlockRange(), 2) > blockDistanceSquard) {
            return this.generateLocation(sourceLocation, level);
        }

        return targetLocation;
    }

    private Location generateSafeLocation(Location sourceLocation, JNRLevel level) {
        Location targetLocation = this.generateLocation(sourceLocation, level);

        int i = 0;
        while (!this.isSafeLocation(targetLocation) && i < 10) {
            targetLocation = this.generateLocation(sourceLocation, level);
            i++;
        }
        System.out.println("While stopped after " + i);
        return targetLocation;
    }

    private boolean isSafeLocation(Location targetLocation) {
        Block block = targetLocation.getBlock();

        JNRConfiguration arcadeConfiguration = this.arcadePlugin.getGame().getConfiguration().getJnrConfiguration();
        Location jnrStart = arcadeConfiguration.getJnrStart();
        Location jnrSpawn = arcadeConfiguration.getJnrSpawn();

        if (jnrSpawn.getBlockY() + 3 < targetLocation.getBlockY()
            && jnrStart.getBlockY() + 3 < targetLocation.getBlockY()) {
            if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                return false;
            }
            if (block.getRelative(BlockFace.DOWN, 2).getType() != Material.AIR) {
                return false;
            }
            if (block.getRelative(BlockFace.DOWN, 3).getType() != Material.AIR) {
                return false;
            }

            for (BlockFace blockFace : BLOCK_FACES) {
                if (blockFace == BlockFace.DOWN || blockFace == BlockFace.UP || blockFace == BlockFace.SELF) {
                    continue;
                }
                if (block.getRelative(blockFace).getType() != Material.AIR) {
                    return false;
                }
            }
        }

        if (block.getRelative(BlockFace.UP).getType() != Material.AIR) {
            return false;
        }
        if (block.getRelative(BlockFace.UP, 2).getType() != Material.AIR) {
            return false;
        }
        if (block.getRelative(BlockFace.UP, 3).getType() != Material.AIR) {
            return false;
        }
        if (targetLocation.getBlock().getType() != Material.AIR) {
            return false;
        }
        return true;
    }

    @Debug
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
