package net.kitpvp.plugins.arcade.game;

import java.util.function.IntUnaryOperator;
import net.kitpvp.gameapi.GameState;
import net.kitpvp.plugins.arcade.ArcadeCategory;

public abstract class InGameStatus extends ArcadeGameStatus {

    public InGameStatus(ArcadeGame game) {
        super(game, GameState.INGAME, COUNT_UP, 0);
    }

    public InGameStatus(ArcadeGame game, IntUnaryOperator unaryOperator, int start) {
        super(game, GameState.INGAME, unaryOperator, start);
    }

    @Override
    public abstract ArcadeCategory getArcadeCategory();

    @Override
    protected void tickSecond(int j) {

    }

}
