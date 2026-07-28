/*
 * Decompiled with CFR 0.152.
 */
package thegate.guis;

import com.google.common.collect.Sets;
import com.gui.tools.guitools.GUIPages;
import com.gui.tools.guitools.InventoryManager;
import java.util.ArrayList;
import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import thegate.gate.GateObject;
import thegate.guis.EditGateGUI;
import thegate.main.ConfigManager;
import thegate.main.Perms;
import thegate.main.TheGateMain;

public class CoOwnerAddGUI
extends GUIPages {
    GateObject gate;

    public CoOwnerAddGUI(Player p, String name, GateObject gate) {
        super(p, 54, name, "CoOwnerAddGUI");
        this.gate = gate;
        this.setup();
        this.setupFunctions();
    }

    public void Perms() {
    }

    public void setup() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        HashSet<? extends Player> onlinePlayer = Sets.newHashSet(TheGateMain.theGateMain.getServer().getOnlinePlayers());
        for (Player player : onlinePlayer) {
            if (player.equals(this.getPlayer()) && !player.hasPermission(Perms.thegate_admin_editowner.value())) continue;
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta)item.getItemMeta();
            meta.setOwningPlayer(player);
            meta.setDisplayName(player.getName());
            item.setItemMeta(meta);
            items.add(item);
        }
        this.setSorceList(items);
    }

    public void setupFunctions() {
        this.setGeneralFunction(x -> {
            Player p;
            String name;
            if (!(x.index >= 45 || (name = x.item.getItemMeta().getDisplayName()) == null && name == "" || (p = TheGateMain.theGateMain.getServer().getPlayer(name)) == null || this.gate.getCoOwner().keySet().contains(p.getUniqueId().toString()))) {
                this.gate.addCoOwner(p.getUniqueId().toString(), p.getName());
                x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.AddCoOwner.Message2", "{PLAYER_NAME}", p.getName()));
                p.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.AddCoOwner.Message1", "{ADDRESS}", this.gate.getAddress()));
                TheGateMain.SaveLoadInterface.AddPlayerToCoowner(p.getUniqueId().toString(), p.getName(), this.gate.getAddress());
                x.player.closeInventory();
                EditGateGUI gui = new EditGateGUI(x.player, ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{PLAYER_NAME}", p.getName()), this.gate.getAddress(), this.gate);
                if (gui.OpenGUI()) {
                    InventoryManager.addGUI(gui);
                }
            }
        });
    }
}

