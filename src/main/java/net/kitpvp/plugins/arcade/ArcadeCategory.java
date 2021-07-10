package net.kitpvp.plugins.arcade;

public enum ArcadeCategory {

    LOBBY, JNR, MUSHROOM, LABYRINTH
    ;

    public static short shortOrdinal(ArcadeCategory category) {
        if(category == null) {
            return -1;
        } else {
            return (short) category.ordinal();
        }
    }
}
