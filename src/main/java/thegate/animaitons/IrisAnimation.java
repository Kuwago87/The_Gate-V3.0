/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package thegate.animaitons;

import com.gui.tools.guitools.GUIBase;
import com.gui.tools.guitools.InventoryManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import thegate.gate.GateObject;
import thegate.gate.IrisPart;
import thegate.guis.IrisIDCGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.TheGateMain;

public class IrisAnimation
extends BukkitRunnable {
    private boolean open;
    private GateObject gate;
    private double maxTime = 60.0;
    private double dTime = 0.0;
    private double dTimeInc = Globals.IrisSpeed;
    private double angle = 60.0;
    private double delay = 5.0;

    public IrisAnimation(GateObject gate, boolean open) {
        this.open = open;
        this.gate = gate;
        this.dTime = open ? 60.0 : 0.0;
        if (!open) {
            for (IrisPart part : gate.getPackages().getIris()) {
                for (Player p : gate.getPlayerInRange()) {
                    part.display(p);
                }
            }
        }
        if (!Globals.DoIrisAnimaiton) {
            if (gate.isIrisClosed()) {
                for (IrisPart part : gate.getPackages().getIris()) {
                    for (Player p : gate.getPlayerInRange()) {
                        part.vanish(p);
                    }
                }
            } else {
                for (IrisPart part : gate.getPackages().getIris()) {
                    part.setRotation(Math.toRadians(open ? 0 : 60));
                    for (Player p : gate.getPlayerInRange()) {
                        part.move(p);
                    }
                }
            }
            for (GUIBase gui : InventoryManager.getGUIbyTag("IDCGUI")) {
                IrisIDCGUI guiIDC;
                if (!(gui instanceof IrisIDCGUI) || !(guiIDC = (IrisIDCGUI)gui).getConnectedGate().getAddress().equals(gate.getAddress())) continue;
                guiIDC.setItem(26, Material.GREEN_CONCRETE, ConfigManager.getString("GUIS.IDCTransmitter.Items.Open", new String[0]), null);
                guiIDC.refreshItems();
            }
            gate.setIrisClosed(!open);
            return;
        }
        this.runTaskTimerAsynchronously((Plugin)TheGateMain.theGateMain, 5L, Globals.IrisAnimaitonTicks);
    }

    public void run() {
        double change = this.dTime / this.maxTime;
        if (change > 1.0 || change < 0.0) {
            if (this.delay <= 0.0) {
                this.gate.setIrisClosed(!this.open);
                if (!this.gate.isIrisClosed()) {
                    for (IrisPart part : this.gate.getPackages().getIris()) {
                        for (Player p : this.gate.getPlayerInRange()) {
                            part.vanish(p);
                        }
                    }
                }
                this.gate.irisAnimaiton = null;
                this.cancel();
                for (GUIBase gui : InventoryManager.getGUIbyTag("IDCGUI")) {
                    IrisIDCGUI guiIDC;
                    if (!(gui instanceof IrisIDCGUI) || !(guiIDC = (IrisIDCGUI)gui).getConnectedGate().getAddress().equals(this.gate.getAddress())) continue;
                    guiIDC.setItem(26, Material.GREEN_CONCRETE, ConfigManager.getString("GUIS.IDCTransmitter.Items.Open", new String[0]), null);
                    guiIDC.refreshItems();
                }
            } else {
                this.delay -= 1.0;
            }
        } else {
            for (IrisPart part : this.gate.getPackages().getIris()) {
                part.setRotation(Math.toRadians(this.angle * change));
                for (Player p : this.gate.getPlayerInRange()) {
                    part.move(p);
                }
            }
            this.dTime = this.open ? (this.dTime -= this.dTimeInc) : (this.dTime += this.dTimeInc);
        }
    }
}

