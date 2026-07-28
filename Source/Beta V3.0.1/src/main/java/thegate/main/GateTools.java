/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.util.Vector
 */
package thegate.main;

import com.gui.tools.guitools.InventoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.guis.AbydosGUI;
import thegate.guis.AddressAssignGUI;
import thegate.guis.DHD_GUI;
import thegate.guis.EditAllGatesGUI;
import thegate.guis.EditGateGUI;
import thegate.guis.GateCrystalGUI;
import thegate.guis.IrisIDCGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;
import thegate.math.GateMath;

public class GateTools
implements CommandExecutor,
Listener {
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (player.hasPermission(Perms.thegate_user_gatetools.value())) {
                this.GATETOOLSGUI(player);
            } else {
                player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
            }
        } else {
            TheGateMain.theGateMain.getLogger().log(Level.INFO, "Command only for players");
        }
        return true;
    }

    private void GATETOOLSGUI(Player player) {
        Inventory invent = Bukkit.createInventory(null, (int)9, (String)ConfigManager.getString("GUIS.GateTools.GUIName", new String[0]));
        if (player.hasPermission(Perms.thegate_admin_creategate.value()) || player.hasPermission(Perms.thegate_owner_creategate.value())) {
            ItemStack GateCreator = new ItemStack(Globals.DefaultGateCreationTool);
            ItemMeta GateCreatormeta = GateCreator.getItemMeta();
            GateCreatormeta.setDisplayName(ConfigManager.getString("GUIS.GateTools.Items.CreateTool.Name", new String[0]));
            ArrayList<String> itemlore = new ArrayList<String>();
            itemlore.add(ConfigManager.getString("GUIS.GateTools.Items.CreateTool.Lore", new String[0]));
            GateCreatormeta.setLore(itemlore);
            GateCreator.setItemMeta(GateCreatormeta);
            invent.setItem(2, GateCreator);
        }
        if (player.hasPermission(Perms.thegate_admin_editgate.value()) || player.hasPermission(Perms.thegate_admin_editallgates.value()) || player.hasPermission(Perms.thegate_owner_editgate.value())) {
            ItemStack GateEditor = new ItemStack(Globals.DefaultGateEditTool);
            ItemMeta GateEditormeta = GateEditor.getItemMeta();
            GateEditormeta.setDisplayName(ConfigManager.getString("GUIS.GateTools.Items.EditTool.Name", new String[0]));
            ArrayList<String> GateEditorlore = new ArrayList<String>();
            GateEditorlore.add(ConfigManager.getString("GUIS.GateTools.Items.EditTool.Lore", new String[0]));
            GateEditormeta.setLore(GateEditorlore);
            GateEditor.setItemMeta(GateEditormeta);
            invent.setItem(3, GateEditor);
        }
        if (player.hasPermission(Perms.thegate_admin_abydoscartouche.value()) || player.hasPermission(Perms.thegate_user_abydoscartouche.value())) {
            ItemStack abydoscartouche = new ItemStack(Globals.DefaultAbydosCartouche);
            ItemMeta abydoscartouchemeta = abydoscartouche.getItemMeta();
            abydoscartouchemeta.setDisplayName(ConfigManager.getString("GUIS.GateTools.Items.AbydosCartouche.Name", new String[0]));
            ArrayList<String> abydoscartouchelore = new ArrayList<String>();
            abydoscartouchelore.add(ConfigManager.getString("GUIS.GateTools.Items.AbydosCartouche.Lore", new String[0]));
            abydoscartouchemeta.setLore(abydoscartouchelore);
            abydoscartouche.setItemMeta(abydoscartouchemeta);
            invent.setItem(4, abydoscartouche);
        }
        if (player.hasPermission(Perms.thegate_user_idctransmitter.value())) {
            ItemStack IDCTransmitter = new ItemStack(Globals.DefaultIDCTransmitter);
            ItemMeta IDCTransmittermeta = IDCTransmitter.getItemMeta();
            IDCTransmittermeta.setDisplayName(ConfigManager.getString("GUIS.GateTools.Items.IDCTransmitter.Name", new String[0]));
            ArrayList<String> IDCTransmitterLore = new ArrayList<String>();
            IDCTransmitterLore.add(ConfigManager.getString("GUIS.GateTools.Items.IDCTransmitter.Lore", new String[0]));
            IDCTransmittermeta.setLore(IDCTransmitterLore);
            IDCTransmitter.setItemMeta(IDCTransmittermeta);
            invent.setItem(5, IDCTransmitter);
        }
        if (player.hasPermission(Perms.thegate_user_gatetools.value())) {
            player.openInventory(invent);
        } else {
            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack i;
        block40: {
            Player player;
            block41: {
                List itemlore;
                ItemStack item;
                ItemMeta meta;
                block39: {
                    block38: {
                        player = e.getPlayer();
                        if (!e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) || !e.getMaterial().equals((Object)Globals.DefaultGateCreationTool)) break block38;
                        if (e.getItem().hasItemMeta() && (meta = (item = e.getItem()).getItemMeta()).getDisplayName().contains(ConfigManager.getString("GUIS.GateTools.Items.CreateTool.Name", new String[0]))) {
                            if (GateManager.GateInRadius(e.getClickedBlock().getLocation())) {
                                player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateNear", new String[0]));
                                return;
                            }
                            if (meta.hasLore() && ((String)(itemlore = meta.getLore()).get(0)).equals(ConfigManager.getString("GUIS.GateTools.Items.CreateTool.Lore", new String[0]))) {
                                if (player.hasPermission(Perms.thegate_admin_creategate.value())) {
                                    if (player.getOpenInventory().getTitle().contains(ConfigManager.getString("GUIS.AddressAssignGUI.GUIName", new String[0]))) return;
                                    AddressAssignGUI addressAssignGUI = new AddressAssignGUI(e.getPlayer(), ConfigManager.getString("GUIS.AddressAssignGUI.GUIName", new String[0]), e.getClickedBlock().getLocation(), e.getClickedBlock().getType());
                                    if (!addressAssignGUI.OpenGUI()) return;
                                    InventoryManager.addGUI(addressAssignGUI);
                                    return;
                                }
                                if (player.hasPermission(Perms.thegate_owner_creategate.value())) {
                                    if (!player.getOpenInventory().getTitle().contains(ConfigManager.getString("GUIS.AddressAssignGUI.GUIName", new String[0])) && GateManager.getPlayerGateAmmount(player) < Globals.PlayerGateAmmount) {
                                        AddressAssignGUI addressAssignGUI = new AddressAssignGUI(e.getPlayer(), ConfigManager.getString("GUIS.AddressAssignGUI.GUIName", new String[0]), e.getClickedBlock().getLocation(), e.getClickedBlock().getType());
                                        if (!addressAssignGUI.OpenGUI()) return;
                                        InventoryManager.addGUI(addressAssignGUI);
                                        return;
                                    }
                                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateAmmountCap", new String[0]));
                                    return;
                                }
                                player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                            }
                        }
                        break block39;
                    }
                    if (e.getAction().equals((Object)Action.RIGHT_CLICK_AIR) && e.getMaterial().equals((Object)Globals.DefaultGateCreationTool) && e.getItem().hasItemMeta() && (meta = (item = e.getItem()).getItemMeta()).getDisplayName().contains(ConfigManager.getString("GUIS.GateTools.Items.CreateTool.Name", new String[0]))) {
                        GateObject obj = GateManager.getClosestGateTo(player.getLocation());
                        if (obj == null) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoGatesNearBy", new String[0]));
                            return;
                        }
                        boolean isCoowner = obj.isCoowner(player);
                        if (player.hasPermission(Perms.thegate_admin_creategate.value()) || player.hasPermission(Perms.thegate_owner_creategate.value()) || player.hasPermission(Perms.thegate_admin_editgate.value())) {
                            if (obj.getOwnerUUID().equals(player.getUniqueId().toString()) || player.hasPermission(Perms.thegate_admin_editgate.value()) || isCoowner && player.hasPermission(Perms.thegate_owner_creategate.value())) {
                                if (!(GateMath.getDistance(obj.getGate().getX(), obj.getGate().getY(), obj.getGate().getZ(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()) <= 10.0)) {
                                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoGatesNearBy", new String[0]));
                                    return;
                                }
                                GateCrystalGUI gcg = new GateCrystalGUI(player, ConfigManager.getString("GUIS.GateCrystalGUI.GUIName", new String[0]), obj.getAddress());
                                if (gcg.OpenGUI()) {
                                    InventoryManager.addGUI(gcg);
                                }
                            } else {
                                player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoOwnership", new String[0]));
                            }
                        }
                    }
                }
                if (e.getAction().equals((Object)Action.RIGHT_CLICK_AIR) && e.getMaterial().equals((Object)Globals.DefaultGateEditTool) && e.getItem().hasItemMeta() && (meta = (item = e.getItem()).getItemMeta()).getDisplayName().contains(ConfigManager.getString("GUIS.GateTools.Items.EditTool.Name", new String[0])) && meta.hasLore() && ((String)(itemlore = meta.getLore()).get(0)).equals(ConfigManager.getString("GUIS.GateTools.Items.EditTool.Lore", new String[0]))) {
                    if (player.hasPermission(Perms.thegate_admin_editallgates.value())) {
                        EditAllGatesGUI editAllGates = new EditAllGatesGUI(player, ConfigManager.getString("GUIS.EditAllGatesGUI.GUIName", new String[0]));
                        if (editAllGates.OpenGUI()) {
                            InventoryManager.addGUI(editAllGates);
                        }
                    } else {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                    }
                }
                if ((e.getAction().equals((Object)Action.RIGHT_CLICK_AIR) || e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) && e.getMaterial().equals((Object)Globals.DefaultAbydosCartouche) && e.getItem().hasItemMeta() && (meta = (item = e.getItem()).getItemMeta()).getDisplayName().contains(ConfigManager.getString("GUIS.GateTools.Items.AbydosCartouche.Name", new String[0])) && meta.hasLore() && ((String)(itemlore = meta.getLore()).get(0)).equals(ConfigManager.getString("GUIS.GateTools.Items.AbydosCartouche.Lore", new String[0]))) {
                    if (player.hasPermission(Perms.thegate_admin_abydoscartouche.value()) || player.hasPermission(Perms.thegate_user_abydoscartouche.value())) {
                        AbydosGUI abydosGUI = new AbydosGUI(e.getPlayer(), ConfigManager.getString("GUIS.AbydosGUI.GUIName", new String[0]));
                        if (abydosGUI.OpenGUI()) {
                            InventoryManager.addGUI(abydosGUI);
                        }
                    } else {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                    }
                }
                if (e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && e.getMaterial().equals((Object)Globals.DefaultGateCrystal) && e.getItem().hasItemMeta() && (meta = (item = e.getItem()).getItemMeta()).getDisplayName().equals(ConfigManager.getString("GUIS.GateCrystalGUI.Items.DHDCrystal.Name", new String[0])) && meta.hasLore() && ((String)(itemlore = meta.getLore()).get(0)).equals(ConfigManager.getString("GUIS.GateCrystalGUI.Items.DHDCrystal.Lore", new String[0]))) {
                    if (player.hasPermission(Perms.thegate_admin_creategate.value()) || player.hasPermission(Perms.thegate_owner_creategate.value())) {
                        if (GateManager.hasGateWithAddress((String)itemlore.get(1))) {
                            this.CreateDHD(e.getClickedBlock().getLocation(), (String)itemlore.get(1), player);
                        }
                    } else {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                    }
                }
                if (!e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) || !e.getClickedBlock().getType().equals((Object)Globals.DefaultDHDMaterial) || !GateManager.hasDHDatLocation(e.getClickedBlock().getLocation())) break block40;
                e.setCancelled(true);
                e.setCancelled(true);
                if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) break block41;
                item = e.getPlayer().getInventory().getItemInMainHand();
                meta = item.getItemMeta();
                if (item.getType().equals((Object)Globals.DefaultGateEditTool) && meta.getDisplayName().contains(ConfigManager.getString("GUIS.GateTools.Items.EditTool.Name", new String[0]))) {
                    GateObject gate = GateManager.getGateByDHD(e.getClickedBlock().getLocation());
                    if (gate == null) {
                        return;
                    }
                    if (player.hasPermission(Perms.thegate_admin_editgate.value()) || player.hasPermission(Perms.thegate_owner_editgate.value()) || gate.getCoOwner().containsKey(player.getUniqueId().toString()) && player.hasPermission(Perms.thegate_owner_editgate.value())) {
                        if (!player.hasPermission(Perms.thegate_admin_editallgates.value()) && player.hasPermission(Perms.thegate_owner_editgate.value()) && !gate.getOwnerUUID().equals(player.getUniqueId().toString()) && !gate.getCoOwner().containsKey(player.getUniqueId().toString())) {
                            player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoOwnership", new String[0]));
                            return;
                        }
                        EditGateGUI egg = new EditGateGUI(player, ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", gate.getAddress()), gate.getAddress(), gate);
                        if (egg.OpenGUI()) {
                            InventoryManager.addGUI(egg);
                        }
                        break block40;
                    } else {
                        player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                    }
                }
                break block40;
            }
            if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                GateObject gate = GateManager.getGateByDHD(e.getClickedBlock().getLocation());
                if (player.hasPermission(Perms.thegate_user_dialgate.value()) || player.hasPermission(Perms.thegate_admin_dialgate.value())) {
                    DHD_GUI dhd;
                    if (gate != null && !player.getOpenInventory().getTitle().contains(ConfigManager.getString("GUIS.DHD_GUI.GUIName", new String[0])) && (dhd = new DHD_GUI(e.getPlayer(), ConfigManager.getString("GUIS.DHD_GUI.GUIName", "{ADDRESS}", gate.getAddress()), gate)).OpenGUI()) {
                        InventoryManager.addGUI(dhd);
                    }
                } else {
                    player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
                }
            }
        }
        if (!e.getAction().equals((Object)Action.RIGHT_CLICK_AIR)) {
            if (!e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) return;
        }
        if ((i = e.getItem()) == null) return;
        if (!i.getType().equals((Object)Globals.DefaultIDCTransmitter)) return;
        if (!i.hasItemMeta()) return;
        if (!i.getItemMeta().getDisplayName().equals(ConfigManager.getString("GUIS.GateTools.Items.IDCTransmitter.Name", new String[0]))) return;
        if (!i.getItemMeta().hasLore()) return;
        if (!((String)i.getItemMeta().getLore().get(0)).equals(ConfigManager.getString("GUIS.GateTools.Items.IDCTransmitter.Lore", new String[0]))) return;
        GateObject obj = GateManager.getClosestGateTo(e.getPlayer().getLocation());
        e.setCancelled(true);
        if (obj == null) {
            return;
        }
        Vector v = obj.getGate().toVector().add(e.getPlayer().getLocation().toVector().multiply(-1));
        if (!(GateMath.getVectorLength(v) < 30.0)) return;
        if (!obj.isActive()) return;
        if (!obj.isDialingout()) return;
        GateObject connected = GateManager.getGateWithAddress(obj.getDiled());
        if (connected == null) {
            return;
        }
        IrisIDCGUI idcGUI = new IrisIDCGUI(e.getPlayer(), "IDCGUI", "IDCGUI", connected);
        if (!idcGUI.OpenGUI()) return;
        InventoryManager.addGUI(idcGUI);
    }

    public void CreateDHD(Location loc, String Address, Player player) {
        if (loc.getBlock().getType().equals((Object)Globals.DefaultDHDMaterial)) {
            if (GateManager.hasGateWithAddress(Address)) {
                GateManager.getGateWithAddress(Address).setDHD(loc);
                TheGateMain.SaveLoadInterface.updateGate(GateManager.getGateWithAddress(Address));
                player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.LinkGate", "{ADDRESS}", Address));
                player.getInventory().setItemInMainHand(null);
            } else {
                player.sendMessage(ConfigManager.getString("PlayerMessages.GlobalText.GateNotFound", "{ADDRESS}", Address));
            }
        } else {
            player.sendMessage(String.valueOf(ConfigManager.getString("PlayerMessages.GlobalText.WrongDHDMaterial", new String[0])) + Globals.DefaultDHDMaterial.toString());
        }
    }
}

