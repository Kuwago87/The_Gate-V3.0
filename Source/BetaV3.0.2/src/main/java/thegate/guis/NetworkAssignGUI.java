/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package thegate.guis;

import com.gui.tools.guitools.GUIPages;
import com.gui.tools.guitools.InventoryManager;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thegate.gate.GateObject;
import thegate.guis.EditGateGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;

public class NetworkAssignGUI
extends GUIPages {
    public GateObject gate;
    ArrayList<String> Network = new ArrayList();
    private String NetPrio;
    private EditGateGUI ui;

    public NetworkAssignGUI(Player p, String name, String NetPrio, GateObject gate, EditGateGUI ui) {
        super(p, 54, name, "NetworkAssignGUI");
        this.gate = gate;
        this.ui = ui;
        this.NetPrio = NetPrio;
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.setDefaultErrorMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
        this.addUIAccessPermission(Perms.thegate_owner_editnetwork.value(), Perms.thegate_admin_editgate.value());
    }

    public void setup() {
        if (this.NetPrio.equals(ConfigManager.getString("GUIS.NetworkAssignGUI.Secondary", new String[0]))) {
            this.Network.add("null");
        }
        int i = 0;
        while (i < Globals.Networks.size()) {
            String network = Globals.Networks.get(i);
            if (!(!this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_visablenetwork_.value()) + network) || this.NetPrio.contains("SecundNetwork") && network.equals(Globals.Networks.get(0)))) {
                this.Network.add(network);
            }
            ++i;
        }
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        int i2 = 0;
        while (i2 < this.Network.size()) {
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(this.Network.get(i2));
            items.add(this.createItem(ConfigManager.getString("GUIS.NetworkAssignGUI.Items.Network", new String[0]), lore, Material.END_CRYSTAL));
            ++i2;
        }
        this.setSorceList(items);
        this.addControleItem(0, this.createItem(ConfigManager.getString("GUIS.NetworkAssignGUI.Items.Back", new String[0]), null, Material.HOPPER));
    }

    public void setupFunctions() {
        this.addControleItemFunctions(0, x -> {
            x.player.closeInventory();
            if (this.ui.OpenGUI()) {
                InventoryManager.addGUI(this.ui);
            }
        });
        this.setGeneralFunction(x -> {
            this.gate.setUpdated(true);
            if (x.index < 45 && x.index > -1) {
                if (x.event.getView().getTitle().contains(ConfigManager.getString("GUIS.NetworkAssignGUI.Primary", new String[0]))) {
                    if (((String)x.item.getItemMeta().getLore().get(0)).contains(Globals.Networks.get(0))) {
                        this.gate.setNetwork((String)x.item.getItemMeta().getLore().get(0));
                        this.gate.Repair();
                        x.player.closeInventory();
                        EditGateGUI egg = new EditGateGUI(x.player, ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.gate.getAddress()), this.gate.getAddress(), this.gate);
                        if (egg.OpenGUI()) {
                            InventoryManager.addGUI(egg);
                        }
                    } else {
                        this.gate.setNetwork((String)x.item.getItemMeta().getLore().get(0));
                        this.gate.setSecundaryNetwork("null");
                        this.gate.Repair();
                        x.player.closeInventory();
                        EditGateGUI egg = new EditGateGUI(x.player, ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.gate.getAddress()), this.gate.getAddress(), this.gate);
                        if (egg.OpenGUI()) {
                            InventoryManager.addGUI(egg);
                        }
                    }
                } else if (x.event.getView().getTitle().contains(ConfigManager.getString("GUIS.NetworkAssignGUI.Secondary", new String[0]))) {
                    this.gate.setSecundaryNetwork((String)x.item.getItemMeta().getLore().get(0));
                    this.gate.Repair();
                    x.player.closeInventory();
                    EditGateGUI egg = new EditGateGUI(x.player, ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.gate.getAddress()), this.gate.getAddress(), this.gate);
                    if (egg.OpenGUI()) {
                        InventoryManager.addGUI(egg);
                    }
                }
            }
        });
    }

    public boolean ContaindInNetworkList(String net) {
        int i = 0;
        while (i < Globals.Networks.size()) {
            if (net.equals(Globals.Networks.get(i))) {
                return true;
            }
            ++i;
        }
        return false;
    }
}

