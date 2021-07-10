package net.kitpvp.plugins.arcade.game;

import net.kitpvp.gameapi.GameState;
import net.kitpvp.plugins.arcade.ArcadeCategory;

public abstract class InGameStatus extends ArcadeGameStatus {

    public InGameStatus(ArcadeGame game) {
        super(game, GameState.INGAME, COUNT_UP, 0);
    }

    @Override
    public abstract ArcadeCategory getArcadeCategory();

    @Override
    protected void tickSecond(int j) {

    }

}
