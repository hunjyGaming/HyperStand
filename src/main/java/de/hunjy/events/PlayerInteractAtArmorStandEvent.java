package de.hunjy.events;

import de.hunjy.HyperStand;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtArmorStandEvent implements Listener {

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof ArmorStand)) {
            return;
        }

        ArmorStand armorStand = (ArmorStand) entity;

        if(HyperStand.getInstance().getArmorStandManager().armorStandIsInUse(armorStand)) {
            if (HyperStand.getInstance().getArmorStandManager().hasSelectedArmorStand(player)) {
                if (HyperStand.getInstance().getArmorStandManager().getSelectedArmorStand(player) != armorStand) {
                    player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_ALREADY_IN_USE", true));
                    return;
                }
            }else {
                player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_ALREADY_IN_USE", true));
                return;
            }
        }

        //CREATE HYPERSTAND WITH ITEM
        if (player.getInventory().getItemInMainHand() != null) {
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                NBTItem item = new NBTItem(player.getInventory().getItemInMainHand());
                if (item.hasTag("hyperstand")) {
                    if (HyperStand.getInstance().getArmorStandManager().isEdibleArmorStand(armorStand)) {
                        player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_ALLRADY_EXIST", true));
                        event.setCancelled(true);
                        return;
                    }
                    HyperStand.getInstance().getArmorStandManager().useHyperStandItem(player, item.getItem());
                    HyperStand.getInstance().getInventoryHandler().openCreateMenu(player, armorStand);
                    return;
                }
            }

        }


        if (!player.isSneaking()) {
            return;
        }

        if (HyperStand.getInstance().getArmorStandManager().trySelectArmorStand(player, armorStand)) {
            return;
        }

        event.setCancelled(true);
        HyperStand.getInstance().getInventoryHandler().openMainMenu(player, armorStand);
    }

}
