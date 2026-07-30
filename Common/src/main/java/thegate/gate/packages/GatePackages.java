/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 */
package thegate.gate.packages;

import com.packageing.tools.packagetools.PackageManager;
import com.packageing.tools.packagetools.entitys.ArmorStand;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import thegate.gate.GateChevron;
import thegate.gate.GateObject;
import thegate.gate.IrisPart;
import thegate.main.Globals;
import thegate.main.Perms;

public class GatePackages {
    public ArmorStand[] Symbol = null;
    public ArrayList<GateChevron> Chevrons = null;
    private List<Integer> entityIDs = new ArrayList<Integer>();
    public ArmorStand[] Ring = null;
    public Set<ArmorStand> DisplayTextStands = null;
    public Vector[] SymbolPositionAfterRotation = null;
    public Map<Integer, Set<ArmorStand>> horizon = null;
    public Map<ArmorStand, Vector> horizonLocations = null;
    public Map<ArmorStand, Vector> horizonHeadPosition = null;
    private Set<IrisPart> iris = new HashSet<IrisPart>();
    private GateObject gate = null;

    public GatePackages(GateObject g) {
        this.gate = g;
    }

    public void addEntityID(ArmorStand stand) {
        this.entityIDs.add(stand.getEntityID());
    }

    public void Display(Player p) {
        if (this.gate.isIrisClosed()) {
            for (IrisPart s : this.iris) {
                s.display(p);
            }
        }
        int i = 0;
        while (i < 16) {
            ArmorStand armorStand = this.Symbol[i];
            PackageManager.SendSpawnPackage(armorStand, p);
            ++i;
        }
        ArmorStand[] armorStandArray = this.Ring;
        int n = this.Ring.length;
        int var3_6 = 0;
        while (var3_6 < n) {
            ArmorStand stand2 = armorStandArray[var3_6];
            PackageManager.SendSpawnPackage(stand2, p);
            ++var3_6;
        }
        for (GateChevron c2 : this.Chevrons) {
            PackageManager.SendSpawnPackage(c2.FrameBot, p);
            PackageManager.SendSpawnPackage(c2.LightBot, p);
            PackageManager.SendSpawnPackage(c2.LightTop, p);
            PackageManager.SendSpawnPackage(c2.LFrameBotLeft, p);
            PackageManager.SendSpawnPackage(c2.LFrameBotRight, p);
            PackageManager.SendSpawnPackage(c2.LFrameTopLeft, p);
            PackageManager.SendSpawnPackage(c2.LFrameTopRight, p);
        }
        if (this.gate.woosh != null) {
            i = 1;
            while (i <= this.gate.woosh.layerDone) {
                for (ArmorStand armorStand : this.horizon.get(i)) {
                    PackageManager.SendSpawnPackage(armorStand, p);
                }
                ++i;
            }
        }
        if (Globals.DisplayName && p.hasPermission(Perms.thegate_user_viewgatename.value())) {
            for (ArmorStand stand4 : this.DisplayTextStands) {
                PackageManager.SendSpawnPackage(stand4, p);
            }
        }
    }

    public void Refresh() {
        for (Player p : this.gate.getPlayerInRange()) {
            int i = 0;
            while (i < 16) {
                ArmorStand stand = this.Symbol[i];
                PackageManager.SendUpdate(stand, p);
                ++i;
            }
            ArmorStand[] armorStandArray = this.Ring;
            int n = this.Ring.length;
            int n2 = 0;
            while (n2 < n) {
                ArmorStand stand = armorStandArray[n2];
                PackageManager.SendSpawnPackage(stand, p);
                ++n2;
            }
            for (GateChevron c2 : this.Chevrons) {
                PackageManager.SendSpawnPackage(c2.FrameBot, p);
                PackageManager.SendSpawnPackage(c2.LightBot, p);
                PackageManager.SendSpawnPackage(c2.LightTop, p);
                PackageManager.SendSpawnPackage(c2.LFrameBotLeft, p);
                PackageManager.SendSpawnPackage(c2.LFrameBotRight, p);
                PackageManager.SendSpawnPackage(c2.LFrameTopLeft, p);
                PackageManager.SendSpawnPackage(c2.LFrameTopRight, p);
            }
            if (!Globals.DisplayName || !p.hasPermission(Perms.thegate_user_viewgatename.value())) continue;
            for (ArmorStand stand : this.DisplayTextStands) {
                PackageManager.SendSpawnPackage(stand, p);
            }
        }
    }

    public ArmorStand[] getSymbol() {
        return this.Symbol;
    }

    public void setSymbol(ArmorStand[] symbol) {
        this.Symbol = symbol;
    }

    public ArrayList<GateChevron> getChevrons() {
        return this.Chevrons;
    }

    public void setChevrons(ArrayList<GateChevron> chevrons) {
        this.Chevrons = chevrons;
    }

    public ArmorStand[] getRing() {
        return this.Ring;
    }

    public void setRing(ArmorStand[] ring) {
        this.Ring = ring;
    }

    public Set<ArmorStand> getDisplayTextStands() {
        return this.DisplayTextStands;
    }

    public void setDisplayTextStands(Set<ArmorStand> displayTextStands) {
        this.DisplayTextStands = displayTextStands;
    }

    public Vector[] getSymbolPositionAfterRotation() {
        return this.SymbolPositionAfterRotation;
    }

    public void setSymbolPositionAfterRotation(Vector[] symbolPositionAfterRotation) {
        this.SymbolPositionAfterRotation = symbolPositionAfterRotation;
    }

    public Map<Integer, Set<ArmorStand>> getHorizon() {
        return this.horizon;
    }

    public void setHorizon(Map<Integer, Set<ArmorStand>> horizon) {
        this.horizon = horizon;
    }

    public Map<ArmorStand, Vector> getHorizonLocations() {
        return this.horizonLocations;
    }

    public void setHorizonLocations(Map<ArmorStand, Vector> horizonLocations) {
        this.horizonLocations = horizonLocations;
    }

    public Map<ArmorStand, Vector> getHorizonHeadPosition() {
        return this.horizonHeadPosition;
    }

    public void setHorizonHeadPosition(Map<ArmorStand, Vector> horizonHeadPosition) {
        this.horizonHeadPosition = horizonHeadPosition;
    }

    public Set<IrisPart> getIris() {
        return this.iris;
    }

    public void setIris(Set<IrisPart> iris) {
        this.iris = iris;
    }

    public List<Integer> getEntityIDs() {
        return this.entityIDs;
    }

    public void setEntityIDs(List<Integer> entityIDs) {
        this.entityIDs = entityIDs;
    }
}

