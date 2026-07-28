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
import thegate.gate.BlockedState;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.guis.DHD_GUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;

public class QuickDialGUI
extends GUIPages {
    GateObject gate;
    ArrayList<GateObject> userGates = new ArrayList();
    private DHD_GUI ui;

    public QuickDialGUI(Player p, String name, GateObject gate, DHD_GUI ui) {
        super(p, 54, name, "QuickDialGUI");
        this.gate = gate;
        this.ui = ui;
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.addUIAccessPermission(Perms.thegate_user_quickdial.value());
        this.setDefaultErrorMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
    }

    public void setup() {
        Set<GateObject> gates = GateManager.getGatesAsSet();
        if (Globals.UseBungee) {
            gates.addAll(GateManager.getGatesOnOtherServer());
        }
        List<GateObject> sortedList = gates.stream().sorted((x, y) -> x.getAddress().compareTo(y.getAddress())).collect(Collectors.toList());
        List<GateObject> owned = sortedList.stream().filter(x -> {
            if ((x.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) || x.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString())) && !x.getAddress().equals(this.gate.getAddress())) {
                return this.gate.getNetwork().equals(x.getNetwork()) || this.gate.getNetwork().equals(x.getSecondaryNetwork()) || this.gate.getSecondaryNetwork().equals(x.getNetwork());
            }
            return false;
        }).collect(Collectors.toList());
        List<GateObject> accessable = sortedList.stream().filter(x -> {
            if (!x.getAddress().equals(this.gate.getAddress()) && (this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_visablenetwork_.value()) + x.getNetwork()) || !x.getSecondaryNetwork().equals("null") && this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_visablenetwork_.value()) + x.getSecondaryNetwork())) && (x.isOpen() || this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value())) && !x.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) && !x.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString())) {
                return this.gate.getNetwork().equals(x.getNetwork()) || this.gate.getNetwork().equals(x.getSecondaryNetwork()) || this.gate.getSecondaryNetwork().equals(x.getNetwork());
            }
            return false;
        }).collect(Collectors.toList());
        this.userGates.addAll(owned);
        this.userGates.addAll(accessable);
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
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
            List<String> lore = ConfigManager.getStringList("GUIS.QuickDialGUI.Items.GateObjects.Lore", "{GATENAME}", GATENAME, "{WORLD}", WORLD, "{X}", LocX, "{Y}", LocY, "{Z}", LocZ, "{NETWORK}", NET, "{OWNER}", OWNER, "{DESCRIPTION}", DESCRIPTION);
            items.add(this.createItem(GATE.getAddress(), lore, Globals.UseBungee && !GATE.getServer().equals(Globals.ServerName) ? Material.ENDER_PEARL : Material.HEART_OF_THE_SEA));
            ++i;
        }
        this.setSorceList(items);
        this.setNextPage(this.createItem(ConfigManager.getString("GUIS.QuickDialGUI.Items.NextPage", new String[0]), null, Material.PAPER));
        this.setPrevPage(this.createItem(ConfigManager.getString("GUIS.QuickDialGUI.Items.PreviousPage", new String[0]), null, Material.PAPER));
        this.addControleItem(0, this.createItem(ConfigManager.getString("GUIS.QuickDialGUI.Items.Back", new String[0]), null, Material.HOPPER));
    }

    public void setupFunctions() {
        this.addControleItemFunctions(0, x -> {
            x.player.closeInventory();
            if (this.ui.OpenGUI()) {
                InventoryManager.addGUI(this.ui);
            }
        });
        this.setGeneralFunction(x -> {
            if (x.item == null || x.event.getRawSlot() >= 45 || x.item.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                return;
            }
            String Address = x.item.getItemMeta().getDisplayName();
            GateObject destinationGate = GateManager.getGateWithAddress(Address);
            if (this.gate.isDialinginProssed() || this.gate.isDialingout()) {
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.QuickDialGUI.Message3", new String[0]));
                return;
            }
            if (this.gate.isUseGatePerms() && !this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_dial_.value()) + this.gate.getAddress()) && !this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value())) {
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                return;
            }
            if (!Globals.UseBungee && destinationGate == null) {
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.QuickDialGUI.Message1", "{ADDRESS}", Address));
                this.getPlayer().closeInventory();
                return;
            }
            if (!(Globals.UseBungee || destinationGate.isOpen() || destinationGate.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()))) {
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.QuickDialGUI.Message2", new String[0]));
                this.getPlayer().closeInventory();
                return;
            }
            if (Globals.UseBungee && destinationGate == null && GateManager.hasGateOnOtherServerWithAddress(Address) && !this.gate.isActive() && !this.gate.isDialinginProssed()) {
                this.bungeeDial(destinationGate, this.gate, Address);
                return;
            }
            if (destinationGate.isUseGatePerms() && !this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_dial_.value()) + destinationGate.getAddress()) && !this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value())) {
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                return;
            }
            if (this.gate.getNetwork().equals(destinationGate.getNetwork()) || this.gate.getNetwork().equals(destinationGate.getSecondaryNetwork()) || this.gate.getSecondaryNetwork().equals(destinationGate.getNetwork())) {
                if (GateManager.hasGateWithAddress(Address)) {
                    if (!(destinationGate.isActive() || this.gate.isActive() || destinationGate.getBlockedState().equals((Object)BlockedState.block_incoming) || destinationGate.getBlockedState().equals((Object)BlockedState.locked) || this.gate.getBlockedState().equals((Object)BlockedState.block_outgoing) || this.gate.getBlockedState().equals((Object)BlockedState.locked) || destinationGate.isDialinginProssed() || this.gate.isDialinginProssed())) {
                        destinationGate.Activate(this.gate.getAddress(), false, this.getPlayer());
                        this.gate.Activate(destinationGate.getAddress(), true, this.getPlayer());
                        this.getPlayer().closeInventory();
                    } else {
                        this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.QuickDialGUI.Message3", new String[0]));
                        this.getPlayer().closeInventory();
                    }
                }
            } else {
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.QuickDialGUI.Message4", new String[0]));
                this.getPlayer().closeInventory();
            }
        });
    }

    private void bungeeDial(GateObject destinationGate, GateObject gate, String Address) {
        destinationGate = GateManager.getGateOnOtherServerWithAddress(Address);
        if (destinationGate == null) {
            this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.QuickDialGUI.Message5", "{ADDRESS}", Address));
            this.getPlayer().closeInventory();
            return;
        }
        if (destinationGate.isUseGatePerms() && !this.getPlayer().hasPermission(String.valueOf(Perms.thegate_user_dial_.value()) + destinationGate.getAddress()) && !this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value())) {
            this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            return;
        }
        if (!(gate.getNetwork().equals(destinationGate.getNetwork()) || gate.getNetwork().equals(destinationGate.getSecondaryNetwork()) || gate.getSecondaryNetwork().equals(destinationGate.getNetwork()))) {
            this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message12", new String[0]));
            this.getPlayer().closeInventory();
            return;
        }
        if (destinationGate.isActive() || gate.isActive() || destinationGate.getBlockedState().equals((Object)BlockedState.block_incoming) || destinationGate.getBlockedState().equals((Object)BlockedState.locked) || gate.getBlockedState().equals((Object)BlockedState.block_outgoing) || gate.getBlockedState().equals((Object)BlockedState.locked) || destinationGate == null || destinationGate.isDialinginProssed() || gate.isDialinginProssed()) {
            this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message11", new String[0]));
            this.getPlayer().closeInventory();
            return;
        }
        if (TheGateMain.SaveLoadInterface.hasGateWithAddressInTableGates(Address) && destinationGate.isOpen()) {
            gate.Activate(Address, true, this.getPlayer());
            this.getPlayer().closeInventory();
            return;
        }
        this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message11", new String[0]));
        this.getPlayer().closeInventory();
    }
}

