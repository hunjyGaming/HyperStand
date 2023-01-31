package de.hunjy.events;

import de.hunjy.HyperStand;
import de.hunjy.armorstand.ArmorStandEditType;
import de.hunjy.armorstand.ArmorStandManager;
import de.hunjy.armorstand.GlowColor;
import de.hunjy.mysql.ArmorstandQueryListener;
import de.hunjy.template.ArmorStandTemplate;
import de.hunjy.template.PlayerTemplate;
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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.List;
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


        if (armorStandManager.getCurrentEditType(player) == ArmorStandEditType.ARMOR) {
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }
            if (event.getCursor() != null) {
                if (event.getCursor().getType() != Material.AIR) {
                    if (armorStandManager.isEditing(player)) {
                        String mat = event.getCursor().toString();
                        if(event.getClickedInventory() == player.getInventory()){
                            return;
                        }
                        if (event.getSlot() == INV_SLOT_CHEST) {
                            if (!mat.contains("_CHESTPLATE")) {
                                event.setCancelled(true);
                                event.setResult(Event.Result.DENY);
                            }
                        }
                        if (event.getSlot() == INV_SLOT_LEGS) {
                            if (!mat.contains("_LEGGINGS")) {
                                event.setCancelled(true);
                                event.setResult(Event.Result.DENY);
                            }
                        }

                        if (event.getSlot() == INV_SLOT_BOOTS) {
                            if (!mat.contains("_BOOTS")) {
                                event.setCancelled(true);
                                event.setResult(Event.Result.DENY);
                            }
                        }
                    }
                }
            }
        }

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

            if (action.equals("CANCLE")) {
                armorStandManager.returnHyperStandItemToPlayer(player);
                player.closeInventory();
                return;
            }

            if (!item.hasTag("ArmoStandID")) {
                return;
            }
            InventoryAction inventoryAction = event.getAction();

            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(item.getString("ArmoStandID")));

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 5);
            switch (action) {
                case "CREATE_HYPERSTAND": {
                    player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_CREATET", true));
                    armorStandManager.removeHyperStandItemFromCahce(player);
                    armorStandManager.createHyperStand(player, armorStand);
                    player.closeInventory();
                    break;
                }
                case "OPEN_MAIN_MENU": {
                    inventoryHandler.openMainMenu(player, armorStand);
                    break;
                }
                case "OPEN_ARMOR_MENU": {
                    armorStandManager.startEdit(player, armorStand, ArmorStandEditType.ARMOR);
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
                    if (inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        player.closeInventory();
                        armorStandManager.startEdit(player, armorStand, ArmorStandEditType.HEAD_X);
                        break;
                    }
                    armorStandManager.resetToDefault(ArmorStandEditType.HEAD, armorStand);
                    closePlayerInventory(player, 1);
                    break;

                }
                case "POSITION_RARM": {
                    if (inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        player.closeInventory();
                        armorStandManager.startEdit(player, armorStand, ArmorStandEditType.RARM_X);
                        break;
                    }
                    armorStandManager.resetToDefault(ArmorStandEditType.RARM, armorStand);
                    closePlayerInventory(player, 1);
                    break;
                }
                case "POSITION_LARM": {
                    if (inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        player.closeInventory();
                        armorStandManager.startEdit(player, armorStand, ArmorStandEditType.LARM_X);
                        break;
                    }
                    armorStandManager.resetToDefault(ArmorStandEditType.LARM, armorStand);
                    closePlayerInventory(player, 1);
                    break;
                }
                case "POSITION_BODY": {
                    if (inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        player.closeInventory();
                        armorStandManager.startEdit(player, armorStand, ArmorStandEditType.BODY_X);
                        break;
                    }
                    armorStandManager.resetToDefault(ArmorStandEditType.BODY, armorStand);
                    closePlayerInventory(player, 1);
                    break;
                }
                case "POSITION_RLEG": {
                    if (inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        player.closeInventory();
                        armorStandManager.startEdit(player, armorStand, ArmorStandEditType.RLEG_X);
                        break;
                    }
                    armorStandManager.resetToDefault(ArmorStandEditType.RLEG, armorStand);
                    closePlayerInventory(player, 1);
                    break;
                }
                case "POSITION_LLEG": {
                    if (inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        player.closeInventory();
                        armorStandManager.startEdit(player, armorStand, ArmorStandEditType.LLEG_X);
                        break;
                    }
                    armorStandManager.resetToDefault(ArmorStandEditType.LLEG, armorStand);
                    closePlayerInventory(player, 1);
                    break;
                }
                case "POSITION_TEMPLATE": {
                    inventoryHandler.openTemplateMenu(player, armorStand);
                    break;
                }
                case "TOGGLE_NAME": {
                    player.closeInventory();
                    player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPSERSTAND_SET_NAME", true));
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


            PlayerTemplate.get(player, new ArmorstandQueryListener() {
                @Override
                public void onQueryResult(List<ArmorStandTemplate> templates) {
                    Bukkit.getScheduler().runTask(HyperStand.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (ArmorStandTemplate template : templates) {
                                if (template.getName().equals(name)) {
                                    player.closeInventory();
                                    template.assignToArmorStand(armorStand);
                                    return;
                                }
                            }

                        }
                    });
                }

                @Override
                public void onQueryError(Exception exception) {
                    onQueryError(exception);
                }
            });
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if (armorStandManager.isInItemCache(player)) {
            armorStandManager.returnHyperStandItemToPlayer(player);
            return;
        }

        if (!armorStandManager.hasSelectedArmorStand(player)) {
            return;
        }

        ArmorStand armorStand = armorStandManager.getSelectedArmorStand(player);

        if (armorStandManager.getCurrentEditType(player) != ArmorStandEditType.MOVE) {
            armorStandManager.removeSelectedArmorStand(player);
        }


        if (inventory.getItem(10) == null) {
            return;
        }

        if (!inventory.getItem(10).hasItemMeta()) {
            return;
        }
        NBTItem item = new NBTItem(inventory.getItem(10));

        if (item.hasTag("ArmoStandID_CHECK")) {
            if (armorStand.getUniqueId().toString().equals(item.getString("ArmoStandID_CHECK"))) {
                updateArmorStandInventory(armorStand, inventory);
                armorStandManager.finishEditing(player);
            }
        }


    }


    public void closePlayerInventory(Player player, int delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                this.cancel();
            }
        }.runTaskLater(HyperStand.getInstance(), delay);
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
