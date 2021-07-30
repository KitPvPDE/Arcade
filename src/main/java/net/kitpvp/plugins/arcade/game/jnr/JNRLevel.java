package net.kitpvp.plugins.arcade.game.jnr;

import lombok.Getter;

public enum JNRLevel {

    VERY_EASY(2.3, 0, 2.4), EASY(2.5, 7, 2.8),
    MEDIUM(2.8, 16, 3.3), HARD(4.2, 24, 4.4);

    private static final JNRLevel[] VALUES = JNRLevel.values();

    @Getter
    private final double blockRange;
    @Getter
    private final int neededJumps;
    @Getter
    private final double minBlockRange;

    JNRLevel(double blockRange, int neededJumps, double minBlockRange) {
        this.blockRange = blockRange;
        this.neededJumps = neededJumps;
        this.minBlockRange = minBlockRange;
    }

    public static JNRLevel levelByJumps(int jumps) {
        JNRLevel current = JNRLevel.VERY_EASY;
        for (JNRLevel jnrLevel : VALUES) {
            if (jnrLevel.neededJumps <= jumps && jnrLevel.neededJumps >= current.neededJumps) {
                current = jnrLevel;
            }
        }
        return current;
    }
}
