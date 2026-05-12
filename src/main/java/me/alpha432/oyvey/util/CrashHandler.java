package me.alpha432.oyvey.util;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CrashHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger("QuantumMC+ CrashHandler");
    private static boolean installed = false;

    public static void install() {
        if (installed) return;
        installed = true;

        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logCrashContext(throwable);
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        });
        LOGGER.info("Crash handler installed");
    }

    private static void logCrashContext(Throwable throwable) {
        try {
            LOGGER.error("=== QuantumMC+ Crash Context ===");
            LOGGER.error("Active Modules: {}", getActiveModules());
            LOGGER.error("Module Count: {}", getModuleCount());
            LOGGER.error("================================");
        } catch (Throwable ignored) {
        }
    }

    private static String getActiveModules() {
        if (OyVey.moduleManager == null) return "N/A (not initialized)";
        List<Module> active = OyVey.moduleManager.stream()
                .filter(Module::isEnabled)
                .toList();
        if (active.isEmpty()) return "none";
        return active.stream()
                .map(Module::getDisplayName)
                .collect(Collectors.joining(", "));
    }

    private static String getModuleCount() {
        if (OyVey.moduleManager == null) return "N/A";
        long total = OyVey.moduleManager.stream().count();
        long active = OyVey.moduleManager.stream().filter(Module::isEnabled).count();
        return active + "/" + total;
    }
}
