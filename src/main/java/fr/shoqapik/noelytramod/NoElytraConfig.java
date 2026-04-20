package fr.shoqapik.noelytramod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NoElytraConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("noelytramod");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = FMLPaths.GAMEDIR.get()
            .resolve("config/noelytramod.json").toFile();

    // Defaults — same as the original mod
    public static String itemId   = "minecraft:book";
    public static String itemName = "I'm sorry but the Elytra is in another End City";

    public static void load() {
        if (!FILE.exists()) {
            save();
            LOGGER.info("[NoElytraMod] Created default config at {}", FILE.getAbsolutePath());
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            if (obj.has("item_id"))   itemId   = obj.get("item_id").getAsString();
            if (obj.has("item_name")) itemName = obj.get("item_name").getAsString();
            LOGGER.info("[NoElytraMod] Loaded config: item={}, name={}", itemId, itemName);
        } catch (IOException e) {
            LOGGER.error("[NoElytraMod] Failed to read config", e);
        }
    }

    private static void save() {
        try {
            FILE.getParentFile().mkdirs();
            JsonObject obj = new JsonObject();
            obj.addProperty("item_id",   itemId);
            obj.addProperty("item_name", itemName);
            try (FileWriter writer = new FileWriter(FILE)) {
                writer.write(GSON.toJson(obj));
            }
        } catch (IOException e) {
            LOGGER.error("[NoElytraMod] Failed to write config", e);
        }
    }
}
