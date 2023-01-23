package de.hunjy.template;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class ArmorStandTemplate {

    String name;
    String description;

    EulerAngle headPosition;
    EulerAngle bodyPosition;
    EulerAngle leftArmPosition;
    EulerAngle rightArmPosition;
    EulerAngle leftLegPosition;
    EulerAngle rightLegPosition;

    YamlConfiguration configuration;

    public ArmorStandTemplate(YamlConfiguration configuration) {
        this.configuration = configuration;

        this.name = configuration.getString("name");
        this.description = configuration.getString("description");

        double tmpX = configuration.getDouble("position.head.X");
        double tmpY = configuration.getDouble("position.head.Y");
        double tmpZ = configuration.getDouble("position.head.Z");
        headPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = configuration.getDouble("position.body.X");
        tmpY = configuration.getDouble("position.body.Y");
        tmpZ = configuration.getDouble("position.body.Z");
        bodyPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = configuration.getDouble("position.leftarm.X");
        tmpY = configuration.getDouble("position.leftarm.Y");
        tmpZ = configuration.getDouble("position.leftarm.Z");
        leftArmPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = configuration.getDouble("position.rightarm.X");
        tmpY = configuration.getDouble("position.rightarm.Y");
        tmpZ = configuration.getDouble("position.rightarm.Z");
        rightArmPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = configuration.getDouble("position.leftleg.X");
        tmpY = configuration.getDouble("position.leftleg.Y");
        tmpZ = configuration.getDouble("position.leftleg.Z");
        leftLegPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = configuration.getDouble("position.rightleg.X");
        tmpY = configuration.getDouble("position.rightleg.Y");
        tmpZ = configuration.getDouble("position.rightleg.Z");
        rightLegPosition = new EulerAngle(tmpX, tmpY, tmpZ);
    }

    public void assignToArmorStand(ArmorStand armorStand) {
        armorStand.setHeadPose(headPosition);
        armorStand.setBodyPose(bodyPosition);
        armorStand.setLeftArmPose(leftArmPosition);
        armorStand.setRightArmPose(rightArmPosition);
        armorStand.setLeftLegPose(leftLegPosition);
        armorStand.setRightLegPose(rightLegPosition);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public EulerAngle getHeadPosition() {
        return headPosition;
    }

    public EulerAngle getBodyPosition() {
        return bodyPosition;
    }

    public EulerAngle getLeftArmPosition() {
        return leftArmPosition;
    }

    public EulerAngle getRightArmPosition() {
        return rightArmPosition;
    }

    public EulerAngle getLeftLegPosition() {
        return leftLegPosition;
    }

    public EulerAngle getRightLegPosition() {
        return rightLegPosition;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
