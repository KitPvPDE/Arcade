package net.kitpvp.plugins.arcade.game;

import java.util.function.IntUnaryOperator;
import net.kitpvp.gameapi.GameState;
import net.kitpvp.gameapi.status.CountingStatus;
import net.kitpvp.plugins.arcade.ArcadeCategory;

public abstract class ArcadeGameStatus extends CountingStatus {

    public ArcadeGameStatus(ArcadeGame game, GameState gameState, int start) {
        super(game, gameState, start);
    }

    public ArcadeGameStatus(ArcadeGame game, GameState gameState, IntUnaryOperator counter, int start) {
        super(game, gameState, counter, start);
    }

    @Override
    public ArcadeGame getGame() {
        return (ArcadeGame) super.getGame();
    }

    public abstract ArcadeCategory getArcadeCategory();
}
