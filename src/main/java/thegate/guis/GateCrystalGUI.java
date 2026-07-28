/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package thegate.guis;

import com.gui.tools.guitools.GUIBase;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.ConfigManager;
import thegate.main.Globals;

public class GateCrystalGUI
extends GUIBase {
    private String Address;

    public GateCrystalGUI(Player p, String name, String Address) {
        super(p, 9, name, "GateCrystalGUI");
        this.Address = Address;
        this.setup();
        this.CondPerms();
    }

    public void CondPerms() {
        this.setGUIAccessCondition(c2 -> {
            GateObject o = GateManager.getGateWithAddress(this.Address);
            if (o != null) {
                return o.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString());
            }
            return false;
        });
    }

    public void setup() {
        this.setItem(0, Material.BLACK_STAINED_GLASS_PANE, " ", null);
        this.setItem(1, Material.BLACK_STAINED_GLASS_PANE, " ", null);
        this.setItem(8, Material.BLACK_STAINED_GLASS_PANE, " ", null);
        this.setItem(7, Material.BLACK_STAINED_GLASS_PANE, " ", null);
        this.setItem(2, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(6, Material.RED_STAINED_GLASS_PANE, " ", null);
        ItemStack DHDCrystal = new ItemStack(Globals.DefaultGateCrystal);
        ItemMeta DHDCrystalMeta = DHDCrystal.getItemMeta();
        DHDCrystalMeta.setDisplayName(ConfigManager.getString("GUIS.GateCrystalGUI.Items.DHDCrystal.Name", new String[0]));
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(ConfigManager.getString("GUIS.GateCrystalGUI.Items.DHDCrystal.Lore", new String[0]));
        Lore.add(this.Address);
        DHDCrystalMeta.setLore(Lore);
        DHDCrystal.setItemMeta(DHDCrystalMeta);
        this.setItem(4, DHDCrystal);
        this.addNoCancleRange(4);
    }
}

