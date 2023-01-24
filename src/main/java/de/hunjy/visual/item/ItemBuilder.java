package de.hunjy.visual.item;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;
    private boolean cancelClick = true;
    private Map<String, String> nbts = new HashMap();
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }


    public ItemBuilder setCancelClick(boolean cancelClick) {
        this.cancelClick = cancelClick;
        return this;
    }

    public ItemBuilder removeAllAttributes() {
        this.meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        this.meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        this.meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        return this;
    }

    public ItemBuilder addNBTTag(String key, String value) {
        this.nbts.put(key, value);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder addLore(ArrayList<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        this.meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }
    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        if(item == null) {
            return null;
        }

        if(item.getType() == Material.AIR) {
            return item;
        }

        NBTItem nbtItem = new NBTItem(this.item);
        nbtItem.setBoolean("GUI_CLICK_CANCEL", this.cancelClick);

        for (String key : this.nbts.keySet()) {
            String value = (String) this.nbts.get(key);
            nbtItem.setString(key, value);
        }

        return nbtItem.getItem();
    }
}
