package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import de.hunjy.template.PlayerTemplate;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaveSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
        if(!HyperStand.getInstance().getMySQLConnection().isConnected()) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("MYSQL_ERROR"));
            return;
        }

        Entity entity = player.getTargetEntity(5);

        if (entity != null) if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            if (args.length <= 1) {
                player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_SAVE_USAGE"));
                return;
            }

            String name = args[1];
            String description = "";

            if (args.length >= 3) {
                for (int i = 2; i < args.length; i++) {
                    description += args[i] + " ";
                }

                description = description.substring(0, description.length() - 1);
            }

            PlayerTemplate.trySaveTemplate(player, name, description, armorStand);

            return;
        }

        player.sendMessage(HyperStand.getInstance().getMessageManager().get("NEED_TO_LOOK_AT_ARMOR_STAND"));
    }

    @Override
    public String getPermission() {
        return "hyperstand.template";
    }

    @Override
    public String getDescription() {
        return "Speichere eine Position um diese immer weider benutzen zu können";
    }
    @Override
    public @NotNull String getAlias() {
        return "save";
    }
}
