package me.alpha432.oyvey.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.settings.Bind;
import me.alpha432.oyvey.features.settings.EnumConverter;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.traits.Jsonable;
import net.fabricmc.loader.api.FabricLoader;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ConfigManager");
    private static final Path BASE_PATH = FabricLoader.getInstance().getGameDir().resolve("quantummcplus");
    private static final String DEFAULT_PROFILE = "main";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final List<Jsonable> jsonables = new LinkedList<>();
    private String currentProfile = DEFAULT_PROFILE;

    public String getCurrentProfile() {
        return currentProfile;
    }

    public boolean setProfile(String name) {
        if (name == null || name.isBlank() || name.contains("..") || name.contains("/") || name.contains("\\")) {
            LOGGER.warn("Invalid profile name: {}", name);
            return false;
        }
        save();
        currentProfile = name;
        load();
        LOGGER.info("Switched to config profile: {}", name);
        return true;
    }

    public List<String> listProfiles() {
        List<String> profiles = new LinkedList<>();
        Path base = getProfilePath("");
        if (Files.isDirectory(base)) {
            try (Stream<Path> dirs = Files.list(base)) {
                dirs.filter(Files::isDirectory)
                        .map(p -> p.getFileName().toString())
                        .forEach(profiles::add);
            } catch (IOException e) {
                LOGGER.error("Failed to list profiles", e);
            }
        }
        if (profiles.isEmpty()) profiles.add(DEFAULT_PROFILE);
        return profiles;
    }

    public boolean deleteProfile(String name) {
        if (name.equals(DEFAULT_PROFILE) || name.equals(currentProfile)) {
            LOGGER.warn("Cannot delete current or default profile: {}", name);
            return false;
        }
        Path dir = getProfilePath(name);
        if (Files.isDirectory(dir)) {
            try (Stream<Path> files = Files.list(dir)) {
                files.forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                });
                Files.deleteIfExists(dir);
            } catch (IOException e) {
                LOGGER.error("Failed to delete profile: {}", name, e);
                return false;
            }
        }
        return true;
    }

    private Path getProfilePath(String profile) {
        return BASE_PATH.resolve(profile == null || profile.isBlank() ? DEFAULT_PROFILE : profile);
    }

    private Path getConfigPath(String fileName) {
        return getProfilePath(currentProfile).resolve(fileName);
    }

    public void addConfig(Jsonable jsonable) {
        jsonables.add(jsonable);
    }

    public void load() {
        mkdirs();
        for (Jsonable jsonable : jsonables) {
            String fileName = jsonable.getFileName();
            Path filePath = getConfigPath(fileName);
            try {
                if (!Files.exists(filePath)) continue;
                String read = Files.readString(filePath);
                JsonElement parsed = JsonParser.parseString(read);
                if (parsed == null || parsed.isJsonNull()) {
                    LOGGER.warn("Config file {} is empty or null, using defaults", fileName);
                    continue;
                }
                jsonable.fromJson(parsed);
                LOGGER.debug("Loaded config [{}]: {}", currentProfile, fileName);
            } catch (Throwable e) {
                LOGGER.error("Failed to load config '{}' in profile '{}', using defaults. Reason: {}",
                        fileName, currentProfile, e.getMessage());
                backupCorrupted(filePath, fileName);
            }
        }
    }

    public void save() {
        mkdirs();
        for (Jsonable jsonable : jsonables) {
            String fileName = jsonable.getFileName();
            Path filePath = getConfigPath(fileName);
            try {
                if (Files.exists(filePath)) {
                    Path backupPath = getConfigPath(fileName + ".bak");
                    Files.copy(filePath, backupPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
                JsonElement json = jsonable.toJson();
                Files.writeString(filePath, GSON.toJson(json));
                LOGGER.debug("Saved config [{}]: {}", currentProfile, fileName);
            } catch (Throwable e) {
                LOGGER.error("Failed to write config '{}' in profile '{}'", fileName, currentProfile, e);
            }
        }
    }

    private void backupCorrupted(Path filePath, String fileName) {
        try {
            Path corruptedPath = getConfigPath(fileName + ".corrupted");
            Files.move(filePath, corruptedPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            LOGGER.warn("Backed up corrupted config to: {}", corruptedPath.getFileName());
        } catch (Throwable e) {
            LOGGER.warn("Could not back up corrupted config '{}': {}", fileName, e.getMessage());
        }
    }

    private void mkdirs() {
        Path dir = getProfilePath(currentProfile);
        if (!dir.toFile().exists()) {
            boolean success = dir.toFile().mkdirs();
            if (!success) {
                throw new RuntimeException("Failed to create config directory: " + dir);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
        if (element == null || element.isJsonNull()) return;
        switch (setting.getType()) {
            case "Boolean" -> setting.setValue(element.getAsBoolean());
            case "Double" -> setting.setValue(element.getAsDouble());
            case "Float" -> setting.setValue(element.getAsFloat());
            case "Integer" -> setting.setValue(element.getAsInt());
            case "String" -> setting.setValue(element.getAsString().replace("_", " "));
            case "Bind" -> setting.setValue(new Bind(element.getAsInt()));
            case "Color" -> {
                try {
                    String colorStr = element.getAsString();
                    String[] parts = colorStr.split(",");
                    if (parts.length == 4) {
                        int r = Integer.parseInt(parts[0]);
                        int g = Integer.parseInt(parts[1]);
                        int b = Integer.parseInt(parts[2]);
                        int a = Integer.parseInt(parts[3]);
                        setting.setValue(new Color(r, g, b, a));
                    }
                } catch (Exception exception) {
                    LOGGER.error("Error parsing color for: {} : {}", feature.getName(), setting.getName());
                }
            }
            case "Pos" -> {
                try {
                    String posStr = element.getAsString();
                    String[] parts = posStr.split(",");
                    if (parts.length == 2) {
                        float x = Float.parseFloat(parts[0]);
                        float y = Float.parseFloat(parts[1]);
                        setting.setValue(new Vector2f(x, y));
                    }
                } catch (Exception exception) {
                    LOGGER.error("Error parsing position for: {} : {}", feature.getName(), setting.getName());
                }
            }
            case "Enum" -> {
                try {
                    EnumConverter converter = new EnumConverter(setting.getValue().getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue(value);
                } catch (Exception exception) {
                    LOGGER.error("Error parsing enum for {}.{}: {}", feature.getName(), setting.getName(), exception);
                }
            }
            default -> LOGGER.error("Unknown Setting type for: {} : {}", feature.getName(), setting.getName());
        }
    }
}
