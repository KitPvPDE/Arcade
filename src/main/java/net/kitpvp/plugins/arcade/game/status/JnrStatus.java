package net.kitpvp.plugins.arcade.game.status;

import java.util.List;
import java.util.Random;
import net.kitpvp.gameapi.GameSettings;
import net.kitpvp.network.chat.ChatFormats;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.game.ArcadeConfiguration.JNRConfiguration;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.InGameStatus;
import net.kitpvp.plugins.arcade.item.Items.JNR;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JnrStatus extends InGameStatus {

    private static final DyeColor[] DYE_COLORS = DyeColor.values();
    private static final Random RANDOM = new Random();

    private final JNRConfiguration configuration;

    public JnrStatus(ArcadeGame game, int start) {
        super(game, COUNT_DOWN, start);
        this.configuration = game.getConfiguration().getJnrConfiguration();
    }

    @Override
    public ArcadeCategory getArcadeCategory() {
        return ArcadeCategory.JNR;
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        // disable damaging other players while jumping
        GameSettings.setFoodChangeAllowed(false);
        GameSettings.setDamageAllowed(false);

        // for now every player starts at the same point
        Block startBlock = this.configuration.getJnrStart().getBlock();
        startBlock.setType(Material.GOLD_BLOCK);
        for (Player player : super.getGame().getParticipants()) {
            player.teleport(this.configuration.getJnrSpawn());

            ArcadeUser user = ArcadeUser.getUser(player);
            SessionBlock block = user.getSession(ArcadeCategory.JNR);
            // set the start block for the player
            block.setAttr(ArcadeAttributes.JNR.ACTIVE_BLOCK, startBlock);

            if (this.configuration.isColorBlocks()) {
                // choose a random color and assign it to the player
                block.setAttr(ArcadeAttributes.JNR.BLOCK_COLOR, DYE_COLORS[RANDOM.nextInt(DYE_COLORS.length)]);
            }

            player.getInventory().clear();
            player.getInventory().setItem(4, JNR.CHECKPOINT.getItem(player));
        }
    }

    @Override
    protected void tickSecond(int j) {
        if (!this.getGame().getPlugin().getGlobalSession().getAttr(ArcadeAttributes.JNR.DEATHMATCH)) {
            if (j == 0) {
                Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.jnr.deathmatch.now");
                this.startDeathMatch();
            } else if (j > 0 && j % 60 == 0) {
                Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.jnr.deathmatch.minutes", j / 60);

                Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
            } else if (j > 0 && j % 10 == 0 && j < 60 || j <= 5) {
                Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.jnr.deathmatch.seconds", j);
                if (j < 3) {
                    Bukkit.playSound(Sound.NOTE_PLING, 1, 1);
                } else if (j >= 10) {
                    Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
                }
            }
        }
    }

    private void startDeathMatch() {
        // set the deathmatch in the global session, to prevent some events
        this.getGame().getPlugin().getGlobalSession().setAttr(ArcadeAttributes.JNR.DEATHMATCH, true);

        GameSettings.setDamageAllowed(true);
        GameSettings.setFoodChangeAllowed(true);

        Location fallbackLocation = this.configuration.getDeathMatchFallBackSpawn();
        List<Player> participants = this.getGame().getParticipants();
        List<Location> spawns = this.configuration.getDeathMatchSpawns();
        for (int i = 0; i < participants.size(); i++) {
            Player player = participants.get(i);
            if (spawns.size() - 1 >= i) {
                player.teleport(spawns.get(i));
            } else {
                player.teleport(fallbackLocation);
            }

            int doneJumps = ArcadeUser.getUser(player).getSession(ArcadeCategory.JNR)
                .getAttr(ArcadeAttributes.JNR.BLOCK_COUNT);

            // give the player the amount of done jumps in recraft
            player.getInventory().addItem(
                new ItemStack(Material.RED_MUSHROOM, doneJumps),
                new ItemStack(Material.BOWL, doneJumps),
                new ItemStack(Material.BROWN_MUSHROOM, doneJumps)
            );
            player.getInventory().remove(JNR.CHECKPOINT.getItem(player));
        }
    }
}
