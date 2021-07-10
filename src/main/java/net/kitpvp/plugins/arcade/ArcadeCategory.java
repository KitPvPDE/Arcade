package net.kitpvp.plugins.arcade;

import net.kitpvp.plugins.kitpvp.modules.listener.AbstractStageable;

public enum ArcadeCategory implements AbstractStageable {

    LOBBY, JNR, MUSHROOM, LABYRINTH
    ;

    @Override
    public int id() {
        return this.ordinal();
    }

    public static short shortOrdinal(ArcadeCategory category) {
        if(category == null) {
            return -1;
        } else {
            return (short) category.ordinal();
        }
    }
}
