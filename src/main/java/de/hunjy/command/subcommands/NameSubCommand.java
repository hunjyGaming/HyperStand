package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NameSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {

        if (args.length <= 1) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_NAME_USAGE", true));
            return;
        }

        String displayName = "";
        for (int i = 1; i < args.length; i++) {
            displayName += args[i] + " ";
        }

        displayName = displayName.substring(0, displayName.length() - 2);

        Entity entity = player.getTargetEntity(5);

        if (entity != null) if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;

            if (HyperStand.getInstance().getArmorStandManager().trySelectArmorStand(player, armorStand)) {
                return;
            }


            HyperStand.getInstance().getArmorStandManager().setDisplayName(armorStand, displayName);
            return;
        }

        player.sendMessage(HyperStand.getInstance().getMessageManager().get("NEED_TO_LOOK_AT_ARMOR_STAND", true));
    }

    @Override
    public String getPermission() {
        return "hyperstand.name";
    }

    @Override
    public @NotNull String getAlias() {
        return "name";
    }
}
