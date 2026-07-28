/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.inventory.Inventory
 */
package com.gui.tools.guitools;

import com.gui.tools.guitools.DispatchInformations;
import com.gui.tools.guitools.GUIBase;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryManager
implements Listener {
    private static Map<Player, GUIBase> GUIs = new HashMap<Player, GUIBase>();

    public static void addGUI(GUIBase gui) {
        GUIs.put(gui.getPlayer(), gui);
    }

    public static void removeGUI(GUIBase gui) {
        GUIs.remove(gui.getPlayer());
    }

    public static GUIBase getGUIbyPlayer(Player player) {
        return GUIs.get(player);
    }

    public static GUIBase getGUIbyInventory(Inventory invent) {
        for (GUIBase inv : GUIs.values()) {
            if (!inv.getInventory().equals(invent)) continue;
            return inv;
        }
        return null;
    }

    public static void removeGUIofPlayer(Player p) {
        GUIs.remove(p);
    }

    public static Set<GUIBase> getGUIbyTag(String tag) {
        HashSet<GUIBase> out = new HashSet<GUIBase>();
        for (GUIBase inv : GUIs.values()) {
            if (!inv.getTag().equals(tag)) continue;
            out.add(inv);
        }
        return out;
    }

    @EventHandler
    public void playerInventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player)e.getPlayer();
            InventoryManager.removeGUIofPlayer(p);
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        int rawClick = e.getRawSlot();
        if (rawClick < 0) {
            return;
        }
        GUIBase gui = InventoryManager.getGUIbyPlayer(p);
        if (gui == null) {
            return;
        }
        if (e.getRawSlot() >= e.getInventory().getSize()) {
            InventoryAction action = e.getAction();
            switch (action) {
                case MOVE_TO_OTHER_INVENTORY: {
                    e.setCancelled(true);
                    return;
                }
                case HOTBAR_MOVE_AND_READD: {
                    e.setCancelled(true);
                    return;
                }
                case HOTBAR_SWAP: {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if (gui.isActionAllowed(p.getOpenInventory()) && e.getCurrentItem() == null && e.getRawSlot() < gui.getSize()) {
            gui.dispatch(new DispatchInformations(e.getCurrentItem(), rawClick, gui, p, e));
        } else if (e.getCurrentItem() != null && e.getRawSlot() < gui.getSize()) {
            e.setCancelled(true);
            gui.dispatch(new DispatchInformations(e.getCurrentItem(), rawClick, gui, p, e));
        }
    }

    @EventHandler
    public void onDragDrop(InventoryDragEvent e) {
        Player p = (Player)e.getWhoClicked();
        GUIBase gui = InventoryManager.getGUIbyPlayer(p);
        if (gui == null) {
            return;
        }
        if (gui.isActionAllowed(p.getOpenInventory())) {
            Iterator iterator = e.getRawSlots().iterator();
            while (iterator.hasNext()) {
                int i = (Integer)iterator.next();
                if (i >= e.getInventory().getSize()) continue;
                if (e.getCursor() != null && !e.getCursor().equals((Object)e.getOldCursor())) {
                    e.setCancelled(true);
                    continue;
                }
                if (e.getCursor() != null || e.getOldCursor() == null) continue;
                e.setCancelled(true);
            }
        }
    }
}

