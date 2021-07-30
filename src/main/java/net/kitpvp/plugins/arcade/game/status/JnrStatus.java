package net.kitpvp.plugins.arcade.game.status;

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
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JnrStatus extends InGameStatus {

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

        Block startBlock = this.configuration.getJnrStart().getBlock();
        for (Player player : getGame().getParticipants()) {
            player.teleport(this.configuration.getJnrSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);

            ArcadeUser user = ArcadeUser.getUser(player);
            SessionBlock block = user.getSession(ArcadeCategory.JNR);
            block.setAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK, startBlock);

            player.getInventory().setItem(4, JNR.CHECKPOINT.getItem(player));
        }
    }

    @Override
    protected void tickSecond(int j) {
        if (j == 0) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.jnr.deathmatch.now");



        } else if (j > 0 && j % 60 == 0) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.jnr.deathmatch.minutes", j / 60);

            Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
        } else if (j > 0 && j % 10 == 0 || j <= 5) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.jnr.deathmatch.seconds", j);

            if(j < 3) {
                Bukkit.playSound(Sound.NOTE_PLING, 1, 1);
            } else if(j >= 10) {
                Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
            }
        }
    }
}
