package de.hunjy.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SubCommand {

    void onCommand(Player player, String[] args);

    String getPermission();

    @NotNull
    String getAlias();
}
