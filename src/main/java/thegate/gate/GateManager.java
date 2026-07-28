/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package thegate.gate;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import thegate.gate.GateObject;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;
import thegate.math.GateMath;

public class GateManager {
    private static HashMap<String, GateObject> Gates = new HashMap();
    private static HashMap<String, Location> GatesLocation = new HashMap();
    private static HashMap<String, Set<GateObject>> GatesByWorldName = new HashMap();
    private static Set<GateObject> GatesOnOtherServer = new HashSet<GateObject>();

    public static void addGate(GateObject go) {
        if (go.getServer().equals(Globals.ServerName)) {
            Gates.put(go.getAddress(), go);
            GatesLocation.put(go.getAddress(), go.getGate());
            Set<GateObject> set = GatesByWorldName.get(go.getWorldName());
            if (GatesByWorldName.get(go.getWorldName()) != null) {
                set.add(go);
            } else {
                set = new HashSet<GateObject>();
                GatesByWorldName.put(go.getWorldName(), set);
                set.add(go);
            }
        } else {
            GatesOnOtherServer.add(go);
        }
    }

    public static void addGate(List<GateObject> gates) {
        gates.forEach(x -> GateManager.addGate(x));
    }

    public static void RemoveGate(GateObject go) {
        Gates.remove(go.getAddress());
        GatesLocation.remove(go.getAddress());
        if (GatesByWorldName.containsKey(go.getWorldName())) {
            Set<GateObject> gates = GatesByWorldName.get(go.getWorldName());
            gates.remove(go);
            GatesByWorldName.put(go.getWorldName(), gates);
        }
        for (String p : go.getCoOwner().keySet()) {
            TheGateMain.SaveLoadInterface.DeleatePlayerFromCoowner(p, go.getAddress());
        }
    }

    public static void ClearLists() {
        Gates.clear();
        GatesLocation.clear();
        GatesByWorldName.clear();
    }

    public static boolean GateInRadius(Location l) {
        Set<GateObject> gates = GatesByWorldName.get(l.getWorld().getName());
        if (gates == null) {
            return false;
        }
        for (GateObject obj : gates) {
            double z;
            double y;
            double x = obj.getGate().getX() - l.getX();
            if (!(Math.sqrt(x * x + (y = obj.getGate().getY() - l.getY()) * y + (z = obj.getGate().getZ() - l.getZ()) * z) < (double)Globals.GateExclusionRadius)) continue;
            return true;
        }
        return false;
    }

    public static void GateInRadius(Player p, float r) {
        Location l = p.getLocation();
        Set<GateObject> gates = GatesByWorldName.get(l.getWorld().getName());
        if (gates == null) {
            return;
        }
        for (GateObject g : gates) {
            double z;
            double y;
            Location loc = g.getGate();
            if (!l.getWorld().equals(loc.getWorld())) continue;
            double x = loc.getX() - l.getX();
            if (Math.sqrt(x * x + (y = loc.getY() - l.getY()) * y + (z = loc.getZ() - l.getZ()) * z) < (double)r) {
                g.addPlayerInRange(p);
                continue;
            }
            g.removePlayerInRange(p);
        }
    }

    public static Set<GateObject> getGatesInWorld(String world) {
        return GatesByWorldName.get(world);
    }

    public static int getPlayerGateAmmount(Player p) {
        int ammount = 0;
        for (GateObject gate : Gates.values()) {
            if (!gate.getOwnerUUID().equals(p.getUniqueId().toString())) continue;
            ++ammount;
        }
        return ammount;
    }

    public static int getPlayerGateAmmount(String uuid) {
        int ammount = 0;
        for (GateObject gate : Gates.values()) {
            if (!gate.getOwnerUUID().equals(uuid)) continue;
            ++ammount;
        }
        return ammount;
    }

    public static GateObject getGateWithAddress(String Address) {
        return Gates.get(Address);
    }

    public static GateObject getGateOnOtherServerWithAddress(String Address) {
        for (GateObject gate : GatesOnOtherServer) {
            if (!gate.getAddress().equals(Address)) continue;
            return gate;
        }
        return null;
    }

    public static boolean hasGateWithAddress(String Address) {
        return Gates.get(Address) != null;
    }

    public static boolean hasGateOnOtherServerWithAddress(String Address) {
        for (GateObject gate : GatesOnOtherServer) {
            if (!gate.getAddress().equals(Address)) continue;
            return true;
        }
        return false;
    }

    public static GateObject getGateByDHD(Location DHDLoc) {
        Location l = DHDLoc;
        Set<GateObject> gates = GatesByWorldName.get(l.getWorld().getName());
        if (gates == null) {
            return null;
        }
        for (GateObject gate : gates) {
            if (!gate.hasDHD() || !gate.getDHD().equals((Object)DHDLoc)) continue;
            return gate;
        }
        return null;
    }

    public static boolean hasDHDatLocation(Location DHDLocation) {
        Location l = DHDLocation;
        Set<GateObject> gates = GatesByWorldName.get(l.getWorld().getName());
        if (gates == null) {
            return false;
        }
        for (GateObject gate : gates) {
            if (!gate.hasDHD() || !gate.getDHD().equals((Object)DHDLocation)) continue;
            return true;
        }
        return false;
    }

    public static GateObject getClosestGateTo(Location loc) {
        GateObject closest = null;
        double last_d = 2.147483647E9;
        Set<GateObject> set = GatesByWorldName.get(loc.getWorld().getName());
        if (set == null) {
            return null;
        }
        for (GateObject obj : set) {
            double d = GateMath.getDistance(obj.getGate().getX(), obj.getGate().getY(), obj.getGate().getZ(), loc.getX(), loc.getY(), loc.getZ());
            if (!(d < last_d)) continue;
            last_d = d;
            closest = obj;
        }
        return closest;
    }

    public static List<GateObject> getAvailableGatesForEdit(Player p) {
        return Gates.values().stream().filter(x -> x.hasOwningRelation(p) && p.hasPermission(Perms.thegate_owner_editgate.value()) || p.hasPermission(Perms.thegate_admin_editgate.value())).collect(Collectors.toList());
    }

    public static void repairAll() {
        for (GateObject o : Gates.values()) {
            o.Repair();
        }
    }

    public static HashMap<String, GateObject> getGates() {
        return Gates;
    }

    public static Set<GateObject> getGatesAsSet() {
        return Sets.newHashSet(Gates.values());
    }

    public static Set<GateObject> getChangedGatesAsSet() {
        HashSet<GateObject> gates = new HashSet<GateObject>();
        for (GateObject obj : Gates.values()) {
            if (!obj.isUpdated()) continue;
            gates.add(obj);
            obj.setUpdated(false);
        }
        return gates;
    }

    public static void setGates(HashMap<String, GateObject> gates) {
        Gates = gates;
    }

    public static Set<GateObject> getGatesOnOtherServer() {
        return GatesOnOtherServer;
    }

    public static void setGatesOnOtherServer(Set<GateObject> gatesOnOtherServer) {
        GatesOnOtherServer = gatesOnOtherServer;
    }
}

