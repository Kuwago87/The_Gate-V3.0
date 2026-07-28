/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.util.Vector
 */
package thegate.guis;

import com.gui.tools.guitools.GUIBase;
import com.gui.tools.guitools.InventoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import thegate.gate.BlockedState;
import thegate.gate.CreateGate;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.guis.CoOwnerAddGUI;
import thegate.guis.CoOwnerListGUI;
import thegate.guis.IrisEditGUI;
import thegate.guis.NetworkAssignGUI;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;
import thegate.math.GateMath;

public class EditGateGUI
extends GUIBase {
    public String address;
    public GateObject GATE;

    public EditGateGUI(Player p, String name, String Address, GateObject gate) {
        super(p, 36, name, "EditGateGUI");
        this.address = Address;
        this.GATE = gate;
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.setDefaultErrorMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
        this.setFilterUnaccassable(false);
        this.addUIAccessPermission(Perms.thegate_owner_editgate.value(), Perms.thegate_admin_editgate.value());
        this.additemPerms(0, Perms.thegate_owner_lockgate.value(), Perms.thegate_admin_lockgate.value());
        this.additemPerms(1, Perms.thegate_owner_setprivate.value(), Perms.thegate_admin_setprivate.value());
        this.additemPerms(3, Perms.thegate_owner_repairgate.value(), Perms.thegate_admin_repairgate.value());
        this.additemPerms(4, Perms.thegate_owner_repairgate.value(), Perms.thegate_admin_repairgate.value());
        this.additemPerms(5, Perms.thegate_owner_repairgate.value(), Perms.thegate_admin_repairgate.value());
        this.additemPerms(8, Perms.thegate_owner_removegate.value(), Perms.thegate_admin_removegate.value());
        this.additemPerms(13, Perms.thegate_owner_tptogate.value(), Perms.thegate_admin_tptogate.value());
        this.additemPerms(24, Perms.thegate_owner_editnetwork.value(), Perms.thegate_admin_editnetwork.value());
        this.additemPerms(25, Perms.thegate_owner_editnetwork.value(), Perms.thegate_admin_editnetwork.value());
        this.additemPerms(17, Perms.thegate_owner_creategate.value(), Perms.thegate_admin_creategate.value());
        int i = 27;
        while (i < 36) {
            this.additemPerms(i, Perms.thegate_owner_editmaterial.value(), Perms.thegate_admin_editmaterial.value());
            ++i;
        }
        this.setGUIAccessCondition(c2 -> this.GATE.getOwnerUUID().equals(c2.getPlayer().getUniqueId().toString()) || !c2.getPlayer().hasPermission(Perms.thegate_admin_editgate.value()));
    }

    public void setup() {
        boolean isCoowner = this.GATE.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString());
        int i = 0;
        while (i < 36) {
            this.setItem(i, Material.BLACK_STAINED_GLASS_PANE, " ", null);
            ++i;
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_lockgate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_lockgate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_lockgate.value())) {
            this.setItem(0, this.GATE.getBlockedState().getMaterial(), this.GATE.getBlockedState().getText(), null);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_repairgate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_repairgate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_repairgate.value())) {
            this.setItem(3, Material.GOLDEN_PICKAXE, ConfigManager.getString("GUIS.EditGateGUI.Items.Repair", new String[0]), null);
            ArrayList<String> RemoveHorizon = new ArrayList<String>();
            RemoveHorizon.add(ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Event_Horizon.Lore1", new String[0]));
            RemoveHorizon.add(ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Event_Horizon.Lore2", new String[0]));
            this.setItem(4, Material.ENDER_PEARL, ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Event_Horizon.Name", new String[0]), RemoveHorizon);
            ArrayList<String> RemoveRing = new ArrayList<String>();
            RemoveRing.add(ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Ring.Lore", new String[0]));
            this.setItem(5, Material.LAVA_BUCKET, ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Ring.Name", new String[0]), RemoveRing);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_creategate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_creategate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_creategate.value())) {
            ArrayList<String> RemoveDHDLore = new ArrayList<String>();
            RemoveDHDLore.add(ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_DHD.Lore", new String[0]));
            this.setItem(17, Globals.DefaultDHDMaterial, ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_DHD.Name", new String[0]), RemoveDHDLore);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_removegate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_removegate.value())) {
            ArrayList<String> RemoveGateLore = new ArrayList<String>();
            RemoveGateLore.add(ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Gate.Lore1", new String[0]));
            RemoveGateLore.add(ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Gate.Lore2", new String[0]));
            this.setItem(8, Material.SKELETON_SKULL, ConfigManager.getString("GUIS.EditGateGUI.Items.Remove_Gate.Name", new String[0]), RemoveGateLore);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_editgateuseperms.value()) && this.GATE.hasOwningRelation(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_editgateuseperms.value())) {
            List<String> Lore = ConfigManager.getStringList("GUIS.EditGateGUI.Items.UseGatePerms.Lore", "{ADDRESS}", this.GATE.getAddress());
            this.setItem(9, this.GATE.isUseGatePerms() ? Material.GREEN_STAINED_GLASS : Material.RED_STAINED_GLASS, ConfigManager.getString("GUIS.EditGateGUI.Items.UseGatePerms.Name", "{VALUE}", String.valueOf(this.GATE.isUseGatePerms())), Lore);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_tptogate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_tptogate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_tptogate.value())) {
            this.setItem(13, Material.ELYTRA, ConfigManager.getString("GUIS.EditGateGUI.Items.Teleport_to_Gate", new String[0]), null);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_setprivate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_setprivate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_setprivate.value())) {
            if (this.GATE.isOpen()) {
                this.setItem(1, Material.CHEST, ConfigManager.getString("GUIS.EditGateGUI.Items.Open", new String[0]), null);
            } else if (!this.GATE.isOpen()) {
                this.setItem(1, Material.ENDER_CHEST, ConfigManager.getString("GUIS.EditGateGUI.Items.Private", new String[0]), null);
            }
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_editnetwork.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_editnetwork.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_editnetwork.value())) {
            this.setItem(24, Material.END_CRYSTAL, "\u00a76" + this.GATE.getNetwork(), null);
            if (this.GATE.getNetwork().equals(Globals.Networks.get(0)) && this.GATE.getSecondaryNetwork().equals("null")) {
                this.setItem(25, Material.WHITE_STAINED_GLASS_PANE, ConfigManager.getString("GUIS.EditGateGUI.Items.Secondary_Network", new String[0]), null);
            } else if (!this.GATE.getNetwork().equals(Globals.Networks.get(0))) {
                this.setItem(25, Material.RED_STAINED_GLASS_PANE, ConfigManager.getString("GUIS.EditGateGUI.Items.Secondary_Network", new String[0]), null);
            } else if (this.GATE.getNetwork().equals(Globals.Networks.get(0)) && !this.GATE.getSecondaryNetwork().equals("null")) {
                this.setItem(25, Material.END_CRYSTAL, "\u00a76" + this.GATE.getSecondaryNetwork(), null);
            }
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_editmaterial.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_editmaterial.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_editmaterial.value())) {
            this.setItem(27, Material.OAK_SIGN, ConfigManager.getString("GUIS.EditGateGUI.Items.Gate_Material", new String[0]), null);
            this.setItem(28, Material.ENDER_PEARL, ConfigManager.getString("GUIS.EditGateGUI.Items.Reset_Material", new String[0]), null);
            this.setItem(35, Material.REDSTONE_TORCH, ConfigManager.getString("GUIS.EditGateGUI.Items.Set_Gate_Material", new String[0]), null);
            this.setItem(29, this.GATE.getRingMaterial(), ConfigManager.getString("GUIS.EditGateGUI.Items.Ring_Material", new String[0]), null);
            this.setItem(30, this.GATE.getChevron_botMaterial(), ConfigManager.getString("GUIS.EditGateGUI.Items.Chevron_tip", new String[0]), null);
            this.setItem(31, this.GATE.getChevrons_frameMaterial(), ConfigManager.getString("GUIS.EditGateGUI.Items.Chevron_frame", new String[0]), null);
            this.setItem(32, this.GATE.getChevron_lightMaterial(), ConfigManager.getString("GUIS.EditGateGUI.Items.Chevron_Light_off", new String[0]), null);
            this.setItem(33, this.GATE.getChevron_lightMaterial_on(), ConfigManager.getString("GUIS.EditGateGUI.Items.Chevron_Light_on", new String[0]), null);
            this.setItem(34, this.GATE.getHorizonMaterial(), ConfigManager.getString("GUIS.EditGateGUI.Items.Horizon_Material", new String[0]), null);
        }
        if (!this.GATE.getOwnerUUID().equals("null")) {
            ItemStack item;
            Player p = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(this.GATE.getOwnerUUID()));
            if (p != null) {
                item = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta)item.getItemMeta();
                meta.setOwningPlayer((OfflinePlayer)p);
                meta.setDisplayName(ConfigManager.getString("GUIS.EditGateGUI.Items.GateOwner.Name", "{OWNER_NAME}", this.GATE.getOwnerName()));
                item.setItemMeta((ItemMeta)meta);
                this.setItem(18, item);
            } else {
                item = new ItemStack(Material.SKELETON_SKULL);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ConfigManager.getString("GUIS.EditGateGUI.Items.GateOwner.Name", "{OWNER_NAME}", this.GATE.getOwnerName()));
                item.setItemMeta(meta);
                this.setItem(18, item);
            }
        } else {
            ItemStack item = new ItemStack(Material.SKELETON_SKULL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ConfigManager.getString("GUIS.EditGateGUI.Items.GateOwner.Name", "{OWNER_NAME}", this.GATE.getOwnerName()));
            item.setItemMeta(meta);
            this.setItem(18, item);
        }
        if (this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_editcoowner.value())) {
            this.setItem(19, Material.PLAYER_HEAD, ConfigManager.getString("GUIS.EditGateGUI.Items.ListCoOwner.Name", new String[0]), null);
        }
        if (this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_editcoowner.value())) {
            this.setItem(20, Material.PLAYER_HEAD, ConfigManager.getString("GUIS.EditGateGUI.Items.AddCoOwner.Name", new String[0]), null);
        }
        this.setItem(22, Material.FIRE_CHARGE, ConfigManager.getString("GUIS.EditGateGUI.Items.IrisSettings.Name", new String[0]), null);
    }

    public void setupFunctions() {
        boolean isCoowner = this.GATE.getCoOwner().keySet().contains(this.getPlayer().getUniqueId().toString());
        BlockedState[] blockedStateArray = BlockedState.values();
        int n = blockedStateArray.length;
        int n2 = 0;
        while (n2 < n) {
            BlockedState b = blockedStateArray[n2];
            this.addGUIFunction(0, x -> {
                this.GATE.setBlockedState(BlockedState.values()[(this.GATE.getBlockedState().ordinal() + 1) % BlockedState.values().length]);
                this.GATE.setUpdated(true);
                this.setItem(0, this.GATE.getBlockedState().getMaterial(), this.GATE.getBlockedState().getText(), null);
                this.refreshItems();
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message2", "{ADDRESS}", this.GATE.getAddress(), "{STATE}", this.GATE.getBlockedState().getText()));
            }, b.getMaterial());
            ++n2;
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_repairgate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_repairgate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_repairgate.value())) {
            this.addGUIFunction(3, x -> {
                this.getPlayer().closeInventory();
                this.GATE.Repair();
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message4", "{ADDRESS}", this.GATE.getAddress()));
            }, Material.GOLDEN_PICKAXE);
            this.addGUIFunction(4, x -> {
                if (this.GATE.getDiled() != "" && GateManager.hasGateWithAddress(this.GATE.getDiled())) {
                    GateObject otherGate = GateManager.getGateWithAddress(this.GATE.getDiled());
                    otherGate.Deactivate();
                }
                this.GATE.Deactivate();
                this.getPlayer().closeInventory();
            }, Material.ENDER_PEARL);
            this.addGUIFunction(5, x -> {
                this.GATE.Vanish(new Player[0]);
                if (Globals.CreateBarrier) {
                    CreateGate.RemoveBarrier(this.GATE.getGate(), this.GATE.getFacing());
                }
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message5", "{ADDRESS}", this.GATE.getAddress()));
                this.getPlayer().closeInventory();
            }, Material.LAVA_BUCKET);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_removegate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_removegate.value())) {
            this.addGUIFunction(8, x -> {
                this.GATE.Vanish(new Player[0]);
                if (Globals.CreateBarrier) {
                    CreateGate.RemoveBarrier(this.GATE.getGate(), this.GATE.getFacing());
                }
                TheGateMain.SaveLoadInterface.DeleateElementFromDatabase(this.GATE);
                GateManager.RemoveGate(this.GATE);
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message6", "{ADDRESS}", this.GATE.getAddress()));
                this.getPlayer().closeInventory();
            }, Material.SKELETON_SKULL);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_editgateuseperms.value()) && this.GATE.hasOwningRelation(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_editgateuseperms.value())) {
            this.addGUIFunction(9, x -> {
                this.GATE.setUseGatePerms(false);
                this.GATE.setUpdated(true);
                ItemStack item = this.getItem(9);
                item.setType(Material.RED_STAINED_GLASS);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ConfigManager.getString("GUIS.EditGateGUI.Items.UseGatePerms.Name", "{VALUE}", String.valueOf(this.GATE.isUseGatePerms())));
                item.setItemMeta(meta);
                this.setItem(9, item);
                this.refreshItems();
            }, Material.GREEN_STAINED_GLASS);
            this.addGUIFunction(9, x -> {
                this.GATE.setUseGatePerms(true);
                this.GATE.setUpdated(true);
                ItemStack item = this.getItem(9);
                item.setType(Material.GREEN_STAINED_GLASS);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ConfigManager.getString("GUIS.EditGateGUI.Items.UseGatePerms.Name", "{VALUE}", String.valueOf(this.GATE.isUseGatePerms())));
                item.setItemMeta(meta);
                this.setItem(9, item);
                this.refreshItems();
            }, Material.RED_STAINED_GLASS);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_creategate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_creategate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_creategate.value())) {
            this.addGUIFunction(17, x -> {
                this.GATE.setDHD(null);
                this.GATE.setUpdated(true);
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message10", "{ADDRESS}", this.GATE.getAddress()));
                this.getPlayer().closeInventory();
            }, Globals.DefaultDHDMaterial);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_owner_setprivate.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_setprivate.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_setprivate.value())) {
            this.addGUIFunction(1, x -> {
                this.GATE.setOpen(false);
                this.GATE.setUpdated(true);
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message7", "{ADDRESS}", this.GATE.getAddress()));
                this.setItem(1, Material.ENDER_CHEST, ConfigManager.getString("GUIS.EditGateGUI.Items.Private", new String[0]), null);
                this.refreshItems();
            }, Material.CHEST);
            this.addGUIFunction(1, x -> {
                this.GATE.setOpen(true);
                this.GATE.setUpdated(true);
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message8", "{ADDRESS}", this.GATE.getAddress()));
                this.setItem(1, Material.CHEST, ConfigManager.getString("GUIS.EditGateGUI.Items.Open", new String[0]), null);
                this.refreshItems();
            }, Material.ENDER_CHEST);
        }
        if (this.getPlayer().hasPermission(Perms.thegate_admin_editcoowner.value()) || this.GATE.isOwner(this.getPlayer())) {
            this.addGUIFunction(20, x -> {
                this.getPlayer().closeInventory();
                CoOwnerAddGUI coowneraddgui = new CoOwnerAddGUI(this.getPlayer(), ConfigManager.getString("GUIS.AddCoOwner.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE);
                coowneraddgui.setNextPage(this.createItem(ConfigManager.getString("GUIS.AddCoOwner.Items.NextPage", new String[0]), null, Material.PAPER));
                coowneraddgui.setPrevPage(this.createItem(ConfigManager.getString("GUIS.AddCoOwner.Items.PreviousPage", new String[0]), null, Material.PAPER));
                coowneraddgui.addControleItem(0, this.createItem(ConfigManager.getString("GUIS.AddCoOwner.Items.BackToEditGUI", new String[0]), null, Material.HOPPER));
                coowneraddgui.addControleItemFunctions(0, f -> {
                    this.getPlayer().closeInventory();
                    EditGateGUI editGateGUI = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE);
                    if (editGateGUI.OpenGUI()) {
                        InventoryManager.addGUI(editGateGUI);
                    }
                });
                coowneraddgui.addControleItem(8, this.createItem(ConfigManager.getString("GUIS.AddCoOwner.Items.Info.Name", new String[0]), null, Material.DARK_OAK_SIGN));
                coowneraddgui.addControleItemFunctions(8, f -> this.getPlayer().sendMessage(ConfigManager.getString("GUIS.AddCoOwner.Items.Info.Text", new String[0])));
                if (coowneraddgui.OpenGUI()) {
                    InventoryManager.addGUI(coowneraddgui);
                }
            }, Material.PLAYER_HEAD);
            this.addGUIFunction(19, x -> {
                this.getPlayer().closeInventory();
                CoOwnerListGUI coownerlistgui = new CoOwnerListGUI(this.getPlayer(), ConfigManager.getString("GUIS.RemoveCoOwner.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE, false);
                coownerlistgui.setNextPage(this.createItem(ConfigManager.getString("GUIS.RemoveCoOwner.Items.NextPage", new String[0]), null, Material.PAPER));
                coownerlistgui.setPrevPage(this.createItem(ConfigManager.getString("GUIS.RemoveCoOwner.Items.PreviousPage", new String[0]), null, Material.PAPER));
                coownerlistgui.addControleItem(0, this.createItem(ConfigManager.getString("GUIS.RemoveCoOwner.Items.BackToEditGUI", new String[0]), null, Material.HOPPER));
                coownerlistgui.addControleItemFunctions(0, f -> {
                    this.getPlayer().closeInventory();
                    EditGateGUI editGateGUI = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE);
                    if (editGateGUI.OpenGUI()) {
                        InventoryManager.addGUI(editGateGUI);
                    }
                });
                coownerlistgui.addControleItem(8, this.createItem(ConfigManager.getString("GUIS.RemoveCoOwner.Items.Info.Name", new String[0]), null, Material.DARK_OAK_SIGN));
                coownerlistgui.addControleItemFunctions(8, f -> this.getPlayer().sendMessage(ConfigManager.getString("GUIS.RemoveCoOwner.Items.Info.Text", new String[0])));
                coownerlistgui.setGeneralFunction(f -> {
                    String name;
                    if (f.index < 45 && ((name = f.item.getItemMeta().getDisplayName()) != null || name != "")) {
                        for (String k : this.GATE.getCoOwner().keySet()) {
                            if (!this.GATE.getCoOwner().get(k).equals(name)) continue;
                            Player newCoOwner = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(k));
                            if (newCoOwner != null) {
                                newCoOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.RemoveCoOwner.Message1", "{ADDRESS}", this.GATE.getAddress()));
                            }
                            this.GATE.removeCoOwner(k);
                            TheGateMain.SaveLoadInterface.DeleatePlayerFromCoowner(k, this.GATE.getAddress());
                            this.getPlayer().closeInventory();
                            this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.RemoveCoOwner.Message2", "{PLAYER_NAME}", name));
                            EditGateGUI gui = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE);
                            if (gui.OpenGUI()) {
                                InventoryManager.addGUI(gui);
                            }
                            return;
                        }
                    }
                });
                if (coownerlistgui.OpenGUI()) {
                    InventoryManager.addGUI(coownerlistgui);
                }
            }, Material.PLAYER_HEAD);
        }
        if (this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_admin_editowner.value()) && TheGateMain.theGateMain.getServer().getPlayer(this.GATE.getOwnerUUID()) != null) {
            this.addGUIFunction(18, x -> {
                this.getPlayer().closeInventory();
                CoOwnerListGUI coownerlistgui = new CoOwnerListGUI(this.getPlayer(), ConfigManager.getString("GUIS.SetGateOwner.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE, true);
                coownerlistgui.setNextPage(this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.NextPage", new String[0]), null, Material.PAPER));
                coownerlistgui.setPrevPage(this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.PreviousPage", new String[0]), null, Material.PAPER));
                coownerlistgui.addControleItem(0, this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.BackToEditGUI", new String[0]), null, Material.HOPPER));
                coownerlistgui.addControleItemFunctions(0, f -> {
                    this.getPlayer().closeInventory();
                    EditGateGUI editGateGUI = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE);
                    if (editGateGUI.OpenGUI()) {
                        InventoryManager.addGUI(editGateGUI);
                    }
                });
                coownerlistgui.addControleItem(8, this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.Info.Name", new String[0]), null, Material.DARK_OAK_SIGN));
                coownerlistgui.addControleItemFunctions(8, f -> this.getPlayer().sendMessage(ConfigManager.getString("GUIS.SetGateOwner.Items.Info.Text", new String[0])));
                coownerlistgui.setGeneralFunction(f -> {
                    String name;
                    if (f.index < 45 && ((name = f.item.getItemMeta().getDisplayName()) != null || name != "")) {
                        if (name.equals(Globals.dummyOwnerName)) {
                            EditGateGUI gui;
                            Player prevOwner = TheGateMain.theGateMain.getServer().getPlayer(this.GATE.getOwnerUUID());
                            this.GATE.setOwnerUUID("null");
                            this.GATE.setOwnerName(name);
                            this.GATE.Repair();
                            this.GATE.setUpdated(true);
                            TheGateMain.SaveLoadInterface.DeleatePlayerFromCoowner(this.getPlayer().getUniqueId().toString(), this.GATE.getAddress());
                            this.getPlayer().closeInventory();
                            if (prevOwner != null) {
                                prevOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message2", "{ADDRESS}", this.GATE.getAddress()));
                            }
                            if ((gui = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE)).OpenGUI()) {
                                InventoryManager.addGUI(gui);
                            }
                            return;
                        }
                        for (String k : this.GATE.getCoOwner().keySet()) {
                            if (!this.GATE.getCoOwner().get(k).equals(name)) continue;
                            Player newOwner = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(k));
                            Player prevOwner = null;
                            if (!this.GATE.getOwnerUUID().equals("null")) {
                                prevOwner = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(this.GATE.getOwnerUUID()));
                            }
                            boolean CanOverrideOwnership = false;
                            if (newOwner != null) {
                                if (newOwner.hasPermission(Perms.thegate_admin_creategate.value())) {
                                    CanOverrideOwnership = true;
                                } else if (newOwner.hasPermission(Perms.thegate_admin_creategate.value()) && GateManager.getPlayerGateAmmount(newOwner) < Globals.PlayerGateAmmount) {
                                    CanOverrideOwnership = true;
                                }
                            } else if (GateManager.getPlayerGateAmmount(k) < Globals.PlayerGateAmmount) {
                                CanOverrideOwnership = true;
                            }
                            if (CanOverrideOwnership) {
                                EditGateGUI gui;
                                if (newOwner != null) {
                                    newOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message1", "{ADDRESS}", this.GATE.getAddress()));
                                }
                                if (prevOwner != null) {
                                    this.GATE.addCoOwner(this.GATE.getOwnerUUID(), this.GATE.getOwnerName());
                                }
                                if (prevOwner != null) {
                                    TheGateMain.SaveLoadInterface.AddPlayerToCoowner(this.GATE.getOwnerUUID(), this.GATE.getOwnerName(), this.GATE.getAddress());
                                }
                                this.GATE.setOwnerUUID(k);
                                this.GATE.setOwnerName(name);
                                this.GATE.removeCoOwner(k);
                                this.GATE.Repair();
                                this.GATE.setUpdated(true);
                                TheGateMain.SaveLoadInterface.DeleatePlayerFromCoowner(this.getPlayer().getUniqueId().toString(), this.GATE.getAddress());
                                this.getPlayer().closeInventory();
                                if (prevOwner != null) {
                                    prevOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message2", "{ADDRESS}", this.GATE.getAddress()));
                                }
                                if ((gui = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE)).OpenGUI()) {
                                    InventoryManager.addGUI(gui);
                                }
                            } else {
                                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message3", new String[0]));
                            }
                            return;
                        }
                    }
                });
                if (coownerlistgui.OpenGUI()) {
                    InventoryManager.addGUI(coownerlistgui);
                }
            }, Material.PLAYER_HEAD);
        }
        if (!this.GATE.isOwner(this.getPlayer()) && this.getPlayer().hasPermission(Perms.thegate_admin_editcoowner.value()) && TheGateMain.theGateMain.getServer().getPlayer(this.GATE.getOwnerUUID()) == null) {
            this.addGUIFunction(18, x -> {
                this.getPlayer().closeInventory();
                CoOwnerListGUI coownerlistgui = new CoOwnerListGUI(this.getPlayer(), ConfigManager.getString("GUIS.SetGateOwner.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE, true);
                coownerlistgui.setNextPage(this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.NextPage", new String[0]), null, Material.PAPER));
                coownerlistgui.setPrevPage(this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.PreviousPage", new String[0]), null, Material.PAPER));
                coownerlistgui.addControleItem(0, this.createItem(ConfigManager.getString("GUIS.SetGateOwner.Items.BackToEditGUI", new String[0]), null, Material.HOPPER));
                coownerlistgui.addControleItemFunctions(0, f -> {
                    this.getPlayer().closeInventory();
                    EditGateGUI editGateGUI = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE);
                    if (editGateGUI.OpenGUI()) {
                        InventoryManager.addGUI(editGateGUI);
                    }
                });
                coownerlistgui.setGeneralFunction(f -> {
                    String name;
                    if (f.index < 45 && ((name = f.item.getItemMeta().getDisplayName()) != null || name != "")) {
                        if (name.equals(Globals.dummyOwnerName)) {
                            EditGateGUI gui;
                            Player prevOwner = TheGateMain.theGateMain.getServer().getPlayer(this.GATE.getOwnerUUID());
                            this.GATE.setOwnerUUID("null");
                            this.GATE.setOwnerName(name);
                            this.GATE.Repair();
                            this.GATE.setUpdated(true);
                            TheGateMain.SaveLoadInterface.DeleatePlayerFromCoowner(this.getPlayer().getUniqueId().toString(), this.GATE.getAddress());
                            this.getPlayer().closeInventory();
                            if (prevOwner != null) {
                                prevOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message2", "{ADDRESS}", this.GATE.getAddress()));
                            }
                            if ((gui = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE)).OpenGUI()) {
                                InventoryManager.addGUI(gui);
                            }
                            return;
                        }
                        for (String k : this.GATE.getCoOwner().keySet()) {
                            if (!this.GATE.getCoOwner().get(k).equals(name)) continue;
                            Player newOwner = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(k));
                            Player prevOwner = null;
                            if (!this.GATE.getOwnerUUID().equals("null")) {
                                prevOwner = TheGateMain.theGateMain.getServer().getPlayer(UUID.fromString(this.GATE.getOwnerUUID()));
                            }
                            boolean CanOverrideOwnership = false;
                            if (newOwner != null) {
                                if (newOwner.hasPermission(Perms.thegate_admin_creategate.value())) {
                                    CanOverrideOwnership = true;
                                } else if (newOwner.hasPermission(Perms.thegate_owner_creategate.value()) && GateManager.getPlayerGateAmmount(newOwner) < Globals.PlayerGateAmmount) {
                                    CanOverrideOwnership = true;
                                }
                            } else if (GateManager.getPlayerGateAmmount(k) < Globals.PlayerGateAmmount) {
                                CanOverrideOwnership = true;
                            }
                            if (CanOverrideOwnership) {
                                EditGateGUI gui;
                                if (newOwner != null) {
                                    newOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message1", "{ADDRESS}", this.GATE.getAddress()));
                                }
                                if (prevOwner != null) {
                                    this.GATE.addCoOwner(this.GATE.getOwnerUUID(), this.GATE.getOwnerName());
                                }
                                if (prevOwner != null) {
                                    TheGateMain.SaveLoadInterface.AddPlayerToCoowner(this.GATE.getOwnerUUID(), this.GATE.getOwnerName(), this.GATE.getAddress());
                                }
                                this.GATE.setOwnerUUID(k);
                                this.GATE.setOwnerName(name);
                                this.GATE.removeCoOwner(k);
                                this.GATE.Repair();
                                TheGateMain.SaveLoadInterface.DeleatePlayerFromCoowner(this.getPlayer().getUniqueId().toString(), this.GATE.getAddress());
                                this.getPlayer().closeInventory();
                                if (prevOwner != null) {
                                    prevOwner.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message2", "{ADDRESS}", this.GATE.getAddress()));
                                }
                                if ((gui = new EditGateGUI(this.getPlayer(), ConfigManager.getString("GUIS.EditGateGUI.GUIName", "{ADDRESS}", this.GATE.getAddress()), this.GATE.getAddress(), this.GATE)).OpenGUI()) {
                                    InventoryManager.addGUI(gui);
                                }
                            } else {
                                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.SetOwner.Message3", new String[0]));
                            }
                            return;
                        }
                    }
                });
                if (coownerlistgui.OpenGUI()) {
                    InventoryManager.addGUI(coownerlistgui);
                }
            }, Material.SKELETON_SKULL);
        }
        this.addGUIFunction(24, x -> {
            this.getPlayer().closeInventory();
            String netPrio = ConfigManager.getString("GUIS.NetworkAssignGUI.Primary", new String[0]);
            NetworkAssignGUI nag = new NetworkAssignGUI(this.getPlayer(), ConfigManager.getString("GUIS.NetworkAssignGUI.GUIName", "{NETNAME}", netPrio), netPrio, this.GATE, this);
            if (nag.OpenGUI()) {
                InventoryManager.addGUI(nag);
            }
        }, Material.END_CRYSTAL);
        this.addGUIFunction(25, x -> {
            this.getPlayer().closeInventory();
            String netPrio = ConfigManager.getString("GUIS.NetworkAssignGUI.Secondary", new String[0]);
            NetworkAssignGUI nag = new NetworkAssignGUI(this.getPlayer(), ConfigManager.getString("GUIS.NetworkAssignGUI.GUIName", "{NETNAME}", netPrio), netPrio, this.GATE, this);
            if (nag.OpenGUI()) {
                InventoryManager.addGUI(nag);
            }
        }, Material.END_CRYSTAL);
        this.addGUIFunction(25, x -> {
            this.getPlayer().closeInventory();
            String netPrio = ConfigManager.getString("GUIS.NetworkAssignGUI.Secondary", new String[0]);
            NetworkAssignGUI nag = new NetworkAssignGUI(this.getPlayer(), ConfigManager.getString("GUIS.NetworkAssignGUI.GUIName", "{NETNAME}", netPrio), netPrio, this.GATE, this);
            if (nag.OpenGUI()) {
                InventoryManager.addGUI(nag);
            }
        }, Material.WHITE_STAINED_GLASS_PANE);
        this.addGUIFunction(13, x -> {
            Location DiledLocation = this.GATE.getGate();
            Vector v = GateMath.getRotY(new Vector(0, 0, 2), Math.toRadians(this.GATE.getFacing() * 90.0f));
            Location tpLoc = new Location(DiledLocation.getWorld(), (double)DiledLocation.getBlockX() + 0.5 + v.getX(), (double)(DiledLocation.getBlockY() + 1), (double)DiledLocation.getBlockZ() + 0.5 + v.getZ());
            tpLoc.setYaw(this.GATE.getFacing() * 90.0f);
            this.getPlayer().teleport(tpLoc);
            this.getPlayer().closeInventory();
        }, Material.ELYTRA);
        if (this.getPlayer().hasPermission(Perms.thegate_owner_editmaterial.value()) && this.GATE.isOwner(this.getPlayer()) || this.getPlayer().hasPermission(Perms.thegate_owner_editmaterial.value()) && isCoowner || this.getPlayer().hasPermission(Perms.thegate_admin_editmaterial.value())) {
            this.addGUIFunction(28, x -> {
                this.GATE.setRingMaterial(Globals.DefaultringMaterial);
                this.GATE.setChevrons_frameMaterial(Globals.Defaultchevrons_frameMaterial);
                this.GATE.setChevron_botMaterial(Globals.Defaultchevron_botMaterial);
                this.GATE.setChevron_lightMaterial(Globals.Defaultchevron_lightMaterial);
                this.GATE.setChevron_lightMaterial_on(Globals.Defaultchevron_lightMaterial_ON);
                this.GATE.setHorizonMaterial(Globals.DefaulthorizonMaterial);
                this.GATE.Refresh();
                this.getPlayer().closeInventory();
                this.GATE.setUpdated(true);
            }, Material.ENDER_PEARL);
            this.setGeneralFunction(x -> {
                if (x.index > 28 && x.index < 35 && !x.event.getView().getCursor().getType().equals((Object)Material.AIR)) {
                    ItemStack i = this.getItem(x.index);
                    i.setType(x.event.getView().getCursor().getType());
                    this.setItem(x.index, i);
                    this.refreshItems();
                }
            });
            this.addGUIFunction(35, x -> {
                this.GATE.setRingMaterial(this.getItem(29).getType());
                this.GATE.setChevron_botMaterial(this.getItem(30).getType());
                this.GATE.setChevrons_frameMaterial(this.getItem(31).getType());
                this.GATE.setChevron_lightMaterial(this.getItem(32).getType());
                this.GATE.setChevron_lightMaterial_on(this.getItem(33).getType());
                this.GATE.setHorizonMaterial(this.getItem(34).getType());
                this.GATE.Refresh();
                this.getPlayer().sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditGateGUI.Message9", "{ADDRESS}", this.GATE.getAddress()));
                this.GATE.setUpdated(true);
            }, Material.REDSTONE_TORCH);
        }
        this.addGUIFunction(22, x -> {
            x.player.closeInventory();
            IrisEditGUI irisEditGUI = new IrisEditGUI(x.player, ConfigManager.getString("GUIS.IrisEditGUI.GUIName", new String[0]), "IrisEditGUI", this.GATE, this);
            if (irisEditGUI.OpenGUI()) {
                InventoryManager.addGUI(irisEditGUI);
            }
        }, Material.FIRE_CHARGE);
    }
}

