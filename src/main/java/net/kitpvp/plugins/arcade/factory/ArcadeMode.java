package net.kitpvp.plugins.arcade.factory;

import net.kitpvp.plugins.arcade.ArcadeCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArcadeMode {

    ArcadeCategory value();
}
