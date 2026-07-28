/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package thegate.commands;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.ConfigManager;
import thegate.main.Perms;

public class RotateGateCommand {
    public static boolean RotateGate(Player p, String address, String angle) {
        GateObject gate;
        float a = 0.0f;
        try {
            a = Float.valueOf(angle).floatValue();
        }
        catch (NumberFormatException e) {
            if (p != null) {
                p.sendMessage(ChatColor.RED + "Casting error!");
            } else {
                Bukkit.getLogger().log(Level.WARNING, "Casting Error!");
            }
            return true;
        }
        GateObject gateObject = gate = address != null ? GateManager.getGateWithAddress(address) : GateManager.getClosestGateTo(p.getLocation());
        if (p != null && gate == null) {
            p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoGateWithAddress", "{ADDRESS}", address));
            return true;
        }
        if (!(p == null || p.hasPermission(Perms.thegate_owner_editgate.value()) && gate.hasOwningRelation(p) || p.hasPermission(Perms.thegate_admin_editgate.value()))) {
            p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            return true;
        }
        if (gate == null) {
            if (p != null) {
                p.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoGateWithAddress", "{ADDRESS}", address));
            } else {
                Bukkit.getLogger().log(Level.WARNING, "No gate found with address: " + address);
            }
            return true;
        }
        gate.setFacing(a / 360.0f * 4.0f % 4.0f);
        gate.setUpdated(true);
        gate.Repair();
        return true;
    }
}

