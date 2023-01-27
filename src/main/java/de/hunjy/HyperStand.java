package de.hunjy;

import de.hunjy.armorstand.ArmorStandManager;
import de.hunjy.command.HyperStandCommand;
import de.hunjy.command.subcommands.*;
import de.hunjy.events.EditorEvents;
import de.hunjy.events.InventoryEvent;
import de.hunjy.events.PlayerInteractAtArmorStandEvent;
import de.hunjy.logger.ILogger;
import de.hunjy.messages.MessageManager;
import de.hunjy.mysql.MySQLConnection;
import de.hunjy.visual.gui.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.util.UUID;

public final class HyperStand extends JavaPlugin {

    private static HyperStand instance;

    private MySQLConnection mySQLConnection;

    private MessageManager messageManager;
    private InventoryHandler inventoryHandler;
    private ArmorStandManager armorStandManager;

    @Override
    public void onEnable() {
        instance = this;
        ILogger.log("Enabiling plugin....");
        connectToMySQL();
        messageManager = new MessageManager();
        armorStandManager = new ArmorStandManager();
        inventoryHandler = new InventoryHandler();

        registerCommands();
        registerEvents();
        ILogger.log("Plugin enabeld!");
    }

    @Override
    public void onDisable() {
        for (UUID uuid : armorStandManager.getSelectedArmorStand().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.closeInventory();
            armorStandManager.returnArmorStand(player);
        }

        mySQLConnection.close();
    }

    private void connectToMySQL() {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        FileConfiguration configuration = this.getConfig();
        String host = configuration.getString("mysql.host");
        String name = configuration.getString("mysql.name");
        String password = configuration.getString("mysql.password");
        String database = configuration.getString("mysql.database");

        mySQLConnection = new MySQLConnection(host, name, password, database);
        mySQLConnection.query("CREATE TABLE IF NOT EXISTS hyperstand (" +
                " UUID VARCHAR(60) NOT NULL," +
                " name VARCHAR(60) NOT NULL," +
                " rawData LONGTEXT NOT NULL," +
                " description VARCHAR(255) NOT NULL," +
                " primary key (UUID, name)" +
                ")");
    }

    // REGISTER ALL COMMANDS
    public void registerCommands() {
        getCommand("hyperstand").setExecutor(new HyperStandCommand());
        getCommand("hyperstand").setTabCompleter(new HyperStandCommand());
        HyperStandCommand.registerSubCommand(new HelpSubCommand());
        HyperStandCommand.registerSubCommand(new EditSubCommand());
        HyperStandCommand.registerSubCommand(new SaveSubCommand());
        HyperStandCommand.registerSubCommand(new NameSubCommand());
        HyperStandCommand.registerSubCommand(new ArmorSubCommand());
        HyperStandCommand.registerSubCommand(new CreateSubCommand());
        HyperStandCommand.registerSubCommand(new RemoveSubCommand());
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

    public MySQLConnection getMySQLConnection() {
        return mySQLConnection;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ArmorStandManager getArmorStandManager() {
        return armorStandManager;
    }

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

}
