package de.hunjy;

import de.hunjy.armorstand.ArmorStandManager;
import de.hunjy.command.HyperStandCommand;
import de.hunjy.command.subcommands.*;
import de.hunjy.events.*;
import de.hunjy.logger.ILogger;
import de.hunjy.messages.MessageManager;
import de.hunjy.template.TemplateManager;
import de.hunjy.visual.gui.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyperStand extends JavaPlugin {

    private static HyperStand instance;

    private MessageManager messageManager;
    private InventoryHandler inventoryHandler;
    private ArmorStandManager armorStandManager;
    private TemplateManager templateManager;

    @Override
    public void onEnable() {
        instance = this;

        ILogger.log("Enabiling plugin....");

        messageManager = new MessageManager();
        armorStandManager = new ArmorStandManager();
        inventoryHandler = new InventoryHandler();
        templateManager = new TemplateManager();

        registerCommands();
        registerEvents();
        ILogger.log("Plugin enabeld!");
    }

    @Override
    public void onDisable() {

    }

    // REGISTER ALL COMMANDS
    public void registerCommands() {
        getCommand("hyperstand").setExecutor(new HyperStandCommand());
        getCommand("hyperstand").setTabCompleter(new HyperStandCommand());
        HyperStandCommand.registerSubCommand(new HelpSubCommand());
        HyperStandCommand.registerSubCommand(new EditSubCommand());
        HyperStandCommand.registerSubCommand(new SaveSubCommand());
        HyperStandCommand.registerSubCommand(new NameSubCommand());
        HyperStandCommand.registerSubCommand(new CreateSubCommand());
    }

    public void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerInteractAtArmorStandEvent(), this);
        pluginManager.registerEvents(new InventoryEvent(), this);
        pluginManager.registerEvents(new EditorEvents(), this);

    }

    public static HyperStand getInstance() {
        return instance;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ArmorStandManager getArmorStandManager() {
        return armorStandManager;
    }

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

}
