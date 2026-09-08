package com.packageing.tools.packagetools.entitys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builds a PLAYER_HEAD ItemStack carrying a custom skin texture - used only for the Bedrock-only
 * skull-texture workaround in ArmorStand.pushEquipment() (see the BedrockHeadTextures section of
 * config.yml and the class-level comment in ArmorStand.java for why this exists at all).
 *
 * This used to be two separate per-platform implementations: Paper via the org.bukkit.profile.PlayerProfile
 * API, Spigot via the raw NBT-string escape hatch. The Paper one turned out to be fragile - some Paper API
 * builds only expose SkullMeta.setPlayerProfile(com.destroystokyo.paper.profile.PlayerProfile), the older
 * Paper-specific type, not the newer org.bukkit.profile.PlayerProfile, so which one compiles depends on
 * exactly which Paper build you're against. Bukkit.getUnsafe().modifyItemStack() doesn't have that problem
 * (Paper is a superset of the Spigot/Bukkit API, so this works unchanged on both), so this one file now
 * covers both platforms - it lives in Common instead of being duplicated per-variant.
 *
 * IMPORTANT: since Minecraft 1.20.5's data component overhaul, the argument format changed from the old
 * {SkullOwner:{...}} tag syntax (which no longer works) to the bracket/component syntax below - confirmed
 * against a real 1.20.5+ migration report, not guessed.
 */
public final class HeadTextureFactory {

    private HeadTextureFactory() {}

    public static ItemStack build(String base64Value) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (base64Value == null || base64Value.isBlank()) {
            return head; // no texture configured for this material - plain head, same as before
        }
        try {
            // modifyItemStack expects ONLY the bracketed component block, not the full item[component]
            // form used by /give - the ItemStack already carries the item type, so including
            // "minecraft:player_head" here breaks the parser entirely (confirmed against a real report of
            // this exact failure, not guessed: https://github.com/EssentialsX/Essentials/pull/5783).
            String args = "[minecraft:profile={properties:[{name:\"textures\",value:\"" + base64Value + "\"}]}]";
            head = Bukkit.getUnsafe().modifyItemStack(head, args);
        } catch (Exception e) {
            // Malformed/blank BedrockHeadTextures value - fall back to a plain head rather than
            // crashing gate creation over a config typo. Log it so it's not silently invisible in testing.
            Logger.getLogger("The_Gate").log(Level.WARNING, "[The Gate] Invalid BedrockHeadTextures value, falling back to a plain head: " + e.getMessage());
        }
        return head;
    }
}
