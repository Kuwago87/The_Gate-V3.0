/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.gui.tools.guitools;

import com.gui.tools.guitools.GUIBase;
import com.gui.tools.guitools.GUIFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIPages
extends GUIBase {
    private ArrayList<ItemStack> SorceList = new ArrayList();
    private Map<Integer, ItemStack> ControleItems = new HashMap<Integer, ItemStack>();
    private Map<Integer, GUIFunction> ControleItemFunctions = new HashMap<Integer, GUIFunction>();
    private int page = 0;
    private ItemStack nextPage;
    private ItemStack prevPage;
    private ItemStack current;
    private ItemStack filler;
    int PageCount = 0;

    public GUIPages(Player p, int size, String name, String Tag) {
        super(p, size < 18 ? 18 : size, name, Tag);
        int i = 0;
        while (i < 9) {
            this.ControleItems.put(i, this.filler);
            ++i;
        }
    }

    @Override
    public boolean OpenGUI() {
        if (!this.hasAccess()) {
            return false;
        }
        this.inventory = Bukkit.createInventory((InventoryHolder)this.OpendBy, (int)this.size, (String)this.name);
        this.nextPage = new ItemStack(Material.PAPER);
        ItemMeta nextPageMeta = this.nextPage.getItemMeta();
        nextPageMeta.setDisplayName("Next Page");
        this.nextPage.setItemMeta(nextPageMeta);
        this.prevPage = new ItemStack(Material.PAPER);
        ItemMeta prevPageMeta = this.prevPage.getItemMeta();
        prevPageMeta.setDisplayName("Previous Page");
        this.prevPage.setItemMeta(prevPageMeta);
        this.current = new ItemStack(Material.PAPER);
        ItemMeta currentMeta = this.current.getItemMeta();
        currentMeta.setDisplayName("Current");
        this.current.setItemMeta(currentMeta);
        this.filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = this.current.getItemMeta();
        fillerMeta.setDisplayName(" ");
        this.filler.setItemMeta(fillerMeta);
        this.construct();
        this.refreshItems();
        this.OpendBy.openInventory(this.inventory);
        return true;
    }

    public void addControleItem(int index, ItemStack item) {
        if (index < 9) {
            this.ControleItems.put(index, item);
        }
    }

    public void addControleItemFunctions(int index, GUIFunction f) {
        if (index < 9) {
            this.ControleItemFunctions.put(index, f);
        }
    }

    private void construct() {
        int offset = 0;
        int to = this.SorceList.size() + (this.getSize() - 9 - this.SorceList.size() % (this.getSize() - 9));
        int i = 0;
        while (i < to) {
            if (i % (this.getSize() - 9) == 0 && i > 0) {
                offset += 9;
                ++this.PageCount;
            }
            if (i < this.SorceList.size()) {
                this.setItem(i + offset, this.SorceList.get(i));
            } else {
                this.setItem(i + offset, this.filler);
            }
            ++i;
        }
        i = 0;
        while (i <= this.PageCount) {
            this.setItem(this.getSize() * i + (this.getSize() - 9), this.ControleItems.get(0) != null ? this.ControleItems.get(0) : this.filler);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 9), this.ControleItemFunctions.get(0), this.ControleItems.get(0) != null ? this.ControleItems.get(0).getType() : this.filler.getType());
            this.setItem(this.getSize() * i + (this.getSize() - 8), this.prevPage);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 8), x -> {
                if (this.page > 0) {
                    --this.page;
                    this.refreshItems();
                }
            }, Material.PAPER);
            this.setItem(this.getSize() * i + (this.getSize() - 7), this.ControleItems.get(2) != null ? this.ControleItems.get(2) : this.filler);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 7), this.ControleItemFunctions.get(2), this.ControleItems.get(2) != null ? this.ControleItems.get(2).getType() : this.filler.getType());
            this.setItem(this.getSize() * i + (this.getSize() - 6), this.ControleItems.get(3) != null ? this.ControleItems.get(3) : this.filler);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 6), this.ControleItemFunctions.get(3), this.ControleItems.get(3) != null ? this.ControleItems.get(3).getType() : this.filler.getType());
            this.current = new ItemStack(Material.PAPER);
            ItemMeta currentMeta = this.current.getItemMeta();
            int c2 = i + 1;
            int t = this.PageCount + 1;
            currentMeta.setDisplayName(String.valueOf(c2) + "/" + t);
            this.current.setItemMeta(currentMeta);
            this.setItem(this.getSize() * i + (this.getSize() - 5), this.current);
            this.setItem(this.getSize() * i + (this.getSize() - 4), this.ControleItems.get(5) != null ? this.ControleItems.get(5) : this.filler);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 4), this.ControleItemFunctions.get(5), this.ControleItems.get(5) != null ? this.ControleItems.get(5).getType() : this.filler.getType());
            this.setItem(this.getSize() * i + (this.getSize() - 3), this.ControleItems.get(6) != null ? this.ControleItems.get(6) : this.filler);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 3), this.ControleItemFunctions.get(6), this.ControleItems.get(6) != null ? this.ControleItems.get(6).getType() : this.filler.getType());
            this.setItem(this.getSize() * i + (this.getSize() - 2), this.nextPage);
            this.addGUIFunction(i * (this.getSize() - 2), x -> {
                if (this.page < this.PageCount) {
                    ++this.page;
                    this.refreshItems();
                }
            }, Material.PAPER);
            this.setItem(this.getSize() * i + (this.getSize() - 1), this.ControleItems.get(8) != null ? this.ControleItems.get(8) : this.filler);
            this.addGUIFunction(this.getSize() * i + (this.getSize() - 1), this.ControleItemFunctions.get(8), this.ControleItems.get(8) != null ? this.ControleItems.get(8).getType() : this.filler.getType());
            ++i;
        }
    }

    @Override
    public void refreshItems() {
        if (this.getInventory() != null) {
            this.getInventory().clear();
            int i = 0;
            while (i < this.getSize()) {
                this.getInventory().setItem(i, this.getItems().get(i + this.page * this.getSize()));
                ++i;
            }
        }
    }

    public ItemStack getNextPage() {
        return this.nextPage;
    }

    public void setNextPage(ItemStack nextPage) {
        this.nextPage = nextPage;
    }

    public ItemStack getCurrent() {
        return this.current;
    }

    public void setCurrent(ItemStack current) {
        this.current = current;
    }

    public ItemStack getFiller() {
        return this.filler;
    }

    public void setFiller(ItemStack filler) {
        this.filler = filler;
    }

    public ArrayList<ItemStack> getSorceList() {
        return this.SorceList;
    }

    public void setSorceList(ArrayList<ItemStack> sorceList) {
        this.SorceList = sorceList;
    }

    public ItemStack getPrevPage() {
        return this.prevPage;
    }

    public void setPrevPage(ItemStack prevPage) {
        this.prevPage = prevPage;
    }
}

