package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import de.hunjy.template.PlayerTemplate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {

        if (!HyperStand.getInstance().getMySQLConnection().isConnected()) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("MYSQL_ERROR"));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_SAVE_USAGE", true));
            return;
        }

        String name = args[1];

        PlayerTemplate.remove(player, name);
    }

    @Override
    public String getDescription() {
        return "Lösche eine gespeicherte Position";
    }

    @Override
    public String getPermission() {
        return "hyperstand.template";
    }

    @Override
    public @NotNull String getAlias() {
        return "remove";
    }
}
