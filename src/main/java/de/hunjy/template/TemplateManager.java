package de.hunjy.template;

import de.hunjy.HyperStand;
import de.hunjy.logger.ILogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class TemplateManager {

    private File folder;


    private HashSet<File> templateFiles;
    private HashSet<ArmorStandTemplate> templates;

    public TemplateManager() {
        folder = new File(HyperStand.getInstance().getDataFolder().getPath() + "//templates");
        if (!folder.exists())
            folder.mkdirs();
        templateFiles = new HashSet<>();


        for (File file : folder.listFiles()) {
            if (file.isDirectory())
                continue;

            if (!file.getName().endsWith(".yml"))
                continue;

            templateFiles.add(file);
        }

        loadTemplates();
    }

    public void loadTemplates() {
        ILogger.log("Loading ArmorStand Templates....");
        templates = new HashSet<>();
        for (File file : templateFiles) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            templates.add(new ArmorStandTemplate(configuration));
        }
        ILogger.log("Successfully loaded [" + getTemplateCount() + "] ArmorStand templates.");
    }

    public ArmorStandTemplate templateByName(String name) {
        for (ArmorStandTemplate template : templates) {
            if (template.getName().equals(name)) {
                return template;
            }
        }
        return null;
    }

    public int getTemplateCount() {
        return templates.size();
    }


    public HashSet<ArmorStandTemplate> getTemplates() {
        return templates;
    }

    public boolean saveTemplate(String name, String description, ArmorStand armorStand) {
        File file = new File(folder, name.toLowerCase().replace(" ", "_") + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        configuration.set("name", name);
        configuration.set("description", description);

        configuration.set("position.head.X", armorStand.getHeadPose().getX());
        configuration.set("position.head.Y", armorStand.getHeadPose().getY());
        configuration.set("position.head.Z", armorStand.getHeadPose().getZ());

        configuration.set("position.body.X", armorStand.getBodyPose().getX());
        configuration.set("position.body.Y", armorStand.getBodyPose().getY());
        configuration.set("position.body.Z", armorStand.getBodyPose().getZ());

        configuration.set("position.leftarm.X", armorStand.getLeftArmPose().getX());
        configuration.set("position.leftarm.Y", armorStand.getLeftArmPose().getY());
        configuration.set("position.leftarm.Z", armorStand.getLeftArmPose().getZ());

        configuration.set("position.rightarm.X", armorStand.getRightArmPose().getX());
        configuration.set("position.rightarm.Y", armorStand.getRightArmPose().getY());
        configuration.set("position.rightarm.Z", armorStand.getRightArmPose().getZ());

        configuration.set("position.leftleg.X", armorStand.getLeftLegPose().getX());
        configuration.set("position.leftleg.Y", armorStand.getLeftLegPose().getY());
        configuration.set("position.leftleg.Z", armorStand.getLeftLegPose().getZ());

        configuration.set("position.rightleg.X", armorStand.getRightLegPose().getX());
        configuration.set("position.rightleg.Y", armorStand.getRightLegPose().getY());
        configuration.set("position.rightleg.Z", armorStand.getRightLegPose().getZ());

        try {
            configuration.save(file);

            templates.add(new ArmorStandTemplate(configuration));

            return true;
        } catch (IOException e) {
            return false;
        }

    }


}
