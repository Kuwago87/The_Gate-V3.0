/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 */
package thegate.commands;

import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.Globals;
import thegate.math.GateMath;

public class CreateGateCommand {
    private static char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static boolean create(Player p, String address, String world, int x, int y, int z, float rot) {
        if (p != null) {
            if (address.length() > 7 || address.length() < 7 || !CreateGateCommand.checkAddress(address) || GateManager.getGateWithAddress(address) != null) {
                p.sendMessage(ChatColor.RED + "Illegal Address: " + address);
                return true;
            }
            Location loc = p.getLocation().getBlock().getLocation().add(new Vector(0, -1, 0));
            GateObject go = new GateObject(loc, loc.getWorld().getName(), rot < 0.0f ? GateMath.getPlayerDirection(p) : rot / 360.0f * 4.0f, address, p.getUniqueId().toString(), p.getName());
            go.setUpdated(true);
            GateManager.addGate(go);
            p.sendMessage(ChatColor.GREEN + "Gate created!");
        } else {
            if (address.length() > 7 || address.length() < 7 || !CreateGateCommand.checkAddress(address) || GateManager.getGateWithAddress(address) != null) {
                Bukkit.getLogger().log(Level.WARNING, "Illegal Address: " + address);
                return true;
            }
            World w = Bukkit.getWorld((String)world);
            if (w == null) {
                Bukkit.getLogger().log(Level.WARNING, "Could not find world: " + world);
                return true;
            }
            float r = rot / 360.0f * 4.0f % 4.0f;
            Location loc = new Location(w, (double)x, (double)y, (double)z).getBlock().getLocation().add(new Vector(0, -1, 0));
            GateObject gate = new GateObject(loc, world, r, address, "null", Globals.dummyOwnerName);
            gate.setUpdated(true);
            GateManager.addGate(gate);
            Bukkit.getLogger().log(Level.INFO, "Gate: " + gate.getAddress() + " created.");
            Bukkit.getLogger().log(Level.INFO, gate.toString());
        }
        return true;
    }

    private static boolean checkAddress(String s) {
        String o = s;
        char[] cArray = chars;
        int n = chars.length;
        int n2 = 0;
        while (n2 < n) {
            char c2 = cArray[n2];
            o = o.replace(String.valueOf(c2), "");
            ++n2;
        }
        return o.length() < 1;
    }
}

