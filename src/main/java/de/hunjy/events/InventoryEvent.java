package de.hunjy.events;

import de.hunjy.HyperStand;
import de.hunjy.armorstand.ArmorStandEditType;
import de.hunjy.armorstand.ArmorStandManager;
import de.hunjy.armorstand.GlowColor;
import de.hunjy.template.ArmorStandTemplate;
import de.hunjy.visual.gui.InventoryHandler;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.UUID;

public class InventoryEvent implements Listener {


    private static final int INV_SLOT_HELMET = 19;
    private static final int INV_SLOT_CHEST = 20;
    private static final int INV_SLOT_LEGS = 21;
    private static final int INV_SLOT_BOOTS = 22;
    private static final int INV_SLOT_MAIN_HAND = 24;
    private static final int INV_SLOT_OFF_HAND = 25;
    private static final HashSet<Integer> invSlots = new HashSet<>();

    private InventoryHandler inventoryHandler;

    private ArmorStandManager armorStandManager;


    public InventoryEvent() {
        armorStandManager = HyperStand.getInstance().getArmorStandManager();
        inventoryHandler = HyperStand.getInstance().getInventoryHandler();

        invSlots.add(INV_SLOT_HELMET);
        invSlots.add(INV_SLOT_CHEST);
        invSlots.add(INV_SLOT_LEGS);
        invSlots.add(INV_SLOT_BOOTS);
        invSlots.add(INV_SLOT_MAIN_HAND);
        invSlots.add(INV_SLOT_OFF_HAND);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null)
            return;

        if (event.getCurrentItem().getType() == Material.AIR)
            return;

        NBTItem item = new NBTItem(event.getCurrentItem());

        if (item.hasTag("GUI_CLICK_CANCEL")) {
            if (item.getBoolean("GUI_CLICK_CANCEL")) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }

        if (item.hasTag("HYPERSTAND_ACTION")) {
            String action = item.getString("HYPERSTAND_ACTION");

            if(action.equals("CANCLE")) {
                player.closeInventory();
                armorStandManager.returnHyperStandItemToPlayer(player);
                return;
            }

            if (!item.hasTag("ArmoStandID")) {
                return;
            }

            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(item.getString("ArmoStandID")));

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 5);
            switch (action) {
                case "CREATE_HYPERSTAND": {
                    player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_CREATET", true));
                    armorStandManager.createHyperStand(player, armorStand);
                    player.closeInventory();
                    break;
                }
                case "CANCLE": {
                    player.closeInventory();
                    armorStandManager.returnHyperStandItemToPlayer(player);
                    break;
                }
                case "OPEN_MAIN_MENU": {
                    inventoryHandler.openMainMenu(player, armorStand);
                    break;
                }
                case "OPEN_ARMOR_MENU": {
                    inventoryHandler.openArmorMenu(player, armorStand);
                    break;
                }
                case "OPEN_SETTINGS_MENU": {
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "OPEN_POSITION_MENU": {
                    inventoryHandler.openPositionMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_BASE": {
                    armorStand.setBasePlate(!armorStand.hasBasePlate());
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_GRAVITY": {
                    armorStand.setGravity(!armorStand.hasGravity());
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_VISIBLE": {
                    armorStand.setVisible(!armorStand.isVisible());
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_ARMS": {
                    armorStand.setArms(!armorStand.hasArms());
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_SIZE": {
                    armorStand.setSmall(!armorStand.isSmall());
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_GLOW": {
                    inventoryHandler.openColorPicker(player, armorStand);
                    break;
                }
                case "TOGGLE_LOCK": {
                    armorStandManager.toggleArmorStandEquipmentLock(armorStand);
                    inventoryHandler.openSettingsMenu(player, armorStand);
                    break;
                }
                case "POSITION_PICKUP": {
                    player.closeInventory();
                    armorStandManager.pickUpArmorStand(player, armorStand);
                    break;
                }
                case "POSITION_HEAD": {
                    player.closeInventory();
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.HEAD);
                    break;
                }
                case "POSITION_RARM": {
                    player.closeInventory();
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.RARM);
                    break;
                }
                case "POSITION_LARM": {
                    player.closeInventory();
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.LARM);
                    break;
                }
                case "POSITION_BODY": {
                    player.closeInventory();
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.BODY);
                    break;
                }
                case "POSITION_RLEG": {
                    player.closeInventory();
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.RLEG);
                    break;
                }
                case "POSITION_LLEG": {
                    player.closeInventory();
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.LLEG);
                    break;
                }
                case "POSITION_TEMPLATE": {
                    inventoryHandler.openTemplateMenu(player, armorStand);
                    break;
                }
            }
        }
        if (item.hasTag("HYPERSTAND_GLOW_COLOR")) {
            String action = item.getString("HYPERSTAND_GLOW_COLOR");
            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(item.getString("ArmoStandID")));
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 5);

            if (action.equals("OFF")) {
                if (scoreboard.getEntityTeam(armorStand) != null) {
                    scoreboard.getEntityTeam(armorStand).removeEntity(armorStand);
                }
                armorStand.setGlowing(false);
                inventoryHandler.openSettingsMenu(player, armorStand);
            } else {
                GlowColor glowColor = armorStandManager.getGlowColorByName(action);
                armorStandManager.setGlowColor(armorStand, glowColor);
                inventoryHandler.openSettingsMenu(player, armorStand);
            }
        }
        if (item.hasTag("TEMPLATE_NAME")) {
            String name = item.getString("TEMPLATE_NAME");
            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(item.getString("ArmoStandID")));
            ArmorStandTemplate armorStandTemplate = HyperStand.getInstance().getTemplateManager().templateByName(player, name);
            if (armorStandTemplate == null) {
                return;
            }
            player.closeInventory();
            armorStandTemplate.assignToArmorStand(armorStand);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inventory.getItem(10) == null) {
            return;
        }

        if (!inventory.getItem(10).hasItemMeta()) {
            return;
        }

        if (!armorStandManager.hasSelectedArmorStand(player)) {
            return;
        }

        NBTItem item = new NBTItem(inventory.getItem(10));

        if (item.hasTag("ArmoStandID_CHECK")) {
            ArmorStand armorStand = armorStandManager.getSelectedArmorStand(player);
            if (armorStand.getUniqueId().toString().equals(item.getString("ArmoStandID_CHECK"))) {
                updateArmorStandInventory(armorStand, inventory);
            }
        }

        armorStandManager.removeSelectedArmorStand(player);
    }


    private void updateArmorStandInventory(ArmorStand armorStand, Inventory inventory) {
        if (armorStand.isDead() || armorStand == null) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                EntityEquipment equipment = armorStand.getEquipment();
                if (inventory == null || equipment == null) return;
                equipment.setItemInMainHand(inventory.getItem(INV_SLOT_MAIN_HAND));
                equipment.setItemInOffHand(inventory.getItem(INV_SLOT_OFF_HAND));
                equipment.setHelmet(inventory.getItem(INV_SLOT_HELMET));
                equipment.setChestplate(inventory.getItem(INV_SLOT_CHEST));
                equipment.setLeggings(inventory.getItem(INV_SLOT_LEGS));
                equipment.setBoots(inventory.getItem(INV_SLOT_BOOTS));
            }
        }.runTaskLaterAsynchronously(HyperStand.getInstance(), 1L);
    }
}
