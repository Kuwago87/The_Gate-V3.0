/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.gui.tools.guitools;

import com.gui.tools.guitools.GUIBase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DispatchInformations {
    public final ItemStack item;
    public final int index;
    public final GUIBase base;
    public final Player player;
    public final InventoryClickEvent event;
    public final Object attachedObject;

    public DispatchInformations(ItemStack item, int index, GUIBase base, Player player, InventoryClickEvent e) {
        this.item = item;
        this.index = index;
        this.base = base;
        this.player = player;
        this.event = e;
        this.attachedObject = base.getAttatchedObject(item);
    }
}

