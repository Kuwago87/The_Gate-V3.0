package com.packageing.tools.packagetools.entitys;

import org.bukkit.entity.Player;

/**
 * Optional bridge to Geyser's API. The_Gate does NOT require Geyser - servers that don't run it
 * must keep working exactly as before. Every reference to org.geysermc.* is isolated to this one
 * class and guarded, so a missing (or older) Geyser install can never break gate creation for
 * everyone else, it just makes isBedrock() always return false.
 */
final class BedrockCompat {

    private static final boolean GEYSER_PRESENT;

    static {
        boolean present;
        try {
            Class.forName("org.geysermc.geyser.api.GeyserApi");
            present = true;
        } catch (Throwable t) {
            present = false;
        }
        GEYSER_PRESENT = present;
    }

    private BedrockCompat() {}

    /** True if this player is connected through Geyser (a Bedrock client). Always false if Geyser isn't installed. */
    static boolean isBedrock(Player p) {
        if (!GEYSER_PRESENT) return false;
        try {
            return org.geysermc.geyser.api.GeyserApi.api().isBedrockPlayer(p.getUniqueId());
        } catch (Throwable t) {
            // GeyserApi.api() can be null very early (before Geyser finishes enabling) - treat that as "not bedrock"
            // rather than letting a NPE bubble up into gate creation.
            return false;
        }
    }
}
