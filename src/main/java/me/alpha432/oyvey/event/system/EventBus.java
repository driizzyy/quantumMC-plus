package me.alpha432.oyvey.event.system;

import me.alpha432.oyvey.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger("QuantumMC+ EventBus");
    private final Map<Class<?>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();

    public void register(Object host) {
        register(host, host.getClass());
    }

    public void unregister(Object host) {
        for (CopyOnWriteArrayList<Listener> list : listeners.values()) {
            list.removeIf(listener -> listener.getHost().equals(host));
        }
    }

    public boolean post(Event event) {
        List<Listener> list = listeners.get(event.getClass());
        if (list == null) return false;

        for (Listener listener : list) {
            try {
                listener.invoke(event);
            } catch (Throwable t) {
                LOGGER.error("Exception in listener {}#{} for event {}",
                        listener.getHost().getClass().getSimpleName(),
                        listener.consumer().toString(),
                        event.getClass().getSimpleName(), t);
            }

            if (event.isCancelled())
                return true;
        }

        return false;
    }

    private void register(Object host, Class<?> klass) {
        for (Method method : klass.getDeclaredMethods()) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe == null) continue;

            Class<?>[] params = method.getParameterTypes();
            if (params.length == 1) {
                Class<?> eventType = params[0];

                Listener listener = Listener.of(host, subscribe.priority(), method);
                List<Listener> registry = listeners.computeIfAbsent(eventType,
                        k -> new CopyOnWriteArrayList<>());

                register(registry, listener);
            }
        }

        if (klass.getSuperclass() != null)
            register(host, klass.getSuperclass());
    }

    private void register(List<Listener> registry, Listener target) {
        int i = 0;
        for (Listener listener : registry) {
            i++;
            if (target.priority() > listener.priority()) break;
        }
        registry.add(i, target);
    }
}