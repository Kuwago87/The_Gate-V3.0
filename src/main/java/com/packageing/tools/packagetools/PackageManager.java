package com.packageing.tools.packagetools;

/*
 * REWRITTEN for the 1.21.1 -> 26.2 update.
 *
 * The original PackageManager sent raw NMS packets directly: PacketPlayOutSpawnEntity,
 * PacketPlayOutEntityEquipment, PacketPlayOutEntityMetadata, PacketPlayOutEntityDestroy,
 * PacketPlayOutEntityTeleport, all hardcoded to 1.21.1's obfuscated mappings.
 *
 * This version is a thin facade over the new ArmorStand class (which now wraps an EntityLib
 * WrapperEntity instead of raw NMS). The 5 static method signatures below are IDENTICAL to the
 * original class so that every existing call site across GatePackages, GateChevron, IrisPart,
 * GateObject, TheGateMain, and the animation classes compiles without modification.
 */

import com.packageing.tools.packagetools.entitys.ArmorStand;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.entity.Player;

public class PackageManager {

    public static void SendSpawnPackage(ArmorStand stand, Player p) {
        stand.showTo(p);
    }

    public static void SendDespawnPackage(ArmorStand stand, Player p) {
        stand.hideFrom(p);
    }

    /**
     * Kept for GateObject.Vanish(), which only has the raw entity ID around (not the ArmorStand
     * instance) by the time it wants to despawn. We look the live WrapperEntity up by ID via EntityLib.
     * If the entity has already been removed server-side (e.g. Vanish() called twice), this is a no-op.
     */
    public static void SendDespawnPackage(int standID, Player p) {
        WrapperEntity entity = EntityLib.getApi().getEntity(standID);
        if (entity != null) {
            entity.removeViewer(p.getUniqueId());
        }
    }

    public static void SendUpdate(ArmorStand stand, Player p) {
        stand.updateFor(p);
    }

    public static void SendTeleport(ArmorStand stand, Player p) {
        stand.teleportFor(p);
    }
}
