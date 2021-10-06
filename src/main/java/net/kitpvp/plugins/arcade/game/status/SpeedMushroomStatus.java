package net.kitpvp.plugins.arcade.game.status;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.InGameStatus;
import org.bukkit.entity.Player;

public class SpeedMushroomStatus extends InGameStatus {

    public SpeedMushroomStatus(ArcadeGame game) {
        super(game);
    }

    @Override
    public ArcadeCategory getArcadeCategory() {
        return ArcadeCategory.MUSHROOM;
    }

    @Override
    protected void onEnter() {
        super.onEnter();
        for (Player player : super.getGame().getParticipants()) {
            //TODO: teleport the players onto the mushroom map
        }
    }
}
