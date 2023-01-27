package de.hunjy.armorstand;

import de.hunjy.HyperStand;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ArmorStandManager {
    private final HashMap<UUID, ArmorStandEditType> activeEditType = new HashMap<>();
    private final HashMap<UUID, ArmorStand> selectedArmorStand = new HashMap<>();
    private final HashMap<UUID, Location> locationCache = new HashMap<>();

    private final HashMap<UUID, ItemStack> itemCache = new HashMap<>();
    private final HashSet<GlowColor> glowColors = new HashSet<>();
    private final NamespacedKey namespacedKey = new NamespacedKey("hyperstand", "owner");

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public ArmorStandManager() {
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_WHITE", ChatColor.WHITE, "F9FFFE", "Weiß", Material.WHITE_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_LIGHT_GRAY", ChatColor.GRAY, "9D9D97", "Hell Grau", Material.LIGHT_GRAY_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_GRAY", ChatColor.DARK_GRAY, "474F52", "Grau", Material.GRAY_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_BLACK", ChatColor.BLACK, "1D1D21", "Schwarz", Material.BLACK_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_RED", ChatColor.RED, "B02E26", "Rot", Material.RED_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_ORANGE", ChatColor.GOLD, "F9801D", "Orange", Material.ORANGE_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_YELLOW", ChatColor.YELLOW, "FED83D", "Gelb", Material.YELLOW_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_LIGHT_GREEN", ChatColor.GREEN, "80C71F", "Hell Grün", Material.LIME_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_GREEN", ChatColor.DARK_GREEN, "5E7C16", "Grün", Material.GREEN_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_CYAN", ChatColor.DARK_AQUA, "169C9C", "Cyan", Material.CYAN_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_LIGHT_BLUE", ChatColor.BLUE, "3AB3DA", "Hell Blau", Material.LIGHT_BLUE_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_BLUE", ChatColor.DARK_BLUE, "3C44AA", "Blau", Material.BLUE_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_PURPLE", ChatColor.DARK_PURPLE, "8932B8", "Lila", Material.PURPLE_DYE));
        glowColors.add(new GlowColor("HYPERSTAND_GLOW_PINK", ChatColor.LIGHT_PURPLE, "F38BAA", "Pink", Material.PINK_DYE));


        registerGlowTeams();
    }

    private void registerGlowTeams() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (GlowColor glowColor : glowColors) {
            Team team = scoreboard.getTeam(glowColor.getName());
            if (team == null) team = scoreboard.registerNewTeam(glowColor.getName());
            team.setColor(glowColor.getColor());
        }
    }

    public GlowColor getGlowColorByName(String name) {
        for (GlowColor glowColor : glowColors) {
            if (glowColor.getName().equals(name)) {
                return glowColor;
            }
        }
        return null;
    }

    public GlowColor getCurrentGlowColor(ArmorStand armorStand) {
        Team team = scoreboard.getEntityTeam(armorStand);
        if (team == null) {
            return null;
        }

        return getGlowColorByName(team.getName());
    }

    public void setGlowColor(ArmorStand armorStand, GlowColor glowColor) {
        scoreboard.getTeam(glowColor.getName()).addEntity(armorStand);
        armorStand.setGlowing(true);
    }

    public HashMap<UUID, ArmorStand> getSelectedArmorStand() {
        return selectedArmorStand;
    }

    public void toggleArmorStandEquipmentLock(ArmorStand armorStand) {
        if (armorStand.hasEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING)) {
            armorStand.removeEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.removeEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.removeEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.removeEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.removeEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.removeEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        } else {
            armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        }
    }

    public boolean trySelectArmorStand(Player player, ArmorStand armorStand) {

        if (armorStandIsInUse(armorStand)) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_ALREADY_IN_USE", true));
            return true;
        }

        if (player.hasPermission("hyperstand.admin.bypass")) return false;
        if (!isEdibleArmorStand(armorStand)) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_NOT_EDIBLE", true));
            return true;
        }
        if (!canSelectArmorStand(player, armorStand)) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_NO_ACCESS", true));
            return true;
        }
        return false;
    }

    public void createHyperStand(Player player, ArmorStand armorStand) {
        armorStand.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, player.getUniqueId().toString());
    }

    public boolean armorStandIsInUse(ArmorStand armorStand) {
        return selectedArmorStand.containsValue(armorStand);
    }

    public boolean isEdibleArmorStand(ArmorStand armorStand) {
        return armorStand.getPersistentDataContainer().has(namespacedKey);
    }

    public boolean canSelectArmorStand(Player player, ArmorStand armorStand) {
        if (armorStand.getPersistentDataContainer().has(namespacedKey)) {
            if (armorStand.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING).equals(player.getUniqueId().toString())) {
                return true;
            }
        }
        return player.hasPermission("hyperstand.admin.bypass");
    }

    public boolean hasSelectedArmorStand(Player player) {
        return selectedArmorStand.containsKey(player.getUniqueId());
    }

    public ArmorStand getSelectedArmorStand(Player player) {
        if (selectedArmorStand.containsKey(player.getUniqueId())) {
            return selectedArmorStand.get(player.getUniqueId());
        }
        return null;
    }

    public void removeSelectedArmorStand(Player player) {
        selectedArmorStand.remove(player.getUniqueId());
    }

    public void setSelectedArmorStand(Player player, ArmorStand armorStand) {
        locationCache.put(player.getUniqueId(), armorStand.getLocation());
        selectedArmorStand.put(player.getUniqueId(), armorStand);
    }

    public boolean isEditing(Player player) {
        return activeEditType.containsKey(player.getUniqueId());
    }

    public ArmorStandEditType getCurrentEditType(Player player) {
        return activeEditType.get(player.getUniqueId());
    }

    public void startEdit(Player player, ArmorStand armorStand, ArmorStandEditType armorStandEditType) {
        setSelectedArmorStand(player, armorStand);
        player.getInventory().setHeldItemSlot(4);
        activeEditType.put(player.getUniqueId(), armorStandEditType);
    }

    public void finishEditing(Player player) {
        removeSelectedArmorStand(player);
        activeEditType.remove(player.getUniqueId());
    }

    public void pickUpArmorStand(Player player, ArmorStand armorStand) {
        selectedArmorStand.put(player.getUniqueId(), armorStand);
        activeEditType.put(player.getUniqueId(), ArmorStandEditType.PICK_UP);
        player.sendTitle("§eArmorStand aufgehoben", "§7Rechtsklick um wieder loszulassen", 5, 60, 5);
    }

    public void setDisplayName(ArmorStand armorStand, String displayName) {
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(displayName.replace("&", "§"));
    }

    public void useHyperStandItem(Player player, ItemStack itemStack) {
        player.getInventory().remove(itemStack);
        itemCache.put(player.getUniqueId(), itemStack);
    }

    public void returnHyperStandItemToPlayer(Player player) {
        if (itemCache.containsKey(player.getUniqueId())) {
            player.getInventory().addItem(itemCache.get(player.getUniqueId()));
        }
    }

    public void returnArmorStand(Player player) {
        ArmorStand armorStand = getSelectedArmorStand(player);
        Location location = locationCache.get(player.getUniqueId());
        armorStand.teleport(location);
        finishEditing(player);
    }
}