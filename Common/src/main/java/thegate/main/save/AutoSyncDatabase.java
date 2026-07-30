/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.scheduler.BukkitRunnable
 */
package thegate.main.save;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.TheGateMain;

public class AutoSyncDatabase
extends BukkitRunnable {
    private TheGateMain mainGate;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public AutoSyncDatabase(TheGateMain mainGate) {
        this.mainGate = mainGate;
        mainGate.AutoSyncTime = LocalDateTime.now();
    }

    public void run() {
        Set<GateObject> newGateList = TheGateMain.SaveLoadInterface.GateListOtherServers();
        Set<GateObject> changedGates = GateManager.getChangedGatesAsSet();
        GateManager.setGatesOnOtherServer(newGateList);
        TheGateMain.SaveLoadInterface.SaveDataFromSet(changedGates);
        if (newGateList.size() != GateManager.getGatesOnOtherServer().size()) {
            for (GateObject obj : GateManager.getGates().values()) {
                Location Loc = obj.getDHD();
                if (Loc == null) continue;
                int i = 0;
                while (i < 100) {
                    obj.getDHD().getWorld().spawnParticle(Particle.ENCHANT, Loc.getX() + 0.5 + (new Random().nextDouble() * 2.0 - 1.0), Loc.getY() + 1.0 + (new Random().nextDouble() * 2.0 - 1.0) * 0.5, Loc.getZ() + 0.5 + (new Random().nextDouble() * 2.0 - 1.0), 0);
                    ++i;
                }
            }
        }
        this.mainGate.AutoSyncTime = LocalDateTime.now();
    }
}

