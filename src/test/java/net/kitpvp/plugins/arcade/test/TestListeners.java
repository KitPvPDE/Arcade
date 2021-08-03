package net.kitpvp.plugins.arcade.test;

import net.kitpvp.gameapi.event.GameStatusChangeEvent;
import net.kitpvp.lavendle.LavendlePlugin;
import net.kitpvp.network.test.AbstractTestingBase;
import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadePlugin;
import net.kitpvp.plugins.arcade.factory.ArcadeListenerFactory;
import net.kitpvp.plugins.arcade.item.Items;
import net.kitpvp.plugins.arcade.listener.GameListener;
import net.kitpvp.plugins.kitpvp.events.movement.PlayerMoveEvent;
import net.kitpvp.plugins.kitpvp.modules.listener.register.GlobalEventRegister;
import net.kitpvp.plugins.kitpvpcore.KitPvPCore;
import net.kitpvp.plugins.kitpvpcore.modules.listener.ListenerFactoryRegister;
import net.kitpvp.plugins.kitpvpcore.modules.test.TestListenerFactoryRegister;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;

public class TestListeners extends AbstractTestingBase {

    private static TestListenerFactoryRegister factoryRegister;
    private static GlobalEventRegister<ArcadeCategory> eventRegister;
    private static ArcadePlugin plugin;
    private static KitPvPCore core;

    @BeforeClass
    public static void setupPlugin() {
        factoryRegister = new TestListenerFactoryRegister();
        eventRegister = GlobalEventRegister.newEventRegister(null);
        plugin = new TestArcadePlugin();
        core = new TestCorePlugin();
        factoryRegister.registerFactory(new ArcadeListenerFactory(eventRegister));
        factoryRegister.stopAcceptingFactories();
    }

    @Test
    public void testRegister() {
        factoryRegister.registerListenersRecursive("net.kitpvp.plugins.arcade.listener", plugin);
        Items.JNR.CHECKPOINT.getCallback();
    }

    @Test
    public void testUnusedEvents() {
        Assert.assertEquals("unusedEvents",
                Collections.emptySet(), eventRegister.getUnusedEvents(GameListener.class));
    }

    @Test
    public void testNonCalledEvents() {
        Assert.assertEquals("nonCalledEventsStrict",
                Collections.emptySet(), eventRegister.getNonCalledEvents(GameListener.class, true).stream().filter(eventClass -> {
                    if (eventClass.equals(GameStatusChangeEvent.class) ||
                            eventClass.equals(PlayerMoveEvent.class)) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toSet()));

        Assert.assertEquals("nonCalledEvents",
                Collections.emptySet(), eventRegister.getNonCalledEvents(GameListener.class, false));
    }

    @LavendlePlugin(name = "arcade")
    public static class TestArcadePlugin extends ArcadePlugin {

        public TestArcadePlugin() {
            plugin = this;
        }

        @Override
        public GlobalEventRegister<ArcadeCategory> getEventRegister() {
            return eventRegister;
        }
    }

    @LavendlePlugin(name = "core")
    public static class TestCorePlugin extends KitPvPCore {

        public TestCorePlugin() {
            setInstance(this);
        }

        @Override
        public ListenerFactoryRegister getListenerRegister() {
            return factoryRegister;
        }
    }
}
