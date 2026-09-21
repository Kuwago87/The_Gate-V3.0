package com.packageing.tools.packagetools.entitys;

import org.bukkit.entity.Player;

/**
 * Optional bridge to Geyser's API. The_Gate does NOT require Geyser - servers that don't run it
 * must keep working exactly as before. Every reference to org.geysermc.* is isolated to this one
 * class and guarded, so a missing (or older) Geyser install can never break gate creation for
 * everyone else, it just makes isBedrock() always return false.
 */
final class BedrockCompat {

    // Only ever latches to TRUE once confirmed - Geyser doesn't unload itself mid-session, so that's safe
    // to cache permanently. A FALSE result is never cached: The_Gate now loads BEFORE Geyser-Spigot (see
    // plugin.yml - needed for GeyserSkullSync.java to write its mappings file in time), so the very first
    // check can genuinely run before Geyser's classes exist on the classpath yet. Locking that in
    // permanently (the previous version used `static final`, computed once) would make isBedrock() return
    // false for the rest of the server's uptime even once Geyser finishes loading moments later - exactly
    // the bug that caused the whole gate to stop appearing on Bedrock regardless of restart or reload.
    private static volatile boolean geyserPresent = false;

    private BedrockCompat() {}

    private static boolean isGeyserPresent() {
        if (geyserPresent) {
            return true;
        }
        try {
            Class.forName("org.geysermc.geyser.api.GeyserApi");
            geyserPresent = true;
        } catch (Throwable t) {
            // Not present YET - could just mean Geyser hasn't finished loading. Deliberately not cached,
            // so the next call (e.g. once a real player actually joins, well after all plugins are enabled)
            // gets a fresh, accurate answer instead of an early false negative baked in forever.
        }
        return geyserPresent;
    }

    /** True if this player is connected through Geyser (a Bedrock client). Always false if Geyser isn't installed. */
    static boolean isBedrock(Player p) {
        if (!isGeyserPresent()) return false;
        try {
            return org.geysermc.geyser.api.GeyserApi.api().isBedrockPlayer(p.getUniqueId());
        } catch (Throwable t) {
            // GeyserApi.api() can be null very early (before Geyser finishes enabling) - treat that as "not bedrock"
            // rather than letting a NPE bubble up into gate creation.
            return false;
        }
    }
}
