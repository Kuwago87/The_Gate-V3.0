package thegate.main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Checks https://github.com/Kuwago87/The_Gate-V3.0/releases for a version newer than the one currently
 * running. Two things are deliberately filtered out:
 *   - anything GitHub itself flags as "draft" or "prerelease" (the official API fields for this - NOT
 *     every "-beta"-tagged release on this repo is actually flagged prerelease; e.g. V3.0.6-Beta, the
 *     current latest at the time this was written, is a full release despite the name)
 *   - anything with "alpha" in its tag name, checked separately and explicitly as requested, since no
 *     current release uses that word but a future one might
 * Only ever reports UPWARD from the currently running version - an older or equal release (or a release
 * whose version string can't be parsed) is never reported. Runs once, a few seconds after startup, off the
 * main thread; a failed/unreachable check is silent and never affects plugin startup.
 */
public class GateUpdateChecker extends BukkitRunnable {

    private static final String API_URL = "https://api.github.com/repos/Kuwago87/The_Gate-V3.0/releases";

    @Override
    public void run() {
        Logger log = TheGateMain.theGateMain.getLogger();
        String runningVersion = TheGateMain.theGateMain.getDescription().getVersion();
        try {
            int[] currentVersion = parseVersion(runningVersion);
            if (currentVersion == null) {
                return; // shouldn't happen, but never crash a background task over an unparseable local version
            }

            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setRequestProperty("User-Agent", "The_Gate-UpdateChecker"); // GitHub's API rejects requests with no User-Agent at all
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            StringBuilder body = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }

            JsonElement parsed = JsonParser.parseString(body.toString());
            if (!parsed.isJsonArray()) {
                return;
            }

            String bestTag = null;
            int[] bestVersion = currentVersion;

            for (JsonElement el : (JsonArray) parsed.getAsJsonArray()) {
                if (!el.isJsonObject()) continue;
                JsonObject release = el.getAsJsonObject();

                boolean prerelease = release.has("prerelease") && release.get("prerelease").getAsBoolean();
                boolean draft = release.has("draft") && release.get("draft").getAsBoolean();
                if (prerelease || draft) {
                    continue;
                }

                String tag = release.has("tag_name") && !release.get("tag_name").isJsonNull() ? release.get("tag_name").getAsString() : null;
                if (tag == null || tag.toLowerCase().contains("alpha")) {
                    continue;
                }

                int[] releaseVersion = parseVersion(tag);
                if (releaseVersion != null && compareVersions(releaseVersion, bestVersion) > 0) {
                    bestVersion = releaseVersion;
                    bestTag = tag;
                }
            }

            if (bestTag != null) {
                log.log(Level.WARNING, "[The Gate] A newer version is available: " + bestTag + " (you're running " + runningVersion + ")");
                log.log(Level.WARNING, "[The Gate] https://github.com/Kuwago87/The_Gate-V3.0/releases/latest");
            }
        } catch (Exception e) {
            log.log(Level.FINE, "[The Gate] Could not check for updates: " + e.getMessage());
        }
    }

    /**
     * Extracts the leading dotted-numeric version from a string as an int array - e.g. "3.0.10.13" out of
     * either "3.0.10.13mc26.2-ppr" (this plugin's own version string) or "V3.0.6-Beta" (a GitHub tag).
     * Returns null if no leading numeric run is found at all.
     */
    private static int[] parseVersion(String raw) {
        if (raw == null) return null;
        String s = (raw.startsWith("v") || raw.startsWith("V")) ? raw.substring(1) : raw;
        StringBuilder numeric = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                numeric.append(c);
            } else {
                break;
            }
        }
        if (numeric.length() == 0) return null;
        String[] parts = numeric.toString().split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                result[i] = 0;
            }
        }
        return result;
    }

    /** Segment-by-segment numeric comparison (missing trailing segments treated as 0) - never plain string comparison. */
    private static int compareVersions(int[] a, int[] b) {
        int len = Math.max(a.length, b.length);
        for (int i = 0; i < len; i++) {
            int av = i < a.length ? a[i] : 0;
            int bv = i < b.length ? b[i] : 0;
            if (av != bv) {
                return Integer.compare(av, bv);
            }
        }
        return 0;
    }
}
