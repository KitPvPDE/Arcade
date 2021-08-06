package net.kitpvp.plugins.arcade.util.jnr;

import java.util.List;
import java.util.Random;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.game.ArcadeConfiguration.JNRConfiguration;
import net.kitpvp.plugins.arcade.game.jnr.JNRLevel;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.utils.SpigotUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class LocationGenerator {

    private static final Random RANDOM = new Random();
    private static final BlockFace[] BLOCK_FACES = BlockFace.values();

    private static final double MAX_RANGE = 4.7D;
    private static final double MAX_BLOCK_RANGE = 5.1D;
    private static final int SAFE_CAP = 15;

    public static void generateNewJump(ArcadeUser arcadeUser, JNRConfiguration jnrConfiguration) {
        SessionBlock currentSessionBlock = arcadeUser.getSession(ArcadeCategory.JNR);
        // fetch the old block from the players attributes
        Block currentBlock = currentSessionBlock.getAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK);

        // generate new block based on the level
        JNRLevel jnrLevel = JNRLevel.levelByJumps(currentSessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_COUNT));
        Block generatedBlock = generateSafeLocation(currentBlock.getLocation(), jnrLevel, jnrConfiguration).getBlock();

        // look the session to prevent multiple blocks
        currentSessionBlock.setAttr(ArcadeAttributes.JNR_LOCKED, true);

        // update the block in the world and the attributes of the player
        updateBlock(currentSessionBlock, generatedBlock, currentBlock, jnrConfiguration);

    }

    private static void updateBlock(SessionBlock sessionBlock, Block generatedBlock, Block currentBlock,
        JNRConfiguration jnrConfiguration) {

        // calculate the delay in ticks (20 ticks = 1s) the player has to wait for a new block based on the amount of checkpoints he has set
        long delay = (long) sessionBlock.getAttr(ArcadeAttributes.JNR_CHECKPOINT_HISTORY).size()
            * jnrConfiguration.getCheckpointDelay();

        SpigotUtils.runSyncDelayed(() -> {
            // we use stained glass and color it for each player
            generatedBlock.setType(Material.STAINED_GLASS);
            generatedBlock.setData(sessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_COLOR).getDyeData());

            List<Block> blockHistory = sessionBlock.getAttr(ArcadeAttributes.JNR_BLOCK_HISTORY);

            // we want to remove the last block in the blockHistory and also hide it in the world
            if (blockHistory.size() > 2) {
                blockHistory.get(1).setType(Material.AIR);
                blockHistory.remove(1);
            }
            // add the new block to the attributes
            blockHistory.add(currentBlock);

            // set the new block the player has to reach
            sessionBlock.setAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK, generatedBlock);

            // set the updated blockHistory
            sessionBlock.setAttr(ArcadeAttributes.JNR_BLOCK_HISTORY, blockHistory);

            // increment the amount of blocks the player jumped
            sessionBlock.setAttr(ArcadeAttributes.JNR_BLOCK_COUNT, ArcadeAttributes.COUNT_UP);

            // unlock the session as we are done now
            sessionBlock.setAttr(ArcadeAttributes.JNR_LOCKED, false);
        }, delay);
    }

    private static Location generateSafeLocation(Location sourceLocation, JNRLevel level,
        JNRConfiguration jnrConfiguration) {
        Location targetLocation = generateLocation(sourceLocation, level);

        // generate a new location in a loop as long it's not safe, and it does not take more than SAFE_CAP times to prevent loops
        int i = 0;
        while (!isLocationSafe(targetLocation, jnrConfiguration) && i < SAFE_CAP) {
            targetLocation = generateLocation(sourceLocation, level);
            i++;
        }
        return targetLocation;
    }

    private static Location generateLocation(Location sourceLocation, JNRLevel level) {
        int height = 1;

        // the very_easy and easy levels should always go up by one block, the others randomly (0 or 1)
        if (level.ordinal() > JNRLevel.EASY.ordinal()) {
            height = RANDOM.nextInt(2);
        }

        // generate a random block range using the level's block range
        double distance = level.getBlockRange() + RANDOM.nextInt(2);

        // use a custom generator for the hard level to generate more hard jumps
        if (level == JNRLevel.HARD) {
            distance = level.getBlockRange() + RANDOM.nextInt(6) / 10D;
        }

        // calculate a random degree in which the block is located
        int degree = RANDOM.nextInt(360);
        double radians = Math.toRadians(degree);
        double cos = distance * Math.cos(radians);
        double sin = distance * Math.sin(radians);

        Location targetLocation = sourceLocation.clone().add(sin, height, cos);

        double distanceSquared = targetLocation.toVector().distanceSquared(sourceLocation.toVector());
        // check if the distance of both locations is not too big
        if (distanceSquared > MAX_RANGE * MAX_RANGE) {
            return generateLocation(sourceLocation, level);
        }

        double blockDistanceSquared = targetLocation.getBlock().getLocation().toVector()
            .distanceSquared(sourceLocation.toVector());

        // check if the distance of both blocks is not too big
        if (blockDistanceSquared > MAX_BLOCK_RANGE * MAX_BLOCK_RANGE
            || Math.pow(level.getMinBlockRange(), 2) > blockDistanceSquared) {
            return generateLocation(sourceLocation, level);
        }

        return targetLocation;
    }

    private static boolean isLocationSafe(Location targetLocation, JNRConfiguration jnrConfiguration) {
        Block block = targetLocation.getBlock();

        Location jnrStart = jnrConfiguration.getJnrStart();
        Location jnrSpawn = jnrConfiguration.getJnrSpawn();

        // only check if the block is not on the ground to prevent infinite loops
        if (jnrSpawn.getBlockY() + 3 < targetLocation.getBlockY()
            && jnrStart.getBlockY() + 3 < targetLocation.getBlockY()) {

            for (int i = 0; i <= 3; i++) {
                //check if the block at the given relative block face is air, if the location is not safe
                if (block.getRelative(BlockFace.DOWN, i).getType() != Material.AIR) {
                    return false;
                }
            }

            for (BlockFace blockFace : BLOCK_FACES) {
                // ignore those block faces as they are already checked
                if (blockFace == BlockFace.DOWN || blockFace == BlockFace.UP || blockFace == BlockFace.SELF) {
                    continue;
                }
                //check if the block at the given relative block face is air, if the location is not safe
                if (block.getRelative(blockFace).getType() != Material.AIR) {
                    return false;
                }
            }
        }

        // always check if there is space above
        for (int i = 0; i <= 3; i++) {
            if (block.getRelative(BlockFace.UP, i).getType() != Material.AIR) {
                return false;
            }
        }
        // check if the targetBlock itself is air
        return targetLocation.getBlock().getType() == Material.AIR;
    }

}
