package net.kitpvp.plugins.arcade.game.status;

import net.kitpvp.gameapi.GameSettings;
import net.kitpvp.gameapi.GameState;
import net.kitpvp.network.chat.ChatFormats;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.ArcadeGameStatus;
import net.kitpvp.plugins.arcade.game.InGameStatus;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LobbyStatus extends ArcadeGameStatus {

    public LobbyStatus(ArcadeGame game, int start) {
        super(game, GameState.LOBBY, start);
    }

    @Override
    public ArcadeCategory getArcadeCategory() {
        return ArcadeCategory.LOBBY;
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        GameSettings.setDamageAllowed(false);
        GameSettings.setFoodChangeAllowed(false);
    }

    @Override
    protected void onExit() {
        super.onExit();

        GameSettings.setDamageAllowed(true);
        GameSettings.setFoodChangeAllowed(true);
    }

    @Override
    protected void tickSecond(int j) {
        if (j == 0) {
            InGameStatus status = this.getGame().getModeQueue().peek();
            if (status == null) {
                throw new IllegalStateException("playable status not available");
            } else if (this.getGame().switchStatus(status)) {
                Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.lobby.start.now.msg",
                        this.getGame().getParticipants().size(), this.getGame().getConfiguration().getMaxPlayers());

                this.getGame().getModeQueue().poll();
            }
        } else if (j > 0 && j % 60 == 0) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.lobby.start.minutes.msg",
                    this.getGame().getParticipants().size(), this.getGame().getConfiguration().getMaxPlayers(), j / 60);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            }
        } else if (j > 0 && j % 10 == 0 || j <= 5) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.lobby.start.seconds.msg",
                    this.getGame().getParticipants().size(), this.getGame().getConfiguration().getMaxPlayers(), j);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            }
        }
    }
}
