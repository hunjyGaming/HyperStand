package de.hunjy.command.subcommands;

import de.hunjy.HyperStand;
import de.hunjy.command.SubCommand;
import de.hunjy.visual.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CreateSubCommand implements SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {

        ItemStack itemStack = new ItemBuilder(Material.ARMOR_STAND)
                .setDisplayName("§x§e§c§2§b§e§7§lH§x§d§c§3§3§e§9§ly§x§c§d§3§c§e§c§lp§x§b§e§4§5§e§e§le§x§a§e§4§e§f§0§lr§x§9§f§5§6§f§2§lS§x§8§f§5§f§f§4§lt§x§8§0§6§8§f§6§la§x§7§1§7§1§f§9§ln§x§6§1§7§9§f§b§ld")
                .removeAllAttributes()
                .addNBTTag("hyperstand", "true")
                .addNBTTag("ID", UUID.randomUUID().toString())
                .setCancelClick(false)
                .build();

        player.sendMessage(HyperStand.getInstance().getMessageManager().get("COMMAND_CREATE_ITEM_GIVEN"));
        player.getInventory().addItem(itemStack);

    }

    @Override
    public String getDescription() {
        return null;
    }
    @Override
    public String getPermission() {
        return "hyperstand.admin.create";
    }

    @Override
    public @NotNull String getAlias() {
        return "create";
    }
}
