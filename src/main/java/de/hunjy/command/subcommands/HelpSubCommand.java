package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import de.hunjy.utils.Colorizer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage("§7----------[§x§e§c§2§b§e§7§lH§x§d§c§3§3§e§9§ly§x§c§d§3§c§e§c§lp§x§b§e§4§5§e§e§le§x§a§e§4§e§f§0§lr§x§9§f§5§6§f§2§lS§x§8§f§5§f§f§4§lt§x§8§0§6§8§f§6§la§x§7§1§7§1§f§9§ln§x§6§1§7§9§f§b§ld§7]----------");
        player.sendMessage("");
        player.sendMessage("§7Hier würde die gesamte Hilfe des Plugins stehen!");
        player.sendMessage("");
        player.sendMessage("§7PS: Um es einfacher zu machen, sollte man am beseten einen Forum eintrag erstelleb!");
        player.sendMessage("");
        player.sendMessage("§7----------[§x§e§c§2§b§e§7§lH§x§d§c§3§3§e§9§ly§x§c§d§3§c§e§c§lp§x§b§e§4§5§e§e§le§x§a§e§4§e§f§0§lr§x§9§f§5§6§f§2§lS§x§8§f§5§f§f§4§lt§x§8§0§6§8§f§6§la§x§7§1§7§1§f§9§ln§x§6§1§7§9§f§b§ld§7]----------");

    }

    @Override
    public String getPermission() {
        return "hyperstand.help";
    }

    @Override
    public @NotNull String getAlias() {
        return "help";
    }
}
