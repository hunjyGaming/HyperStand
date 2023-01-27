package de.hunjy.events;

import de.hunjy.HyperStand;
import de.hunjy.armorstand.ArmorStandEditType;
import de.hunjy.armorstand.ArmorStandManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class EditorEvents implements Listener {

    ArmorStandManager armorStandManager = HyperStand.getInstance().getArmorStandManager();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();


        if (armorStandManager.isEditing(player)) {
            ArmorStand armorStand = armorStandManager.getSelectedArmorStand(player);
            ArmorStandEditType armorStandEditType = armorStandManager.getCurrentEditType(player);
            armorStandEditType.modify(player, armorStand);
        }
    }

    @EventHandler
    public void onArmoStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (armorStandManager.armorStandIsInUse(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (armorStandManager.isEditing(player)) {
                if (!event.isCancelled()) {
                    event.setCancelled(true);
                    armorStandManager.finishEditing(player);
                } else {
                    player.sendMessage(HyperStand.getInstance().getMessageManager().get("CANT_PLACE_HERE"));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (armorStandManager.isEditing(player)) {
            armorStandManager.returnArmorStand(player);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (armorStandManager.isEditing(player)) {
            armorStandManager.returnArmorStand(player);
        }
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ArmorStand)) return;

        ArmorStand armorStand = (ArmorStand) event.getEntity();

        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();


        if (armorStandManager.armorStandIsInUse(armorStand)) {
            if (armorStandManager.hasSelectedArmorStand(player)) {
                if (armorStandManager.getSelectedArmorStand(player) == armorStand) {
                    if (armorStandManager.isEditing(player)) {
                        if (!event.isCancelled()) {
                            event.setCancelled(true);
                            armorStandManager.finishEditing(player);
                        } else {
                            player.sendMessage(HyperStand.getInstance().getMessageManager().get("CANT_PLACE_HERE"));
                        }
                    }
                    return;
                }
            }
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("HYPERSTAND_ALREADY_IN_USE", true));
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInteract(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (armorStandManager.isEditing(player)) {
            if (player.isSneaking()) {
                ArmorStand armorStand = armorStandManager.getSelectedArmorStand(player);

                if (armorStand.hasGravity()) {
                    armorStand.setGlowing(false);
                }

                Location location = armorStand.getLocation().clone();
                if (event.getNewSlot() < event.getPreviousSlot()) {
                    location.add(0, 0.1, 0);
                } else {
                    location.subtract(0, 0.1, 0);
                }

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 5);
                armorStand.teleport(location);

                player.getInventory().setHeldItemSlot(4);
                event.setCancelled(true);
            }
        }
    }

}
