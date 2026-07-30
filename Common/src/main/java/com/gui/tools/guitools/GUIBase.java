/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.gui.tools.guitools;

import com.google.common.collect.Sets;
import com.gui.tools.guitools.DispatchInformations;
import com.gui.tools.guitools.GUIAccessCondition;
import com.gui.tools.guitools.GUIFunction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GUIBase {
    protected final String name;
    private Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
    private Map<Integer, Set<String>> itemPerms = new HashMap<Integer, Set<String>>();
    private Map<Integer, Map<Material, GUIFunction>> functions = new HashMap<Integer, Map<Material, GUIFunction>>();
    private Map<Integer, String> ErrorMessages = new HashMap<Integer, String>();
    private Map<ItemStack, Object> attachedObjects = new HashMap<ItemStack, Object>();
    private String defaultErrorMessage = "";
    protected final Player OpendBy;
    protected final int size;
    private final String Tag;
    protected Inventory inventory;
    private Set<String> UIAccessPermissions = new HashSet<String>();
    private Set<Integer> NoEventCancle = new HashSet<Integer>();
    private GUIAccessCondition accessCondition;
    private GUIFunction generalFunction;
    private boolean filterUnaccassable = true;

    public GUIBase(Player p, int size, String name, String Tag) {
        this.name = name;
        this.OpendBy = p;
        this.size = size;
        this.Tag = Tag;
    }

    public boolean OpenGUI() {
        if (!this.hasAccess() && this.accessCondition != null && !this.accessCondition.access(this)) {
            return false;
        }
        this.inventory = Bukkit.createInventory((InventoryHolder)this.OpendBy, (int)this.size, (String)this.name);
        this.refreshItems();
        this.OpendBy.openInventory(this.inventory);
        return true;
    }

    public void setItem(int index, ItemStack item) {
        this.items.put(index, item);
    }

    public void attatchObject(int index, Object object) {
        this.attatchObject(this.getItem(index), object);
    }

    public void attatchObject(ItemStack item, Object object) {
        this.attachedObjects.put(item, object);
    }

    public Object getAttachedObject(int index) {
        return this.getAttatchedObject(this.getItem(index));
    }

    public Object getAttatchedObject(ItemStack item) {
        return this.attachedObjects.get(item);
    }

    public void setItem(int index, Material m, String name, List<String> lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null && lore.size() > 0) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        this.setItem(index, item);
    }

    public void addItems(List<ItemStack> items) {
        int i = 0;
        while (i < items.size()) {
            if (i < this.size) {
                this.setItem(i, items.get(i));
            }
            ++i;
        }
    }

    public void addUIAccessPermission(String ... perm) {
        this.UIAccessPermissions.clear();
        String[] stringArray = perm;
        int n = perm.length;
        int n2 = 0;
        while (n2 < n) {
            String s = stringArray[n2];
            this.UIAccessPermissions.add(s);
            ++n2;
        }
    }

    public void additemPerms(int index, String ... perm) {
        if (!this.itemPerms.keySet().contains(index)) {
            HashSet perms = Sets.newHashSet((Object[])perm);
            this.itemPerms.put(index, perms);
        } else {
            Set<String> perms = this.itemPerms.get(index);
            String[] stringArray = perm;
            int n = perm.length;
            int n2 = 0;
            while (n2 < n) {
                String s = stringArray[n2];
                perms.add(s);
                ++n2;
            }
            this.itemPerms.put(index, perms);
        }
    }

    public void setGUIAccessCondition(GUIAccessCondition c2) {
        this.accessCondition = c2;
    }

    public ItemStack createItem(String itemName, List<String> lore, Material m) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(itemName);
        if (lore != null && lore.size() > 0) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void addItemErrorMessage(int index, String msg) {
        this.ErrorMessages.put(index, msg);
    }

    public void setDefaultErrorMessage(String msg) {
        this.defaultErrorMessage = msg;
    }

    public void addGUIFunction(int index, GUIFunction f, Material m) {
        if (this.functions.get(index) != null) {
            Map<Material, GUIFunction> map = this.functions.get(index);
            map.put(m, f);
            this.functions.put(index, map);
        } else {
            HashMap<Material, GUIFunction> map = new HashMap<Material, GUIFunction>();
            map.put(m, f);
            this.functions.put(index, map);
        }
    }

    public void addNoCancleRange(int from, int to) {
        int i = from;
        while (i <= to) {
            this.NoEventCancle.add(i);
            ++i;
        }
    }

    public void addNoCancleRange(int ... index) {
        int[] nArray = index;
        int n = index.length;
        int n2 = 0;
        while (n2 < n) {
            int i = nArray[n2];
            this.NoEventCancle.add(i);
            ++n2;
        }
    }

    public void ClearNoCancle() {
        if (this.NoEventCancle != null) {
            this.NoEventCancle.clear();
        }
    }

    public void ClearUI() {
        if (this.inventory != null && this.items != null && this.itemPerms != null && this.functions != null) {
            this.items.clear();
            this.itemPerms.clear();
            this.functions.clear();
            this.inventory.clear();
        }
    }

    public void refreshItems() {
        if (this.inventory != null) {
            this.inventory.clear();
            this.items.keySet().stream().filter(x -> {
                if (!this.itemPerms.keySet().contains(x) || !this.filterUnaccassable) {
                    return true;
                }
                boolean canAccess = false;
                for (String s : this.itemPerms.get(x)) {
                    if (!this.OpendBy.hasPermission(s)) continue;
                    canAccess = true;
                }
                return canAccess;
            }).forEach(x -> this.inventory.setItem(x.intValue(), this.items.get(x)));
        }
    }

    public void dispatch(DispatchInformations info) {
        boolean hasAccess = false;
        if (this.NoEventCancle.contains(info.index) && info.event.getClickedInventory().equals(this.inventory)) {
            info.event.setCancelled(false);
        } else {
            info.event.setCancelled(true);
        }
        if (this.itemPerms.keySet().contains(info.index)) {
            for (String s : this.itemPerms.get(info.index)) {
                if (!this.OpendBy.hasPermission(s)) continue;
                hasAccess = true;
            }
            if (!hasAccess) {
                if (this.ErrorMessages.keySet().contains(info.index)) {
                    this.OpendBy.sendMessage(this.ErrorMessages.get(info.index));
                } else {
                    this.OpendBy.sendMessage(this.defaultErrorMessage);
                }
            }
        } else {
            hasAccess = true;
        }
        if (hasAccess && this.functions.keySet().contains(info.index) && this.functions.get(info.index).get(info.item.getType()) != null && this.functions.get(info.index).keySet().contains(info.item.getType())) {
            this.functions.get(info.index).get(info.item.getType()).dispatch(info);
        }
        if (this.generalFunction != null) {
            this.generalFunction.dispatch(info);
        }
    }

    public boolean isActionAllowed(InventoryView inv) {
        if (this.UIAccessPermissions.size() == 0) {
            return true;
        }
        boolean hasAccess = false;
        if (inv.getTitle().equalsIgnoreCase(this.name)) {
            for (String s : this.UIAccessPermissions) {
                if (!this.OpendBy.hasPermission(s)) continue;
                hasAccess = true;
            }
        }
        return hasAccess;
    }

    public boolean hasAccess() {
        if (this.UIAccessPermissions.size() < 1) {
            return true;
        }
        for (String s : this.UIAccessPermissions) {
            if (!this.OpendBy.hasPermission(s)) continue;
            return true;
        }
        return false;
    }

    public boolean isItemOfInventory(ItemStack item) {
        for (ItemStack i : this.items.values()) {
            if (!item.equals((Object)i)) continue;
            return true;
        }
        return false;
    }

    public ItemStack getItem(int index) {
        if (this.items.get(index) != null && this.items.get(index).equals((Object)this.inventory.getItem(index))) {
            return this.items.get(index);
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        return this.OpendBy;
    }

    public String getTag() {
        return this.Tag;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setGeneralFunction(GUIFunction f) {
        this.generalFunction = f;
    }

    public Set<Integer> getNoEventCancle() {
        return this.NoEventCancle;
    }

    public boolean isFilterUnaccassable() {
        return this.filterUnaccassable;
    }

    public void setFilterUnaccassable(boolean filterUnaccassable) {
        this.filterUnaccassable = filterUnaccassable;
    }

    public int getSize() {
        return this.size;
    }

    public Map<Integer, ItemStack> getItems() {
        return this.items;
    }

    public Map<Integer, Set<String>> getItemPerms() {
        return this.itemPerms;
    }

    public Map<Integer, Map<Material, GUIFunction>> getFunctions() {
        return this.functions;
    }

    public Map<Integer, String> getErrorMessages() {
        return this.ErrorMessages;
    }

    public String getDefaultErrorMessage() {
        return this.defaultErrorMessage;
    }

    public Set<String> getUIAccessPermissions() {
        return this.UIAccessPermissions;
    }

    public GUIFunction getGeneralFunction() {
        return this.generalFunction;
    }

    public Map<ItemStack, Object> getAttachedObjects() {
        return this.attachedObjects;
    }

    public void setAttachedObjects(Map<ItemStack, Object> attachedObjects) {
        this.attachedObjects = attachedObjects;
    }
}

