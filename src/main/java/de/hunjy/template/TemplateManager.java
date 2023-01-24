package de.hunjy.template;

import de.hunjy.HyperStand;
import de.hunjy.mysql.MySQLConnection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class TemplateManager {

    private File folder;
    private File file;
    private YamlConfiguration configuration;

    private HashMap<UUID, HashSet<ArmorStandTemplate>> playerTemplates;

    private MySQLConnection mysql;

    public TemplateManager() {
        playerTemplates = new HashMap<>();
        folder = new File(HyperStand.getInstance().getDataFolder().getPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        file = new File(folder, "config.yml");
        configuration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            configuration.set("mysql.host", "localhost");
            configuration.set("mysql.name", "user");
            configuration.set("mysql.password", "password");
            configuration.set("mysql.database", "database");
            try {
                configuration.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);

        mysql = new MySQLConnection(configuration.getString("mysql.host"), configuration.getString("mysql.name"), configuration.getString("mysql.password"), configuration.getString("mysql.database"));
        mysql.query("CREATE TABLE IF NOT EXISTS hyperstand (" +
                " UUID VARCHAR(60) NOT NULL," +
                " name VARCHAR(60) NOT NULL," +
                " rawData LONGTEXT NOT NULL," +
                " description VARCHAR(255) NOT NULL," +
                " primary key (UUID, name)" +
                ")");
    }

    public HashSet<ArmorStandTemplate> getTemplates(Player player) {
        if (!playerTemplates.containsKey(player.getUniqueId())) {
            try {
                playerTemplates.put(player.getUniqueId(), loadPlayerTemplates(player));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return playerTemplates.get(player.getUniqueId());
    }

    public HashSet<ArmorStandTemplate> loadPlayerTemplates(Player player) throws SQLException {
        ResultSet resultSet = mysql.getResult("SELECT * FROM hyperstand WHERE UUID='" + player.getUniqueId() + "';");
        HashSet<ArmorStandTemplate> templates = new HashSet<>();

        if(resultSet == null) {
            return templates;
        }

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String rawData = resultSet.getString("rawData");
            String description = resultSet.getString("description");
            templates.add(new ArmorStandTemplate(name, new String(Base64.getDecoder().decode(rawData.getBytes())), description));
        }
        resultSet.close();
        return templates;
    }

    public ArmorStandTemplate templateByName(Player player, String name) {
        for (ArmorStandTemplate template : playerTemplates.get(player.getUniqueId())) {
            if (template.getName().equals(name)) {
                return template;
            }
        }
        return null;
    }

    public int getTemplateCount(Player player) {
        return playerTemplates.get(player.getUniqueId()).size();
    }

    public void saveTemplate(Player player, String name, String description, ArmorStand armorStand) {
        player.sendMessage(HyperStand.getInstance().getMessageManager().get("create_template", true));
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(armorStand.getHeadPose().getX());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getHeadPose().getY());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getHeadPose().getZ());
        stringBuilder.append(";");

        stringBuilder.append(armorStand.getBodyPose().getX());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getBodyPose().getY());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getBodyPose().getZ());
        stringBuilder.append(";");

        stringBuilder.append(armorStand.getLeftArmPose().getX());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getLeftArmPose().getY());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getLeftArmPose().getZ());
        stringBuilder.append(";");

        stringBuilder.append(armorStand.getRightArmPose().getX());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getRightArmPose().getY());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getRightArmPose().getZ());
        stringBuilder.append(";");

        stringBuilder.append(armorStand.getLeftLegPose().getX());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getLeftLegPose().getY());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getLeftLegPose().getZ());
        stringBuilder.append(";");

        stringBuilder.append(armorStand.getRightLegPose().getX());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getRightLegPose().getY());
        stringBuilder.append(";");
        stringBuilder.append(armorStand.getRightLegPose().getZ());

        String data = Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes());
        mysql.query("INSERT INTO hyperstand(UUID, name, rawData, description) VALUES ('" + player.getUniqueId().toString() + "','" + name + "','" + data + "','" + description + "')");
        if(!playerTemplates.containsKey(player.getUniqueId())) {
            getTemplates(player);
        }
        playerTemplates.get(player.getUniqueId()).add(new ArmorStandTemplate(name, stringBuilder.toString(), description));
        player.sendMessage(HyperStand.getInstance().getMessageManager().get("create_template_success", true));
    }


}
