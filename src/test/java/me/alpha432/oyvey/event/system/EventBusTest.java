package me.alpha432.oyvey.event.system;

import me.alpha432.oyvey.event.Event;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    static class TestEvent extends Event {}

    static class TestListener {
        boolean called = false;

        @Subscribe
        public void onTest(TestEvent event) {
            called = true;
        }
    }

    static class ThrowingListener {
        @Subscribe
        public void onTest(TestEvent event) {
            throw new RuntimeException("Intentional failure");
        }
    }

    @Test
    void testRegisterAndPost() {
        EventBus bus = new EventBus();
        TestListener listener = new TestListener();
        bus.register(listener);
        bus.post(new TestEvent());
        assertTrue(listener.called);
    }

    @Test
    void testUnregister() {
        EventBus bus = new EventBus();
        TestListener listener = new TestListener();
        bus.register(listener);
        bus.unregister(listener);
        bus.post(new TestEvent());
        assertFalse(listener.called);
    }

    @Test
    void testThrowingListenerDoesNotBreakOthers() {
        EventBus bus = new EventBus();
        TestListener good = new TestListener();
        ThrowingListener bad = new ThrowingListener();
        bus.register(good);
        bus.register(bad);
        assertDoesNotThrow(() -> bus.post(new TestEvent()));
        assertTrue(good.called);
    }
}
