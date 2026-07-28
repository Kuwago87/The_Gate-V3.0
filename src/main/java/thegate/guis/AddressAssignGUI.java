/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 */
package thegate.guis;

import com.gui.tools.guitools.GUIBase;
import com.gui.tools.guitools.InventoryManager;
import java.util.Random;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.guis.GateCrystalGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;
import thegate.math.GateMath;

public class AddressAssignGUI
extends GUIBase {
    private Location location;
    String[] list = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public AddressAssignGUI(Player p, String name, Location loc, Material m) {
        super(p, 54, name, "AddressAssignGUI");
        if (m.toString().toLowerCase().contains("slab")) {
            loc.add(new Vector(0.0, -0.5, 0.0));
        }
        this.location = loc;
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.addUIAccessPermission(Perms.thegate_owner_creategate.value(), Perms.thegate_admin_creategate.value());
    }

    public void setup() {
        int i = 9;
        while (i < 53) {
            this.setItem(i, Material.BLACK_STAINED_GLASS_PANE, " ", null);
            ++i;
        }
        this.setItem(0, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(8, Material.RED_STAINED_GLASS_PANE, " ", null);
        this.setItem(20, Globals.SymbolMaterial[0], "0", null);
        this.setItem(21, Globals.SymbolMaterial[1], "1", null);
        this.setItem(22, Globals.SymbolMaterial[2], "2", null);
        this.setItem(23, Globals.SymbolMaterial[3], "3", null);
        this.setItem(24, Globals.SymbolMaterial[4], "4", null);
        this.setItem(29, Globals.SymbolMaterial[5], "5", null);
        this.setItem(30, Globals.SymbolMaterial[6], "6", null);
        this.setItem(32, Globals.SymbolMaterial[7], "7", null);
        this.setItem(33, Globals.SymbolMaterial[8], "8", null);
        this.setItem(38, Globals.SymbolMaterial[9], "9", null);
        this.setItem(39, Globals.SymbolMaterial[10], "a", null);
        this.setItem(40, Globals.SymbolMaterial[11], "b", null);
        this.setItem(41, Globals.SymbolMaterial[12], "c", null);
        this.setItem(42, Globals.SymbolMaterial[13], "d", null);
        this.setItem(48, Globals.SymbolMaterial[14], "e", null);
        this.setItem(50, Globals.SymbolMaterial[15], "f", null);
        this.setItem(53, Material.BARRIER, ConfigManager.getString("GUIS.AddressAssignGUI.Items.Address-not-allowed", new String[0]), null);
        this.setItem(45, Material.SKELETON_SKULL, ConfigManager.getString("GUIS.AddressAssignGUI.Items.Clear", new String[0]), null);
        this.setItem(35, Material.CREEPER_SPAWN_EGG, ConfigManager.getString("GUIS.AddressAssignGUI.Items.Randomize", new String[0]), null);
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

    private GateObject CreateNewGate(Player player, String Address, Location loc, String WorldName) {
        GateObject go = new GateObject(loc, WorldName, GateMath.getPlayerDirection(player), Address, player.getUniqueId().toString(), player.getName());
        go.setUpdated(true);
        GateManager.addGate(go);
        player.closeInventory();
        player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.AddressAssignGUI.Message1", new String[0]));
        player.sendMessage(String.valueOf(ConfigManager.getString("PlayerMessages.FromGUI.AddressAssignGUI.Message2", new String[0])) + "(" + Globals.DefaultDHDMaterial.toString() + ")");
        player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.AddressAssignGUI.Message3", new String[0]));
        player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.AddressAssignGUI.Message4", new String[0]));
        TextComponent message0 = new TextComponent(ConfigManager.getString("PlayerMessages.FromGUI.AddressAssignGUI.Message5", new String[0]));
        message0.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/thegate gatename <address> <name>"));
        player.spigot().sendMessage((BaseComponent)message0);
        message0 = new TextComponent(ConfigManager.getString("PlayerMessages.FromGUI.AddressAssignGUI.Message6", new String[0]));
        message0.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/thegate gatedescription <address> <description>"));
        player.spigot().sendMessage((BaseComponent)message0);
        GateCrystalGUI gateCrystalGUI = new GateCrystalGUI(this.getPlayer(), ConfigManager.getString("GUIS.GateCrystalGUI.GUIName", new String[0]), Address);
        if (gateCrystalGUI.OpenGUI()) {
            InventoryManager.addGUI(gateCrystalGUI);
        }
        return go;
    }

    public void setupFunctions() {
        this.setGeneralFunction(x -> {
            if (x.event.getRawSlot() > 9 && x.event.getRawSlot() < 54 && this.ListHasString(x.item.getItemMeta().getDisplayName())) {
                int i = 1;
                while (i < 8) {
                    if (this.getItem(i) == null) {
                        this.setItem(i, x.item);
                        this.refreshItems();
                        break;
                    }
                    ++i;
                }
            }
            Boolean AllowCreate = false;
            String Address = "";
            int i = 1;
            while (i < 8) {
                AllowCreate = true;
                if (this.getItem(i) == null) {
                    AllowCreate = false;
                } else {
                    Address = String.valueOf(Address) + this.getItem(i).getItemMeta().getDisplayName();
                }
                ++i;
            }
            if (AllowCreate.booleanValue() && !GateManager.hasGateWithAddress(Address) && !GateManager.hasGateOnOtherServerWithAddress(Address)) {
                this.setItem(53, Material.END_CRYSTAL, ConfigManager.getString("GUIS.AddressAssignGUI.Items.CreateGate", new String[0]), null);
                this.refreshItems();
            } else {
                this.setItem(53, Material.BARRIER, ConfigManager.getString("GUIS.AddressAssignGUI.Items.Address-not-allowed", new String[0]), null);
                this.refreshItems();
            }
        });
        this.addGUIFunction(35, x -> {
            String s = "";
            int itterator = 0;
            while (itterator < 100) {
                s = "";
                int[] indices = new int[7];
                int i = 0;
                while (i < 7) {
                    int index = new Random().nextInt(15);
                    s = String.valueOf(s) + this.list[index];
                    indices[i] = index;
                    ++i;
                }
                if (!GateManager.hasGateWithAddress(s) && !GateManager.hasGateOnOtherServerWithAddress(s)) {
                    i = 0;
                    while (i < 7) {
                        this.setItem(1 + i, Globals.SymbolMaterial[indices[i]], this.list[indices[i]], null);
                        ++i;
                    }
                    break;
                }
                ++itterator;
            }
            this.refreshItems();
        }, Material.CREEPER_SPAWN_EGG);
        this.addGUIFunction(45, x -> {
            int i = 1;
            while (i < 8) {
                this.setItem(i, null);
                ++i;
            }
            this.refreshItems();
        }, Material.SKELETON_SKULL);
        this.addGUIFunction(53, x -> {
            String Address = "";
            int i = 1;
            while (i < 8) {
                if (x.event.getInventory().getItem(i) == null) {
                    return;
                }
                Address = String.valueOf(Address) + x.event.getInventory().getItem(i).getItemMeta().getDisplayName();
                ++i;
            }
            if (Address != "" && !GateManager.hasGateWithAddress(Address) && !GateManager.hasGateOnOtherServerWithAddress(Address)) {
                if (Globals.SaveFromat.equalsIgnoreCase("mysql") && TheGateMain.SaveLoadInterface.hasGateWithAddressInTableGates(Address)) {
                    x.player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateAllreadyInDatabase", "{ADDRESS}", Address));
                    x.player.closeInventory();
                    return;
                }
                GateObject obj = this.CreateNewGate(x.player, Address, this.location, this.location.getWorld().getName().toString());
                obj.addPlayerInRange(x.player);
                TheGateMain.SaveLoadInterface.SaveSingleGate(obj);
            }
        }, Material.END_CRYSTAL);
    }
}

