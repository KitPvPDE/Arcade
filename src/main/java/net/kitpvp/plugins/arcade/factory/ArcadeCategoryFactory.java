package net.kitpvp.plugins.arcade.factory;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.kitpvp.modules.listener.factory.LambdaStageParameterFactory;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvp.util.function.UnaryShortPredicate;

import java.lang.reflect.Method;

public class ArcadeCategoryFactory implements LambdaStageParameterFactory<ArcadeCategory> {

    public static final ArcadeCategoryFactory INSTANCE = new ArcadeCategoryFactory();

    @Override
    public ArcadeCategory getParameter(Listener listener, Method method) {
        if(method.isAnnotationPresent(ArcadeMode.class)) {
            return method.getDeclaredAnnotation(ArcadeMode.class).value();
        }
        return null;
    }

    @Override
    public UnaryShortPredicate getParameterMode(Listener listener, Method method) {
        if(method.isAnnotationPresent(ArcadeMode.class)) {
            return ArcadeCategoryMode.EQUAL;
        } else {
            return ArcadeCategoryMode.ANY;
        }
    }
}
