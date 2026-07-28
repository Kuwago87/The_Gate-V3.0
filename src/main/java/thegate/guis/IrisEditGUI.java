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
import com.gui.tools.guitools.InventoryManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thegate.gate.GateObject;
import thegate.guis.EditGateGUI;
import thegate.main.ConfigManager;
import thegate.main.Perms;

public class IrisEditGUI
extends GUIBase {
    private GateObject gate;
    private String newCode = "";
    private int index = 1;
    private EditGateGUI ui;

    public IrisEditGUI(Player p, String name, String Tag, GateObject gate, EditGateGUI ui) {
        super(p, 45, name, Tag);
        this.gate = gate;
        this.ui = ui;
        this.setup();
        this.setupFunctions();
    }

    public void setup() {
        this.addUIAccessPermission(Perms.thegate_owner_editgate.value(), Perms.thegate_admin_editgate.value());
        int i = 9;
        while (i < 45) {
            this.setItem(i, Material.BLACK_STAINED_GLASS_PANE, " ", null);
            ++i;
        }
        this.setItem(0, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(8, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(14, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(23, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(32, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(41, Material.RED_STAINED_GLASS_PANE, " ", null);
        if ((this.gate.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) || this.gate.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString())) && this.getPlayer().hasPermission(Perms.thegate_owner_editidccode.value()) || this.getPlayer().hasPermission(Perms.thegate_admin_editidccode.value())) {
            this.setItem(17, Material.REDSTONE_TORCH, ConfigManager.getString("GUIS.IrisEditGUI.Items.SetNewIDC", new String[0]), null);
            this.setItem(10, Material.MUSIC_DISC_WAIT, "1", null);
            this.setItem(11, Material.MUSIC_DISC_13, "2", null);
            this.setItem(12, Material.MUSIC_DISC_BLOCKS, "3", null);
            this.setItem(19, Material.MUSIC_DISC_CAT, "4", null);
            this.setItem(20, Material.MUSIC_DISC_CHIRP, "5", null);
            this.setItem(21, Material.MUSIC_DISC_FAR, "6", null);
            this.setItem(28, Material.MUSIC_DISC_MALL, "7", null);
            this.setItem(29, Material.MUSIC_DISC_MELLOHI, "8", null);
            this.setItem(30, Material.MUSIC_DISC_STAL, "9", null);
            this.setItem(38, Material.MUSIC_DISC_STRAD, "0", null);
            this.setItem(15, Material.OAK_SIGN, ConfigManager.getString("GUIS.IrisEditGUI.Items.CurrentIDC", "{IDC}", this.gate.getIrisCode()), null);
            this.setItem(40, Material.BARRIER, ConfigManager.getString("GUIS.IrisEditGUI.Items.Clear", new String[0]), null);
        }
        this.setItem(36, Material.HOPPER, ConfigManager.getString("GUIS.IrisEditGUI.Items.Back", new String[0]), null);
        if ((this.gate.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) || this.gate.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString())) && this.getPlayer().hasPermission(Perms.thegate_owner_editirismaterial.value()) || this.getPlayer().hasPermission(Perms.thegate_admin_editirismaterial.value())) {
            this.setItem(35, Material.REDSTONE_TORCH, ConfigManager.getString("GUIS.IrisEditGUI.Items.SetIrisMaterial", new String[0]), null);
            this.setItem(33, this.gate.getIrisMaterial(), ConfigManager.getString("GUIS.IrisEditGUI.Items.IrisMaterial", new String[0]), null);
        }
        this.setItem(16, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(24, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(25, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(26, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(34, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(42, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(43, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(44, Material.WHITE_STAINED_GLASS_PANE, " ", null);
    }

    public void setupFunctions() {
        this.addGUIFunction(36, x -> {
            x.player.closeInventory();
            if (this.ui.OpenGUI()) {
                InventoryManager.addGUI(this.ui);
            }
        }, Material.HOPPER);
        this.addGUIFunction(40, x -> {
            this.index = 1;
            this.newCode = "";
            int i = 1;
            while (i < 8) {
                this.setItem(i, new ItemStack(Material.AIR));
                ++i;
            }
            this.refreshItems();
        }, Material.BARRIER);
        this.addGUIFunction(10, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 1;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_WAIT);
        this.addGUIFunction(11, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 2;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_13);
        this.addGUIFunction(12, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 3;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_BLOCKS);
        this.addGUIFunction(19, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 4;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_CAT);
        this.addGUIFunction(20, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 5;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_CHIRP);
        this.addGUIFunction(21, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 6;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_FAR);
        this.addGUIFunction(28, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 7;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_MALL);
        this.addGUIFunction(29, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 8;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_MELLOHI);
        this.addGUIFunction(30, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 9;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_STAL);
        this.addGUIFunction(38, x -> {
            if (this.index < 8) {
                this.newCode = String.valueOf(this.newCode) + 0;
                this.setItem(this.index++, x.item);
                this.refreshItems();
            }
        }, Material.MUSIC_DISC_STRAD);
        this.addGUIFunction(17, x -> {
            if (this.index == 8 && this.newCode.length() == 7) {
                this.gate.setIrisCode(this.newCode);
                this.setItem(15, Material.OAK_SIGN, ConfigManager.getString("GUIS.IrisEditGUI.Items.CurrentIDC", "{IDC}", this.gate.getIrisCode()), null);
                this.refreshItems();
                x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.IrisEditGUI.Message1", new String[0]));
                this.gate.setUpdated(true);
            } else {
                x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.IrisEditGUI.Message2", new String[0]));
            }
        }, Material.REDSTONE_TORCH);
        this.addGUIFunction(35, x -> {
            if (!x.base.getItem(33).getType().equals((Object)this.gate.getIrisMaterial())) {
                this.gate.setIrisMaterial(x.base.getItem(33).getType());
                x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.IrisEditGUI.Message3", new String[0]));
                this.gate.setUpdated(true);
                this.gate.Repair();
            }
        }, Material.REDSTONE_TORCH);
        this.setGeneralFunction(x -> {
            if (x.index == 33 && !x.event.getView().getCursor().getType().equals((Object)Material.AIR)) {
                ItemStack i = this.getItem(x.index);
                i.setType(x.event.getView().getCursor().getType());
                this.setItem(x.index, i);
                this.refreshItems();
            }
        });
    }
}

