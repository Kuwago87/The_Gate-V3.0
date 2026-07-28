/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package thegate.guis;

import com.gui.tools.guitools.GUIBase;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import thegate.gate.CreateGate;
import thegate.gate.GateManager;
import thegate.gate.GateObject;
import thegate.main.ConfigManager;
import thegate.main.Globals;
import thegate.main.Perms;
import thegate.main.TheGateMain;

public class EditAllGatesGUI
extends GUIBase {
    public EditAllGatesGUI(Player p, String name) {
        super(p, 18, name, "EditAllGatesGUI");
        this.setup();
        this.setupFunctions();
        this.CondPerms();
    }

    public void CondPerms() {
        this.setDefaultErrorMessage(ConfigManager.getString("PlayerMessages.GlobalText.NoPermission", new String[0]));
        this.addUIAccessPermission(Perms.thegate_admin_editallgates.value());
    }

    public void setup() {
        int i = 0;
        while (i < 18) {
            this.setItem(i, Material.BLACK_STAINED_GLASS_PANE, " ", null);
            ++i;
        }
        this.setItem(0, Material.WRITABLE_BOOK, ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Save", new String[0]), null);
        this.setItem(1, Material.BOOK, ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Load", new String[0]), null);
        this.setItem(3, Material.GOLDEN_PICKAXE, ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Repair", new String[0]), null);
        ArrayList<String> RemoveGateLore = new ArrayList<String>();
        RemoveGateLore.add(ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Remove_Armorstands.Lore", new String[0]));
        this.setItem(4, Material.LAVA_BUCKET, ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Remove_Armorstands.Name", new String[0]), RemoveGateLore);
        ArrayList<String> RemoveAllGateLore = new ArrayList<String>();
        RemoveAllGateLore.add(ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Clear_Gate_List.Lore1", new String[0]));
        RemoveAllGateLore.add(ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Clear_Gate_List.Lore2", new String[0]));
        this.setItem(5, Material.BARRIER, ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Clear_Gate_List.Name", new String[0]), RemoveAllGateLore);
        this.setItem(7, Material.TNT, ConfigManager.getString("GUIS.EditAllGatesGUI.Items.Cut_all_Gate_connections", new String[0]), null);
    }

    public void setupFunctions() {
        this.addGUIFunction(0, x -> {
            TheGateMain.SaveLoadInterface.SaveDataFromSet(GateManager.getGatesAsSet());
            x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message7", new String[0]));
        }, Material.WRITABLE_BOOK);
        this.addGUIFunction(1, x -> {
            TheGateMain.SaveLoadInterface.Load(TheGateMain.theGateMain, x.player);
            TheGateMain.SaveLoadInterface.LoadCoowners(TheGateMain.theGateMain);
            x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message1", new String[0]));
        }, Material.BOOK);
        this.addGUIFunction(3, x -> {
            for (GateObject obj : GateManager.getGatesAsSet()) {
                obj.Repair();
                x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message2", "{ADDRESS}", obj.getAddress()));
            }
        }, Material.GOLDEN_PICKAXE);
        this.addGUIFunction(4, x -> {
            for (GateObject obj : GateManager.getGatesAsSet()) {
                obj.Vanish(new Player[0]);
                if (!Globals.CreateBarrier) continue;
                CreateGate.RemoveBarrier(obj.getGate(), obj.getFacing());
            }
            x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message3", new String[0]));
            x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message4", new String[0]));
        }, Material.LAVA_BUCKET);
        this.addGUIFunction(5, x -> {
            for (GateObject obj : GateManager.getGatesAsSet()) {
                obj.Vanish(new Player[0]);
                if (!Globals.CreateBarrier) continue;
                CreateGate.RemoveBarrier(obj.getGate(), obj.getFacing());
            }
            GateManager.ClearLists();
            TheGateMain.SaveLoadInterface.clearDatabase();
            x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message6", new String[0]));
        }, Material.BARRIER);
        this.addGUIFunction(7, x -> {
            for (GateObject obj : GateManager.getGatesAsSet()) {
                obj.Deactivate();
            }
            x.player.sendMessage(ConfigManager.getString("PlayerMessages.FromGUI.EditAllGatesGUI.Message5", new String[0]));
        }, Material.TNT);
    }
}

