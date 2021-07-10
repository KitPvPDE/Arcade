package net.kitpvp.plugins.arcade.factory;

import net.kitpvp.plugins.kitpvp.util.function.UnaryShortPredicate;

public enum ArcadeCategoryMode implements UnaryShortPredicate {

    EQUAL() {
        @Override
        public boolean test(short l, short r) {
            return l == r;
        }
    },
    ANY() {
        @Override
        public boolean test(short l, short r) {
            return true;
        }
    };

    public abstract boolean test(short l, short r);
}
