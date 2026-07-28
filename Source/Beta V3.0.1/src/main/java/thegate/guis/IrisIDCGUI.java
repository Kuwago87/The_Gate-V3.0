/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package thegate.guis;

import com.gui.tools.guitools.GUIBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thegate.animaitons.IrisAnimation;
import thegate.gate.GateObject;
import thegate.main.ConfigManager;

public class IrisIDCGUI
extends GUIBase {
    private GateObject connectedGate;
    private int index = 1;
    private String transmittCode = "";

    public IrisIDCGUI(Player p, String name, String Tag, GateObject connectedGate) {
        super(p, 45, name, Tag);
        this.connectedGate = connectedGate;
        this.setup();
        this.setupFunciton();
    }

    public void setup() {
        int i = 9;
        while (i < 45) {
            this.setItem(i, Material.BLACK_STAINED_GLASS_PANE, " ", null);
            ++i;
        }
        this.setItem(0, Material.RED_STAINED_GLASS_PANE, "", null);
        this.setItem(8, Material.RED_STAINED_GLASS_PANE, "", null);
        this.setItem(12, Material.MUSIC_DISC_WAIT, "1", null);
        this.setItem(13, Material.MUSIC_DISC_13, "2", null);
        this.setItem(14, Material.MUSIC_DISC_BLOCKS, "3", null);
        this.setItem(21, Material.MUSIC_DISC_CAT, "4", null);
        this.setItem(22, Material.MUSIC_DISC_CHIRP, "5", null);
        this.setItem(23, Material.MUSIC_DISC_FAR, "6", null);
        this.setItem(30, Material.MUSIC_DISC_MALL, "7", null);
        this.setItem(31, Material.MUSIC_DISC_MELLOHI, "8", null);
        this.setItem(32, Material.MUSIC_DISC_STAL, "9", null);
        this.setItem(40, Material.MUSIC_DISC_STRAD, "0", null);
        this.setItem(36, Material.BARRIER, ConfigManager.getString("GUIS.IDCTransmitter.Items.Clear", new String[0]), null);
        this.setItem(44, Material.END_CRYSTAL, ConfigManager.getString("GUIS.IDCTransmitter.Items.Transmitt", new String[0]), null);
        if (this.connectedGate.isIrisClosed()) {
            this.setItem(26, Material.RED_CONCRETE, ConfigManager.getString("GUIS.IDCTransmitter.Items.Closed", new String[0]), null);
        } else if (!this.connectedGate.isIrisClosed()) {
            this.setItem(26, Material.GREEN_CONCRETE, ConfigManager.getString("GUIS.IDCTransmitter.Items.Open", new String[0]), null);
        }
    }

    public void setupFunciton() {
        this.addGUIFunction(36, x -> {
            this.index = 1;
            this.transmittCode = "";
            int i = 1;
            while (i < 8) {
                this.setItem(i, new ItemStack(Material.AIR));
                ++i;
            }
            this.refreshItems();
        }, Material.BARRIER);
        this.addGUIFunction(12, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 1;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_WAIT);
        this.addGUIFunction(13, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 2;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_13);
        this.addGUIFunction(14, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 3;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_BLOCKS);
        this.addGUIFunction(21, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 4;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_CAT);
        this.addGUIFunction(22, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 5;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_CHIRP);
        this.addGUIFunction(23, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 6;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_FAR);
        this.addGUIFunction(30, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 7;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_MALL);
        this.addGUIFunction(31, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 8;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_MELLOHI);
        this.addGUIFunction(32, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 9;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_STAL);
        this.addGUIFunction(40, x -> {
            if (this.index < 8) {
                this.transmittCode = String.valueOf(this.transmittCode) + 0;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_STRAD);
        this.addGUIFunction(44, x -> {
            if (!this.transmittCode.equals(this.connectedGate.getIrisCode())) {
                this.setItem(26, Material.BLACK_CONCRETE, ConfigManager.getString("GUIS.IDCTransmitter.Items.CodeDenied", new String[0]), null);
                this.refreshItems();
                return;
            }
            if (this.connectedGate.isActive() && this.connectedGate.isIrisClosed() && this.connectedGate.irisAnimaiton == null) {
                this.connectedGate.irisAnimaiton = new IrisAnimation(this.connectedGate, true);
                this.setItem(26, Material.ORANGE_CONCRETE, ConfigManager.getString("GUIS.IDCTransmitter.Items.Opening", new String[0]), null);
                this.refreshItems();
            }
        }, Material.END_CRYSTAL);
    }

    public GateObject getConnectedGate() {
        return this.connectedGate;
    }

    public void setConnectedGate(GateObject connectedGate) {
        this.connectedGate = connectedGate;
    }
}

