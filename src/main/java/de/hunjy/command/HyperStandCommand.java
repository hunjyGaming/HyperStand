package de.hunjy.command;

import de.hunjy.HyperStand;
import de.hunjy.mysql.ArmorstandQueryListener;
import de.hunjy.template.ArmorStandTemplate;
import de.hunjy.template.PlayerTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperStandCommand implements CommandExecutor, TabCompleter {

    private static Map<String, SubCommand> commands = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_DEFAULT_USAGE", true));
            return false;
        }

        String rawSubCommand = args[0].toLowerCase();

        if (!commands.containsKey(rawSubCommand)) {
            player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_DEFAULT_USAGE", true));
            return false;
        }
        SubCommand subCommand = commands.get(rawSubCommand);

        if (subCommand.getPermission() != null) {
            if (!player.hasPermission(subCommand.getPermission())) {
                player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_NO_PERMISSIONS", true));
                return false;
            }
        }

        subCommand.onCommand(player, args);
        return true;
    }

    public static Map<String, SubCommand> getCommands() {
        return commands;
    }

    public static void registerSubCommand(SubCommand subCommand) {
        commands.put(subCommand.getAlias(), subCommand);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("hyperstand") && args.length >= 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length == 1) {
                    for (String cmd : commands.keySet()) {
                        if (player.hasPermission(commands.get(cmd).getPermission())) {
                            list.add(cmd);
                        }
                    }
                }

                if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                    PlayerTemplate.get(player, new ArmorstandQueryListener() {
                        @Override
                        public void onQueryResult(List<ArmorStandTemplate> templates) {
                            for(ArmorStandTemplate template : templates) {
                                list.add(template.getName());
                            }
                        }

                        @Override
                        public void onQueryError(Exception exception) {
                            onQueryError(exception);
                        }
                    });
                }

            }
        }
        return list;
    }
}
