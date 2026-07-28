/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package thegate.guis;

import com.gui.tools.guitools.GUIBase;
import com.gui.tools.guitools.InventoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import thegate.animaitons.IrisAnimation;
import thegate.gate.BlockedState;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.guis.QuickDialGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;

public class DHD_GUI
extends GUIBase {
    public GateObject gate;
    private String currentAddress = "";
    private int index = 0;
    private String charString = "0123456789abcdef";
    private String[] list = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private char[] charlist = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private List<String> assistAddresses = new ArrayList<String>();
    private Set<GateObject> available;

    public DHD_GUI(Player p, String name, GateObject gate) {
        super(p, 54, name, "DHD_GUI");
        this.gate = gate;
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.addUIAccessPermission(Perms.thegate_user_dialgate.value(), Perms.thegate_admin_dialgate.value());
        this.additemPerms(31, Perms.thegate_user_dialgate.value(), Perms.thegate_admin_dialgate.value(), Perms.thegate_user_canceldialing.value());
        this.setGUIAccessCondition(c2 -> {
            DHD_GUI g = (DHD_GUI)c2;
            if (!(g.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value()) || this.gate.isOpen() || this.gate.getOwnerUUID().equals(g.getPlayer().getUniqueId().toString()))) {
                g.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message1", new String[0]));
                return false;
            }
            return this.gate.isOpen() || this.gate.getOwnerUUID().equals(g.getPlayer().getUniqueId().toString());
        });
    }

    public void setup() {
        this.setFilterUnaccassable(false);
        int i = 9;
        while (i < 54) {
            this.setItem(i, Material.BLACK_STAINED_GLASS_PANE, " ", null);
            ++i;
        }
        this.setItem(0, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(8, Material.RED_STAINED_GLASS_PANE, " ", null);
        if ((this.gate.getOwnerUUID().equals(this.getPlayer().getUniqueId().toString()) || this.gate.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString())) && this.getPlayer().hasPermission(Perms.thegate_owner_useiris.value()) || this.getPlayer().hasPermission(Perms.thegate_admin_useiris.value())) {
            this.setItem(13, Material.FIRE_CHARGE, ConfigManager.getString("GUIS.DHD_GUI.Items.Iris", new String[0]), null);
        }
        this.setItem(20, Globals.SymbolMaterial[0], "0", null);
        this.setItem(21, Globals.SymbolMaterial[1], "1", null);
        this.setItem(22, Globals.SymbolMaterial[2], "2", null);
        this.setItem(23, Globals.SymbolMaterial[3], "3", null);
        this.setItem(24, Globals.SymbolMaterial[4], "4", null);
        this.setItem(29, Globals.SymbolMaterial[5], "5", null);
        this.setItem(30, Globals.SymbolMaterial[6], "6", null);
        if (!this.gate.getBlockedState().equals((Object)BlockedState.block_outgoing) && !this.gate.getBlockedState().equals((Object)BlockedState.locked) && (this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value()) || this.gate.isOpen() || this.gate.hasOwningRelation(this.getPlayer()))) {
            ArrayList<String> Dile = new ArrayList<String>();
            Dile.add(ConfigManager.getString("GUIS.DHD_GUI.Items.Dial.Lore", new String[0]));
            this.setItem(31, Material.REDSTONE_BLOCK, ConfigManager.getString("GUIS.DHD_GUI.Items.Dial.Name", new String[0]), Dile);
            ArrayList<String> Clear = new ArrayList<String>();
            Clear.add(ConfigManager.getString("GUIS.DHD_GUI.Items.Clear_Dial.Lore", new String[0]));
            if (!this.gate.isDialinginProssed() && !this.gate.isActive()) {
                this.setItem(53, Material.BARRIER, ConfigManager.getString("GUIS.DHD_GUI.Items.Clear_Dial.Name", new String[0]), Clear);
            }
        }
        this.setItem(32, Globals.SymbolMaterial[7], "7", null);
        this.setItem(33, Globals.SymbolMaterial[8], "8", null);
        this.setItem(38, Globals.SymbolMaterial[9], "9", null);
        this.setItem(39, Globals.SymbolMaterial[10], "a", null);
        this.setItem(40, Globals.SymbolMaterial[11], "b", null);
        this.setItem(41, Globals.SymbolMaterial[12], "c", null);
        this.setItem(42, Globals.SymbolMaterial[13], "d", null);
        this.setItem(48, Globals.SymbolMaterial[14], "e", null);
        this.setItem(50, Globals.SymbolMaterial[15], "f", null);
        if (this.gate.isDialinginProssed() || this.gate.isActive()) {
            char[] address = this.gate.getDiled().toCharArray();
            int i2 = 1;
            while (i2 < 8) {
                int j = 0;
                while (j < this.charlist.length) {
                    if (this.charlist[j] == address[i2 - 1]) {
                        this.setItem(i2, Globals.SymbolMaterial[j], "" + this.charlist[j], null);
                    }
                    ++j;
                }
                ++i2;
            }
        }
        if (this.gate.getBlockedState().equals((Object)BlockedState.block_outgoing) || this.gate.getBlockedState().equals((Object)BlockedState.locked) || !this.gate.isOpen() && !this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value()) && !this.gate.isOwner(this.getPlayer()) && !this.gate.isCoowner(this.getPlayer())) {
            i = 1;
            while (i < 8) {
                this.setItem(i, Material.SKELETON_SKULL, ConfigManager.getString("GUIS.DHD_GUI.Items.GateLocked", new String[0]), null);
                ++i;
            }
        }
        if (Globals.AllowQuickDial && this.getPlayer().hasPermission(Perms.thegate_user_quickdial.value()) && !this.gate.getBlockedState().equals((Object)BlockedState.block_outgoing) && !this.gate.getBlockedState().equals((Object)BlockedState.locked) && (this.getPlayer().hasPermission(Perms.thegate_admin_dialgate.value()) || this.gate.isOpen() || this.gate.hasOwningRelation(this.getPlayer()))) {
            this.setItem(35, Material.ELYTRA, ConfigManager.getString("GUIS.DHD_GUI.Items.QuickDial", new String[0]), null);
        }
        if (Globals.AllowDialSuggestions && this.getPlayer().hasPermission(Perms.thegate_user_dialassist.value())) {
            this.setAssistPannel();
        }
        this.available = GateManager.getGatesAsSet();
        this.available.addAll(GateManager.getGatesOnOtherServer());
    }

    public boolean ListHasString(String s) {
        int i = 0;
        while (i < this.list.length) {
            if (s.equals(this.list[i])) {
                return true;
            }
            ++i;
        }
        return false;
    }

    private void updateAssistList(List<String> addresses, int index) {
        if (addresses == null || addresses.isEmpty()) {
            this.setAssistPannel();
            return;
        }
        if (addresses.size() > 5) {
            this.setItem(9, Material.ARROW, "Up", null);
            this.setItem(45, Material.ARROW, "Down", null);
            int i = 0;
            while (i < 3) {
                if (i + 3 * index < addresses.size()) {
                    this.setItem(18 + i * 9, Globals.SymbolMaterial[this.charString.indexOf(addresses.get(i).charAt(0))], addresses.get(i + 3 * index), null);
                } else {
                    this.setItem(18 + i * 9, Material.WHITE_STAINED_GLASS_PANE, " ", null);
                }
                ++i;
            }
        } else {
            this.setAssistPannel();
            int i = 0;
            while (i < addresses.size()) {
                this.setItem(9 + i * 9, Globals.SymbolMaterial[this.charString.indexOf(addresses.get(i).charAt(0))], addresses.get(i), null);
                ++i;
            }
        }
    }

    private void setAssistPannel() {
        this.setItem(9, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(18, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(27, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(36, Material.WHITE_STAINED_GLASS_PANE, " ", null);
        this.setItem(45, Material.WHITE_STAINED_GLASS_PANE, " ", null);
    }

    public void setupFunctions() {
        this.setGeneralFunction(x -> {
            if (x.index > 9 && x.index < 54 && this.ListHasString(x.item.getItemMeta().getDisplayName())) {
                int i = 1;
                while (i < 8) {
                    if (this.getItem(i) == null) {
                        this.setItem(i, x.item);
                        this.currentAddress = String.valueOf(this.currentAddress) + x.item.getItemMeta().getDisplayName();
                        break;
                    }
                    ++i;
                }
                if (Globals.AllowDialSuggestions && this.getPlayer().hasPermission(Perms.thegate_user_dialassist.value())) {
                    this.assistAddresses = this.available.stream().map(gate -> gate.getAddress()).filter(address -> address.contains(this.currentAddress) && !address.equalsIgnoreCase(this.gate.getAddress())).sorted().collect(Collectors.toList());
                    this.updateAssistList(this.assistAddresses, 0);
                }
                this.refreshItems();
            }
        });
        this.addGUIFunction(9, x -> {
            if (this.index > 0) {
                --this.index;
                this.updateAssistList(this.assistAddresses, this.index);
                this.refreshItems();
            }
        }, Material.ARROW);
        this.addGUIFunction(45, x -> {
            int listSize = this.assistAddresses.size();
            if (listSize <= 5) {
                return;
            }
            int maxRange = listSize / 3 + (listSize % 3 == 0 ? -1 : 0);
            if (this.index < maxRange) {
                ++this.index;
                this.updateAssistList(this.assistAddresses, this.index);
                this.refreshItems();
            }
        }, Material.ARROW);
        int i = 0;
        while (i < 5) {
            Material[] materialArray = Globals.SymbolMaterial;
            int n = Globals.SymbolMaterial.length;
            int n2 = 0;
            while (n2 < n) {
                Material m = materialArray[n2];
                this.addGUIFunction(9 + i * 9, x -> {
                    char[] address = x.item.getItemMeta().getDisplayName().toCharArray();
                    int a = 0;
                    while (a < 7) {
                        this.setItem(1 + a, Globals.SymbolMaterial[this.charString.indexOf(address[a])], String.valueOf(address[a]), null);
                        ++a;
                    }
                    this.refreshItems();
                }, m);
                ++n2;
            }
            ++i;
        }
        this.addGUIFunction(35, x -> {
            x.player.closeInventory();
            QuickDialGUI quickDial = new QuickDialGUI(x.player, ConfigManager.getString("GUIS.DHD_GUI.GUIName", "{ADDRESS}", this.gate.getAddress()), this.gate, this);
            if (quickDial.OpenGUI()) {
                InventoryManager.addGUI(quickDial);
            }
        }, Material.ELYTRA);
        this.addGUIFunction(13, x -> {
            x.player.closeInventory();
            if (this.gate.irisAnimaiton == null) {
                this.gate.setIrisAnimaiton(new IrisAnimation(this.gate, this.gate.isIrisClosed()));
                if (!Globals.DoIrisAnimaiton) {
                    this.gate.setIrisAnimaiton(null);
                }
            }
        }, Material.FIRE_CHARGE);
        this.addGUIFunction(53, x -> {
            int j = 1;
            while (j < 8) {
                this.setItem(j, null);
                ++j;
            }
            if (Globals.AllowDialSuggestions && this.getPlayer().hasPermission(Perms.thegate_user_dialassist.value())) {
                this.currentAddress = "";
                this.assistAddresses = null;
                this.updateAssistList(null, 0);
                this.index = 0;
                this.refreshItems();
            }
        }, Material.BARRIER);
        this.addGUIFunction(31, x -> {
            Player player = this.getPlayer();
            String Address = "";
            int j = 1;
            while (j < 8) {
                if (this.getItem(j) == null) {
                    return;
                }
                Address = String.valueOf(Address) + this.getItem(j).getItemMeta().getDisplayName();
                ++j;
            }
            this.dialGate(Address, player);
        }, Material.REDSTONE_BLOCK);
    }

    private void dialGate(String Address, Player player) {
        if (Address.equals(this.gate.getAddress())) {
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message6", new String[0]));
            player.closeInventory();
            return;
        }
        GateObject destinationGate = GateManager.getGateWithAddress(Address);
        if (Globals.UseBungee && destinationGate == null && GateManager.hasGateOnOtherServerWithAddress(Address)) {
            this.bungeeDial(player, this.gate, destinationGate, Address);
            return;
        }
        if (destinationGate == null) {
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message7", "{ADDRESS}", Address));
            player.closeInventory();
            return;
        }
        if (player.hasPermission(Perms.thegate_user_canceldialing.value()) && (this.gate.isActive() || this.gate.isDialingout()) && this.gate.isDialingout()) {
            this.gate.Deactivate();
            destinationGate.Deactivate();
            player.closeInventory();
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message8", new String[0]));
            return;
        }
        if (player.hasPermission(Perms.thegate_user_canceldialing.value()) && (this.gate.isActive() || this.gate.isDialingout()) && !this.gate.isDialingout()) {
            player.closeInventory();
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message9", new String[0]));
            return;
        }
        if (!player.hasPermission(Perms.thegate_user_canceldialing.value()) && (this.gate.isActive() || this.gate.isDialingout()) && this.gate.isDialingout()) {
            player.closeInventory();
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message9", new String[0]));
            return;
        }
        if (!player.hasPermission(Perms.thegate_user_canceldialing.value()) && (this.gate.isActive() || this.gate.isDialingout()) && !this.gate.isDialingout()) {
            player.closeInventory();
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message9", new String[0]));
            return;
        }
        if (!(player.hasPermission(Perms.thegate_admin_dialgate.value()) || destinationGate.isOpen() || destinationGate.hasOwningRelation(player))) {
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message1", new String[0]));
            player.closeInventory();
            return;
        }
        if (this.gate.hasSameNetwork(destinationGate)) {
            if (this.gate.canDial(destinationGate, player)) {
                if (Globals.DoDialing) {
                    this.gate.dialGate(destinationGate, player);
                    player.closeInventory();
                    return;
                }
                destinationGate.Activate(this.gate.getAddress(), false, player);
                this.gate.Activate(destinationGate.getAddress(), true, player);
                player.closeInventory();
                return;
            }
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message11", new String[0]));
            player.closeInventory();
            return;
        }
        player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message12", new String[0]));
        player.closeInventory();
    }

    private void bungeeDial(Player player, GateObject gate, GateObject destinationGate, String Address) {
        destinationGate = GateManager.getGateOnOtherServerWithAddress(Address);
        if (destinationGate == null) {
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message7", "{ADDRESS}", Address));
            player.closeInventory();
            return;
        }
        if (player.hasPermission(Perms.thegate_user_canceldialing.value()) && (gate.isDialinginProssed() || gate.isActive()) && gate.isDialingout()) {
            gate.Deactivate();
            player.closeInventory();
            return;
        }
        if (!gate.canDial(destinationGate, player)) {
            player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.DHD_GUI.Message11", new String[0]));
            player.closeInventory();
            return;
        }
        gate.StartDialingOutSequenceSingleGate((Plugin)TheGateMain.theGateMain, TheGateMain.theGateMain, Address, player);
        gate.setDialinginProssed(true);
        player.closeInventory();
    }
}

