package net.kitpvp.plugins.arcade.factory;

import lombok.RequiredArgsConstructor;
import net.kitpvp.plugins.kitpvp.modules.listener.AbstractStageableMode;

@RequiredArgsConstructor
public enum ArcadeCategoryMode implements AbstractStageableMode {

    EQUAL(AbstractStageableMode.EQUAL),
    ANY(AbstractStageableMode.ANY),
    NOT(AbstractStageableMode.NOT),
    ;

    private final AbstractStageableMode parent;

    @Override
    public boolean test(int i, int i1) {
        return this.parent.test(i, i1);
    }
}
