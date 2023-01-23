package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaveSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
        Entity entity = player.getTargetEntity(5);

        if (entity != null) if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            if (args.length <= 2) {
                player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_SAVE_USAGE", true));
                return;
            }

            String name = args[1];
            String description = "";

            for (int i = 2; i < args.length; i++) {
                description += args[i] + " ";
            }

            description = description.substring(0,description.length() - 2);

            boolean saved = HyperStand.getInstance().getTemplateManager().saveTemplate(name, description, armorStand);

            return;
        }

        player.sendMessage(HyperStand.getInstance().getMessageManager().get("NEED_TO_LOOK_AT_ARMOR_STAND", true));
    }

    @Override
    public String getPermission() {
        return "hyperstand.admin.save";
    }

    @Override
    public @NotNull String getAlias() {
        return "save";
    }
}
