package de.hunjy.armorstand;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public enum ArmorStandEditType {

    PICK_UP,
    HEAD,
    LARM,
    RARM,
    MOVE,
    LLEG,
    RLEG,
    BODY;

    public void modify(Player player, ArmorStand armorStand) {

        if (this == PICK_UP) {
            if (!player.isSneaking()) {
                armorStand.teleport(getLocationFacing(player.getLocation()));
                return;
            }
        }

        EulerAngle eulerAngle = switch (this) {
            case HEAD -> armorStand.getHeadPose();
            case BODY -> armorStand.getBodyPose();
            case LARM -> armorStand.getLeftArmPose();
            case RARM -> armorStand.getRightArmPose();
            case LLEG -> armorStand.getLeftLegPose();
            case RLEG -> armorStand.getRightLegPose();
            default -> null;
        };
        if (eulerAngle == null) return;
        eulerAngle = eulerAngle.setX(getPitch(player));
        double yaw = getRelativeYaw(player, armorStand);
        eulerAngle = player.isSneaking() ? eulerAngle.setZ(yaw) : eulerAngle.setY(yaw);
        switch (this) {
            case HEAD -> armorStand.setHeadPose(eulerAngle);
            case BODY -> armorStand.setBodyPose(eulerAngle);
            case LARM -> armorStand.setLeftArmPose(eulerAngle);
            case RARM -> armorStand.setRightArmPose(eulerAngle);
            case LLEG -> armorStand.setLeftLegPose(eulerAngle);
            case RLEG -> armorStand.setRightLegPose(eulerAngle);
        }
    }

    private double getPitch(Player p) {
        double pitch = p.getLocation().getPitch() * 4;
        while (pitch < 0) {
            pitch += 360;
        }
        while (pitch > 360) {
            pitch -= 360;
        }
        return pitch * Math.PI / 180.0;
    }

    private double getRelativeYaw(Player p, ArmorStand as) {
        double difference = p.getLocation().getYaw() - as.getLocation().getYaw();
        double yaw = 360.0 - (difference * 2);
        while (yaw < 0) {
            yaw += 360;
        }
        while (yaw > 360) {
            yaw -= 360;
        }
        return yaw * Math.PI / 180.0;
    }


    Location getLocationFacing(Location loc) {
        Location l = loc.clone();
        Vector v = l.getDirection();
        v.multiply(3);
        l.add(v);
        l.setYaw(l.getYaw() + 180);
        int n;
        boolean ok = false;
        for (n = 0; n < 5; n++) {
            if (l.getBlock().getType().isSolid()) {
                l.add(0, 1, 0);
            } else {
                ok = true;
                break;
            }
        }
        if (!ok) {
            l.subtract(0, 5, 0);
        }
        return l;
    }
}
