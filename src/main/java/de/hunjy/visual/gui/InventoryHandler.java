package de.hunjy.visual.gui;

import de.hunjy.HyperStand;
import de.hunjy.armorstand.ArmorStandManager;
import de.hunjy.template.ArmorStandTemplate;
import de.hunjy.utils.Colorizer;
import de.hunjy.visual.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {

    private ArmorStandManager armorStandManager;

    public InventoryHandler() {
        armorStandManager = HyperStand.getInstance().getArmorStandManager();
    }

    public Inventory createInventory(Player player, String title, InventoryType inventoryType) {
        return Bukkit.createInventory(player, inventoryType, title);
    }

    public Inventory createInventory(Player player, String title, int size) {
        return Bukkit.createInventory(player, size, title);
    }

    public void fillBackground(Inventory inventory, ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
    }

    public void openMainMenu(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Menü", 27);

        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());

        inventory.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName("§l§3Rüstung").addLore("§7Ändere die Rüstung den ausgewählten Rüstungsständer.").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_ARMOR_MENU").build());

        inventory.setItem(13, new ItemBuilder(Material.COMPASS).setDisplayName("§l§dPosition").addLore("§7Ändere die Position des ArmorStand.").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_POSITION_MENU").build());

        inventory.setItem(15, new ItemBuilder(Material.COMPARATOR).setDisplayName("§l§cEinstellungen").addLore("§7Ändere Diverse Einstellungen wie:", "§e- Schwerkraft", "§e- Arme", "§e- Leuchten").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_SETTINGS_MENU").build());

        player.openInventory(inventory);
        HyperStand.getInstance().getArmorStandManager().setSelectedArmorStand(player, armorStand);
    }

    public void openArmorMenu(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Rüstung", 36);

        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());
        // MENU ITEMS
        inventory.setItem(10, new ItemBuilder(Material.LEATHER_HELMET).setDisplayName("§7Helm").addNBTTag("ArmoStandID_CHECK", armorStand.getUniqueId().toString()).removeAllAttributes().build());
        inventory.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName("§7Brustplatte").removeAllAttributes().build());
        inventory.setItem(12, new ItemBuilder(Material.LEATHER_LEGGINGS).setDisplayName("§7Hose").removeAllAttributes().build());
        inventory.setItem(13, new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName("§7Schuhe").removeAllAttributes().build());

        inventory.setItem(15, new ItemBuilder(Material.STICK).setDisplayName("§7Rechter Arm").removeAllAttributes().build());
        inventory.setItem(16, new ItemBuilder(Material.STICK).setDisplayName("§7Lincker Arm").removeAllAttributes().build());

        // CURRENT CONTENTS
        inventory.setItem(19, armorStand.getItem(EquipmentSlot.HEAD));
        inventory.setItem(20, armorStand.getItem(EquipmentSlot.CHEST));
        inventory.setItem(21, armorStand.getItem(EquipmentSlot.LEGS));
        inventory.setItem(22, armorStand.getItem(EquipmentSlot.FEET));

        inventory.setItem(24, armorStand.getItem(EquipmentSlot.HAND));
        inventory.setItem(25, armorStand.getItem(EquipmentSlot.OFF_HAND));

        inventory.setItem((inventory.getSize() - 9), new ItemBuilder("8790").setDisplayName("§7Zurück").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_MAIN_MENU").build());

        player.openInventory(inventory);
        HyperStand.getInstance().getArmorStandManager().setSelectedArmorStand(player, armorStand);
    }

    public void openSettingsMenu(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Einstellungen", 36);

        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());

        inventory.setItem(11, new ItemBuilder(Material.SMOOTH_STONE_SLAB).setDisplayName("§eBaseplate").addLore("", "§7Aktuell: " + ((armorStand.hasBasePlate()) ? "§aAn" : "§cAus")).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_BASE").build());
        inventory.setItem(12, new ItemBuilder(Material.FEATHER).setDisplayName("§eSchwerkraft").addLore("", "§7Aktuell: " + ((armorStand.hasGravity()) ? "§aAn" : "§cAus")).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_GRAVITY").build());
        inventory.setItem(13, new ItemBuilder(Material.POTION).setDisplayName("§eSichtbarkeit").addLore("", "§7Aktuell: " + ((armorStand.isVisible()) ? "§aAn" : "§cAus")).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_VISIBLE").build());
        inventory.setItem(14, new ItemBuilder(Material.ARROW).setDisplayName("§eArme").addLore("", "§7Aktuell: " + ((armorStand.hasArms()) ? "§aAn" : "§cAus")).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_ARMS").build());
        inventory.setItem(15, new ItemBuilder(Material.NAME_TAG).setDisplayName("§eName").addLore("", "§7Aktuell: " + ((armorStand.getCustomName() == null) ? "§c-" : armorStand.getCustomName())).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_NAME").build());

        inventory.setItem(21, new ItemBuilder(Material.EMERALD).setDisplayName("§eGröße").addLore("", "§7Aktuell: " + ((armorStand.isSmall()) ? "§aKlein" : "§cGroß")).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_SIZE").build());
        inventory.setItem(22, new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayName("§eLeuchten").addLore("", "§7Aktuell: " + ((armorStand.isGlowing()) ? "§aAn" : "§cAus"), "", "§7Farbe: " + ((armorStandManager.getCurrentGlowColor(armorStand) == null) ? "§c-" : Colorizer.hex(armorStandManager.getCurrentGlowColor(armorStand).getDisplayName(), armorStandManager.getCurrentGlowColor(armorStand).getPrettyColor()))).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_GLOW").build());
        inventory.setItem(23, new ItemBuilder(Material.TRIPWIRE_HOOK).setDisplayName("§eAusrüstung sperren").addLore("", "§7Aktuell: " + ((armorStand.hasEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING)) ? "§aAn" : "§cAus")).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "TOGGLE_LOCK").build());

        inventory.setItem((inventory.getSize() - 9), new ItemBuilder("8790").setDisplayName("§7Zurück").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_MAIN_MENU").build());

        player.openInventory(inventory);
        HyperStand.getInstance().getArmorStandManager().setSelectedArmorStand(player, armorStand);
    }

    public void openColorPicker(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Farben", 45);
        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());


        inventory.setItem(10, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_WHITE").getDisplayItemWithID(armorStand));
        inventory.setItem(11, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_LIGHT_GRAY").getDisplayItemWithID(armorStand));
        inventory.setItem(12, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_GRAY").getDisplayItemWithID(armorStand));
        inventory.setItem(13, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_BLACK").getDisplayItemWithID(armorStand));
        inventory.setItem(14, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_RED").getDisplayItemWithID(armorStand));
        inventory.setItem(15, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_ORANGE").getDisplayItemWithID(armorStand));
        inventory.setItem(16, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_YELLOW").getDisplayItemWithID(armorStand));

        inventory.setItem(19, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_LIGHT_GREEN").getDisplayItemWithID(armorStand));
        inventory.setItem(20, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_GREEN").getDisplayItemWithID(armorStand));
        inventory.setItem(21, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_CYAN").getDisplayItemWithID(armorStand));
        inventory.setItem(22, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_LIGHT_BLUE").getDisplayItemWithID(armorStand));
        inventory.setItem(23, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_BLUE").getDisplayItemWithID(armorStand));
        inventory.setItem(24, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_PURPLE").getDisplayItemWithID(armorStand));
        inventory.setItem(25, armorStandManager.getGlowColorByName("HYPERSTAND_GLOW_PINK").getDisplayItemWithID(armorStand));

        inventory.setItem(31, new ItemBuilder(Material.BARRIER).setDisplayName("§cGlow Aus").addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_GLOW_COLOR", "OFF").build());
        inventory.setItem((inventory.getSize() - 9), new ItemBuilder("8790").setDisplayName("§7Zurück").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_SETTINGS_MENU").build());

        player.openInventory(inventory);

    }

    public void openPositionMenu(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Position", 45);

        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());

        inventory.setItem(12, new ItemBuilder(Material.WITHER_SKELETON_SKULL).setDisplayName("§eKopf bewegen").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_HEAD").build());

        inventory.setItem(20, new ItemBuilder(Material.STICK).setDisplayName("§eRechten Arm bewegen").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_RARM").build());
        inventory.setItem(21, new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName("§eKörper bewegen").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_BODY").build());
        inventory.setItem(22, new ItemBuilder(Material.STICK).setDisplayName("§eLinken Arm bewegen").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_LARM").build());

        inventory.setItem(24, new ItemBuilder(Material.PAPER).setDisplayName("§eVorgeferitgte Position aussuchen.").addLore("", "§7Wähle aus §e" + HyperStand.getInstance().getTemplateManager().getTemplateCount() + " §7Vorlagen!").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_TEMPLATE").build());

        inventory.setItem(29, new ItemBuilder(Material.STICK).setDisplayName("§eRechtes Bein bewegen").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_RLEG").build());
        inventory.setItem(30, new ItemBuilder(Material.COMPASS).setDisplayName("§eAufheben").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_PICKUP").build());
        inventory.setItem(31, new ItemBuilder(Material.STICK).setDisplayName("§eLinkes Bein bewegen").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "POSITION_LLEG").build());

        inventory.setItem((inventory.getSize() - 9), new ItemBuilder("8790").setDisplayName("§7Zurück").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_MAIN_MENU").build());
        player.openInventory(inventory);
        HyperStand.getInstance().getArmorStandManager().setSelectedArmorStand(player, armorStand);
    }

    public void openTemplateMenu(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Position", 54);

        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());

        for (int row = 0; row < 4; row++) {
            for (int i = 10; i < 17; i++) {
                inventory.setItem((i + (row * 9)), new ItemStack(Material.AIR));
            }
        }

        for (ArmorStandTemplate template : HyperStand.getInstance().getTemplateManager().getTemplates()) {
            inventory.addItem(new ItemBuilder(Material.PAPER).setDisplayName("§e" + template.getName()).addLore("", "§7" + template.getDescription()).addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("TEMPLATE_NAME", template.getName()).build());
        }

        inventory.setItem((inventory.getSize() - 9), new ItemBuilder("8790").setDisplayName("§7Zurück").removeAllAttributes().addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "OPEN_MAIN_MENU").build());
        player.openInventory(inventory);
    }

    public void openCreateMenu(Player player, ArmorStand armorStand) {
        Inventory inventory = createInventory(player, Colorizer.gradientNormalise("HyperStand", 0.8f, 1, 1) + " §5| Erstelle einen HyperStand", 27);

        fillBackground(inventory, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7").removeAllAttributes().build());

        inventory.setItem(11, new ItemBuilder(Material.GREEN_CONCRETE).setDisplayName("§aUmwandeln").addNBTTag("ArmoStandID", armorStand.getUniqueId().toString()).addNBTTag("HYPERSTAND_ACTION", "CREATE_HYPERSTAND").removeAllAttributes().build());
        inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE).setDisplayName("§cAbbrechen").addNBTTag("HYPERSTAND_ACTION", "CANCLE").removeAllAttributes().build());

        player.openInventory(inventory);
    }
}
