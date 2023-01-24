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

    public ArmorStandTemplate(String name, String data, String description) {
        this.name = name;
        this.description = description;
        String[] configuration = data.split(";");

        int i = 0;
        double tmpX = Double.parseDouble(configuration[i++]);
        double tmpY = Double.parseDouble(configuration[i++]);
        double tmpZ = Double.parseDouble(configuration[i++]);
        headPosition = new EulerAngle(tmpX, tmpY, tmpZ);


        tmpX = Double.parseDouble(configuration[i++]);
        tmpY = Double.parseDouble(configuration[i++]);
        tmpZ = Double.parseDouble(configuration[i++]);
        bodyPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = Double.parseDouble(configuration[i++]);
        tmpY = Double.parseDouble(configuration[i++]);
        tmpZ = Double.parseDouble(configuration[i++]);
        leftArmPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = Double.parseDouble(configuration[i++]);
        tmpY = Double.parseDouble(configuration[i++]);
        tmpZ = Double.parseDouble(configuration[i++]);
        rightArmPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = Double.parseDouble(configuration[i++]);
        tmpY = Double.parseDouble(configuration[i++]);
        tmpZ = Double.parseDouble(configuration[i++]);
        leftLegPosition = new EulerAngle(tmpX, tmpY, tmpZ);

        tmpX = Double.parseDouble(configuration[i++]);
        tmpY = Double.parseDouble(configuration[i++]);
        tmpZ = Double.parseDouble(configuration[i]);
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

}
