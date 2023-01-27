package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArmorSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
        Entity entity = player.getTargetEntity(5);

        if (entity != null) if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;

            if(HyperStand.getInstance().getArmorStandManager().trySelectArmorStand(player, armorStand)) {
                return;
            }

            HyperStand.getInstance().getInventoryHandler().openArmorMenu(player, armorStand);
            return;
        }

        player.sendMessage(HyperStand.getInstance().getMessageManager().get("NEED_TO_LOOK_AT_ARMOR_STAND", true));
    }

    @Override
    public String getDescription() {
        return "Bearbeite die Rüstungdes HyperStand";
    }
    @Override
    public String getPermission() {
        return "hyperstand.edit";
    }

    @Override
    public @NotNull String getAlias() {
        return "armor";
    }
}
