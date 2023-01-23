package de.hunjy.armorstand;

import de.hunjy.utils.Colorizer;
import de.hunjy.visual.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class GlowColor {

    private String name;
    private ChatColor chatColor;
    private String prettyColor;
    private String displayName;
    private Material displayMaterial;

    public GlowColor(String name, ChatColor chatColor, String prettyColor, String displayName, Material displayMaterial) {
        this.name = name;
        this.chatColor = chatColor;
        this.prettyColor = prettyColor;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return chatColor;
    }

    public String getPrettyColor() {
        return prettyColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getDisplayItem() {
        return new ItemBuilder(displayMaterial).setDisplayName(Colorizer.hex(displayName, prettyColor)).addNBTTag("HYPERSTAND_GLOW_COLOR", name).build();
    }

    public ItemStack getDisplayItemWithID(ArmorStand armorStand) {
        return new ItemBuilder(displayMaterial).setDisplayName(Colorizer.hex(displayName, prettyColor)).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_GLOW_COLOR", name).build();
    }
}
