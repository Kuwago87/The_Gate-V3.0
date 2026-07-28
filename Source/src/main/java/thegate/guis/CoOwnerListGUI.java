/*
 * Decompiled with CFR 0.152.
 */
package thegate.guis;

import com.gui.tools.guitools.GUIPages;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import thegate.gate.GateObject;
import thegate.main.Globals;
import thegate.main.TheGateMain;

public class CoOwnerListGUI
extends GUIPages {
    private GateObject gate;
    private boolean editOwner = false;

    public CoOwnerListGUI(Player p, String name, GateObject Gate, boolean editOwner) {
        super(p, 54, name, "CoOwnerListGUI");
        this.gate = Gate;
        this.editOwner = editOwner;
        this.Perms();
        this.setup();
        this.setupFunctions();
    }

    public void Perms() {
    }

    public void setup() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if (this.editOwner && Globals.UseDummyOwner) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta)item.getItemMeta();
            meta.setDisplayName(Globals.dummyOwnerName);
            item.setItemMeta(meta);
            items.add(item);
        }
        Map<String, String> onlinePlayer = this.gate.getCoOwner();
        for (String u : onlinePlayer.keySet()) {
            ItemMeta meta;
            ItemStack item;
            Player p = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(u));
            if (p != null) {
                item = new ItemStack(Material.PLAYER_HEAD);
                meta = (SkullMeta)item.getItemMeta();
                ((SkullMeta) meta).setOwningPlayer(p);
                meta.setDisplayName(p.getName());
                item.setItemMeta(meta);
                items.add(item);
                continue;
            }
            item = new ItemStack(Material.ENDER_PEARL);
            meta = item.getItemMeta();
            meta.setDisplayName(onlinePlayer.get(u));
            item.setItemMeta(meta);
            items.add(item);
        }
        this.setSorceList(items);
    }

    public void setupFunctions() {
    }
}

