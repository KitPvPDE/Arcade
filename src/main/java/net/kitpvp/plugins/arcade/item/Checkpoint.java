package net.kitpvp.plugins.arcade.item;

import lombok.Getter;
import net.kitpvp.plugins.arcade.item.Items.JNR;
import net.kitpvp.plugins.kitpvpcore.lavendle.LavendleItem;

public enum Checkpoint {

    VERY_EASY(JNR.CHECKPOINT_1), EASY(JNR.CHECKPOINT_2), MEDIUM(JNR.CHECKPOINT_3), HARD(JNR.CHECKPOINT_4);

    public static final Checkpoint[] CHECKPOINTS = Checkpoint.values();

    @Getter
    private final LavendleItem[] items;

    Checkpoint(LavendleItem[] items) {
        this.items = items;
    }

    public static Checkpoint byOrdinal(int ordinal) {
        for (Checkpoint checkpoint : CHECKPOINTS) {
            if(ordinal == checkpoint.ordinal()) {
                return checkpoint;
            }
        }
        return null;
    }
}
