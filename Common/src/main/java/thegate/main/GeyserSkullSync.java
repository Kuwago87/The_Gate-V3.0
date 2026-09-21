package thegate.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Writes the base64 values already sitting in Globals.BedrockHeadTextures into Geyser's own
 * custom_skulls.json mapping file, so an admin only has to maintain one list (The_Gate's config.yml)
 * instead of keeping two files in sync by hand. See geysermc.org/wiki/geyser/custom-skulls/ for the format.
 *
 * This does NOT require Geyser to be installed - if it isn't found, this silently does nothing, the same
 * way BedrockCompat.java degrades gracefully. Must run BEFORE Geyser's own onEnable() reads that file,
 * which is why plugin.yml declares `loadbefore: [Geyser-Spigot]` instead of the softdepend it used to be.
 *
 * Merge behavior is intentionally ADD-ONLY: if the file already exists (hand-edited, or from a previous
 * run of this same sync), any entries already there that we don't recognize are left completely alone -
 * we only ever add our own values if they're missing, never remove anything. The tradeoff: if you remove a
 * material from BedrockHeadTextures, its value stays orphaned in Geyser's file rather than being cleaned
 * up automatically. That's a deliberate, safer default - an add-only sync can never destroy something an
 * admin (or another tool) put there for an unrelated reason; a "make this file exactly match ours" sync could.
 *
 * Runs even with zero configured textures (creates/verifies the file structure regardless) - see the
 * "always sync" note in sync() below.
 */
public class GeyserSkullSync {

    public static void sync() {
        Plugin geyser = Bukkit.getPluginManager().getPlugin("Geyser-Spigot");
        if (geyser == null) {
            return; // Geyser isn't installed - nothing to do, same as BedrockCompat's guard
        }

        Logger log = TheGateMain.theGateMain.getLogger();
        try {
            File mappingsDir = new File(geyser.getDataFolder(), "custom_mappings");
            if (!mappingsDir.exists() && !mappingsDir.mkdirs()) {
                log.log(Level.WARNING, "[The Gate] Could not create Geyser's custom_mappings folder - add BedrockHeadTextures values to Geyser's custom_skulls.json manually instead.");
                return;
            }
            File skullsFile = new File(mappingsDir, "custom_skulls.json");

            JsonObject root;
            if (skullsFile.exists()) {
                try (FileReader reader = new FileReader(skullsFile)) {
                    JsonElement parsed = JsonParser.parseReader(reader);
                    root = parsed != null && parsed.isJsonObject() ? parsed.getAsJsonObject() : new JsonObject();
                } catch (Exception e) {
                    log.log(Level.WARNING, "[The Gate] custom_skulls.json exists but couldn't be parsed - leaving it untouched rather than risk overwriting it. (" + e.getMessage() + ")");
                    return;
                }
            } else {
                root = new JsonObject();
            }

            if (!root.has("format_version")) {
                root.addProperty("format_version", 1);
            }
            JsonObject skulls = root.has("skulls") && root.get("skulls").isJsonObject() ? root.getAsJsonObject("skulls") : new JsonObject();
            JsonArray profiles = skulls.has("profile") && skulls.get("profile").isJsonArray() ? skulls.getAsJsonArray("profile") : new JsonArray();

            Set<String> existing = new HashSet<String>();
            for (JsonElement el : profiles) {
                if (el.isJsonPrimitive()) {
                    existing.add(el.getAsString());
                }
            }

            int added = 0;
            for (String value : Globals.BedrockHeadTextures.values()) {
                if (!existing.contains(value)) {
                    profiles.add(value);
                    existing.add(value);
                    added++;
                }
            }

            skulls.add("profile", profiles);
            root.add("skulls", skulls);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(skullsFile)) {
                gson.toJson(root, writer);
            }

            if (added > 0) {
                log.log(Level.INFO, "[The Gate] Added " + added + " texture value(s) to Geyser's custom_skulls.json - restart the server for Geyser to pick them up.");
            } else {
                log.log(Level.INFO, "[The Gate] Geyser's custom_skulls.json is up to date (" + Globals.BedrockHeadTextures.size() + " texture(s) configured).");
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "[The Gate] Failed to sync BedrockHeadTextures into Geyser's custom_skulls.json - add them manually instead. (" + e + ")");
        }
    }
}
