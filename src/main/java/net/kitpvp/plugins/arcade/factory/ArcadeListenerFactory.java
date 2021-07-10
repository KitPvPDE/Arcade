package net.kitpvp.plugins.arcade.factory;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.kitpvp.modules.listener.GlobalEvent;
import net.kitpvp.plugins.kitpvp.modules.listener.factory.LambdaListenerFactory;
import net.kitpvp.plugins.kitpvp.modules.listener.listeners.Listener;
import net.kitpvp.plugins.kitpvp.modules.listener.register.AbstractEventListener;
import net.kitpvp.plugins.kitpvp.modules.listener.register.EventRegister;
import net.kitpvp.plugins.kitpvp.util.function.UnaryShortPredicate;

import java.lang.invoke.LambdaConversionException;
import java.lang.reflect.Method;

public class ArcadeListenerFactory extends LambdaListenerFactory<ArcadeCategory> {

    public ArcadeListenerFactory(EventRegister<ArcadeCategory> register) {
        super(register, ArcadeCategory.class);
    }

    @Override
    public AbstractEventListener getEventListener(Listener listener, Method method) throws IllegalAccessException, LambdaConversionException {
        if (method.isAnnotationPresent(ArcadeEvent.class)) {
            ArcadeEvent annotation = method.getDeclaredAnnotation(ArcadeEvent.class);
            return this.createListener(listener, method, annotation.priority(),
                    null, annotation.category(), annotation.categoryMode(), null, null, null, null);
        } else if(method.isAnnotationPresent(GlobalEvent.class)) {
            GlobalEvent annotation = method.getDeclaredAnnotation(GlobalEvent.class);
            return this.createListener(listener, method, annotation.priority(),
                    null, null, ArcadeCategoryMode.ANY, null, null, null, null);
        }
        return null;
    }
}
