package net.kitpvp.plugins.arcade.test;

import net.kitpvp.network.test.AbstractTestingBase;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.factory.ArcadeCategoryFactory;
import net.kitpvp.plugins.arcade.listener.GameListener;
import net.kitpvp.plugins.kitpvp.modules.listener.factory.LambdaListenerFactory;
import net.kitpvp.plugins.kitpvp.modules.listener.register.GlobalEventRegister;
import net.kitpvp.plugins.kitpvpcore.modules.test.TestListenerFactoryRegister;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

public class TestListeners extends AbstractTestingBase {

    private static GlobalEventRegister<ArcadeCategory> eventRegister;
    private static ArcadePlugin plugin;

    @BeforeClass
    public static void setupPlugin() {
        plugin = new ArcadePlugin();
        eventRegister = new GlobalEventRegister<>(ArcadeCategory::shortOrdinal);
    }

    @Test
    public void testRegister() {
        TestListenerFactoryRegister factoryRegister = new TestListenerFactoryRegister();
        factoryRegister.registerFactory(LambdaListenerFactory.newListenerFactory(eventRegister,
                ArcadeCategoryFactory.INSTANCE));
        factoryRegister.stopAcceptingFactories();
        factoryRegister.registerListenersRecursive("net.kitpvp.plugins.arcade.listener", plugin);
    }

    @Test
    public void testUnusedEvents() {
        Assert.assertEquals("unusedEvents",
                Collections.emptySet(), eventRegister.getUnusedEvents(GameListener.class));
    }

    @Test
    public void testNonCalledEvents() {
        Assert.assertEquals("nonCalledEvents",
                Collections.emptySet(), eventRegister.getNonCalledEvents(GameListener.class));
    }
}
