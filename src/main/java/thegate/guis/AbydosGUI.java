/*
 * Decompiled with CFR 0.152.
 */
package thegate.guis;

import com.gui.tools.guitools.GUIPages;
import com.gui.tools.guitools.InventoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.guis.EditGateGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;

public class AbydosGUI
extends GUIPages {
    ArrayList<GateObject> userGates = new ArrayList();
    ArrayList<ItemStack> gateItems = new ArrayList();

    public AbydosGUI(Player p, String name) {
        super(p, 54, name, "AbydosGUI");
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.addUIAccessPermission(Perms.thegate_admin_abydoscartouche.value(), Perms.thegate_user_abydoscartouche.value());
        this.setDefaultErrorMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
        int i = 0;
        while (i < 45) {
            this.additemPerms(i, Perms.thegate_owner_editgate.value(), Perms.thegate_admin_editgate.value());
            ++i;
        }
    }

    public void setup() {
        Set<GateObject> fullSet = GateManager.getGatesAsSet();
        fullSet.addAll(GateManager.getGatesOnOtherServer());
        List<GateObject> sorted = fullSet.stream().sorted((x, y) -> x.getAddress().compareTo(y.getAddress())).collect(Collectors.toList());
        List<GateObject> owned = new ArrayList<>();
        List<GateObject> accessable = new ArrayList<>();
        List<GateObject> rest = new ArrayList<>();
        if (this.getPlayer().hasPermission(Perms.thegate_admin_abydoscartouche.value())) {
            owned = sorted.stream().filter(x -> x.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString())).collect(Collectors.toList());
            accessable = sorted.stream().filter(x -> x.getCoOwner().containsKey(this.getPlayer().getUniqueId().toString())).collect(Collectors.toList());
            rest = sorted.stream().filter(x -> !x.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) && !x.getCoOwner().containsKey(this.getPlayer().getUniqueId().toString())).collect(Collectors.toList());
        } else if (this.getPlayer().hasPermission(Perms.thegate_user_abydoscartouche.value())) {
            owned = sorted.stream().filter(x -> x.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString())).collect(Collectors.toList());
            accessable = sorted.stream().filter(x -> x.getCoOwner().containsKey(this.getPlayer().getUniqueId().toString())).collect(Collectors.toList());
            rest = sorted.stream().filter(x -> this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_visablenetwork_.value()) + x.getNetwork()) && x.isOpen() && !x.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) && !x.getCoOwner().containsKey(this.getPlayer().getUniqueId().toString())).collect(Collectors.toList());
        }
        this.userGates.addAll(owned);
        this.userGates.addAll(accessable);
        this.userGates.addAll(rest);
        int i = 0;
        while (i < this.userGates.size()) {
            GateObject GATE = this.userGates.get(i);
            String GATENAME = GATE.getGateName();
            String WORLD = GATE.getWorldName();
            String LocX = String.valueOf(GATE.getGate().getX());
            String LocY = String.valueOf(GATE.getGate().getY());
            String LocZ = String.valueOf(GATE.getGate().getZ());
            String NET = GATE.getNetwork();
            String OWNER = GATE.getOwnerName();
            String DESCRIPTION = GATE.getDescription();
            List<String> lore = ConfigManager.getStringList("GUIS.AbydosGUI.Items.GateObjects.Lore", "{GATENAME}", GATENAME, "{WORLD}", WORLD, "{X}", LocX, "{Y}", LocY, "{Z}", LocZ, "{NETWORK}", NET, "{OWNER}", OWNER, "{DESCRIPTION}", DESCRIPTION);
            this.gateItems.add(this.createItem(GATE.getAddress(), lore, Globals.UseBungee && !GATE.getServer().equals(Globals.ServerName) ? Material.ENDER_PEARL : Material.HEART_OF_THE_SEA));
            ++i;
        }
        this.setSorceList(this.gateItems);
        this.setNextPage(this.createItem(ConfigManager.getString("GUIS.AbydosGUI.Items.NextPage", new String[0]), null, Material.PAPER));
        this.setPrevPage(this.createItem(ConfigManager.getString("GUIS.AbydosGUI.Items.PreviousPage", new String[0]), null, Material.PAPER));
    }

    public void setupFunctions() {
        this.setGeneralFunction(x -> {
            String name;
            if (x.event.getRawSlot() < 45 && x.item.getType().equals(Material.HEART_OF_THE_SEA) && x.item.hasItemMeta() && GateManager.hasGateWithAddress(name = x.item.getItemMeta().getDisplayName()) && (GateManager.getGateWithAddress(name).isOwner(x.player) && x.player.hasPermission(Perms.thegate_owner_editgate.value()) || x.player.hasPermission(Perms.thegate_admin_editgate.value()) || x.player.hasPermission(Perms.thegate_owner_editgate.value()) && GateManager.getGateWithAddress(name).getCoOwner().get(x.player.getUniqueId().toString()) != null)) {
                x.player.closeInventory();
                EditGateGUI editGateGUI = new EditGateGUI(x.player, ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", name), name, GateManager.getGateWithAddress(name));
                if (editGateGUI.OpenGUI()) {
                    InventoryManager.addGUI(editGateGUI);
                }
            }
        });
    }
}

