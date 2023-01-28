package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import org.bukkit.Bukkit;
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

        Entity entity = player.getTargetEntity(5);
        if (entity != null) if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;

            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("entfrenen")) {
                    armorStand.setCustomName(null);
                    armorStand.setCustomNameVisible(false);
                    return;
                }
            }
            String displayName = "";

            for (int i = 1; i < args.length; i++) {
                displayName += args[i] + " ";
            }

            displayName = displayName.substring(0, displayName.length() - 1);
            displayName = displayName.replace("&", "§");

            String rawName = ChatColor.stripColor(displayName);

            if(rawName.length() > 30) {
                player.sendMessage(HyperStand.getInstance().getMessageManager().get("NAME_TO_LONG", true));
                return;
            }

            if (HyperStand.getInstance().getArmorStandManager().trySelectArmorStand(player, armorStand)) {
                return;
            }


            HyperStand.getInstance().getArmorStandManager().setDisplayName(armorStand, displayName);
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("NAME_SET", true, displayName));
            return;
        }

        player.sendMessage(HyperStand.getInstance().getMessageManager().get("NEED_TO_LOOK_AT_ARMOR_STAND", true));
    }

    @Override
    public String getDescription() {
        return "Gebe deinem HyperStand einen Namen";
    }

    @Override
    public String getPermission() {
        return "hyperstand.edit";
    }

    @Override
    public @NotNull String getAlias() {
        return "name";
    }
}
