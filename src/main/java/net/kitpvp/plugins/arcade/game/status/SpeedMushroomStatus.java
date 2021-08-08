package net.kitpvp.plugins.arcade.game.status;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.InGameStatus;

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
    }
}
