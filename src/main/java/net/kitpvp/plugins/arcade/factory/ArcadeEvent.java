package net.kitpvp.plugins.arcade.factory;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArcadeEvent {

    ArcadeCategory category();

    ArcadeCategoryMode categoryMode() default ArcadeCategoryMode.EQUAL;

    EventPriority priority() default EventPriority.NORMAL;
}
