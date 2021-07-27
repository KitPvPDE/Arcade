package net.kitpvp.plugins.arcade.game.status;

import net.kitpvp.gameapi.GameSettings;
import net.kitpvp.gameapi.GameState;
import net.kitpvp.network.chat.ChatFormats;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.chat.Chat;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.ArcadeGameStatus;
import net.kitpvp.plugins.arcade.game.InGameStatus;
import net.kitpvp.plugins.kitpvpcore.bukkit.Bukkit;
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
                Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.lobby.starting.now",
                        this.getGame().getParticipants().size(), this.getGame().getConfiguration().getMaxPlayers());

                Bukkit.playSound(Sound.NOTE_PLING, 1, 1.5);
                this.getGame().getModeQueue().poll();
            } else {
                this.resetCounter();

                Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
            }
        } else if (j > 0 && j % 60 == 0) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.lobby.starting.minutes",
                    this.getGame().getParticipants().size(), this.getGame().getConfiguration().getMaxPlayers(), j / 60);

            Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
        } else if (j > 0 && j % 10 == 0 || j <= 5) {
            Chat.localeAnnounce(ChatFormats.ARCADE, "arcade.lobby.starting.seconds",
                    this.getGame().getParticipants().size(), this.getGame().getConfiguration().getMaxPlayers(), j);

            if(j < 3) {
                Bukkit.playSound(Sound.NOTE_PLING, 1, 1);
            } else if(j >= 10) {
                Bukkit.playSound(Sound.NOTE_BASS, 1, 1);
            }
        }
    }
}
