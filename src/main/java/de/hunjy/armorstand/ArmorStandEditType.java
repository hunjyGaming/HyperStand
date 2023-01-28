package de.hunjy.armorstand;

import de.hunjy.HyperStand;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public enum ArmorStandEditType {

    HEAD,
    HEAD_X,
    HEAD_Y,
    HEAD_Z,
    LARM,
    LARM_X,
    LARM_Y,
    LARM_Z,
    RARM,
    RARM_X,
    RARM_Y,
    RARM_Z,
    MOVE,
    LLEG,
    LLEG_X,
    LLEG_Y,
    LLEG_Z,
    RLEG,
    RLEG_X,
    RLEG_Y,
    RLEG_Z,
    BODY,
    BODY_X,
    BODY_Y,
    BODY_Z,
    ARMOR;

    public void modify(Player player, ArmorStand armorStand) {

        if (player.isDead() || !player.isOnline()) {
            HyperStand.getInstance().getArmorStandManager().finishEditing(player);
            return;
        }

        if (this == MOVE) {
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

    public void modify(Player player, ArmorStand armorStand, double value) {

        if (player.isDead() || !player.isOnline()) {
            HyperStand.getInstance().getArmorStandManager().finishEditing(player);
            return;
        }
        String pos = this.toString().split("_")[0];

        EulerAngle eulerAngle = switch (pos) {
            case "HEAD" -> armorStand.getHeadPose();
            case "BODY" -> armorStand.getBodyPose();
            case "LARM" -> armorStand.getLeftArmPose();
            case "RARM" -> armorStand.getRightArmPose();
            case "LLEG" -> armorStand.getLeftLegPose();
            case "RLEG" -> armorStand.getRightLegPose();
            default -> null;
        };
        if (eulerAngle == null) return;

        if (this.toString().endsWith("_X")) {
            eulerAngle = eulerAngle.setX(eulerAngle.getX() + value);
        } else if (this.toString().endsWith("_Y")) {
            eulerAngle = eulerAngle.setY(eulerAngle.getY() + value);
        } else if (this.toString().endsWith("_Z")) {
            eulerAngle = eulerAngle.setZ(eulerAngle.getZ() + value);
        }
        switch (pos) {
            case "HEAD" -> armorStand.setHeadPose(eulerAngle);
            case "BODY" -> armorStand.setBodyPose(eulerAngle);
            case "LARM" -> armorStand.setLeftArmPose(eulerAngle);
            case "RARM" -> armorStand.setRightArmPose(eulerAngle);
            case "LLEG" -> armorStand.setLeftLegPose(eulerAngle);
            case "RLEG" -> armorStand.setRightLegPose(eulerAngle);
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

    public void sendTitle(Player player) {
        if(this == ARMOR) return;
        String pos = this.toString().split("_")[0];
        String axis = this.toString().split("_")[1];

        String title = switch (pos) {
            case "HEAD" -> "Kopf";
            case "BODY" -> "Körper";
            case "LARM" -> "Linker Arm";
            case "RARM" -> "Rechter Arm";
            case "LLEG" -> "Linkes Bein";
            case "RLEG" -> "Rechtes Bein";
            default -> null;
        };
        String subtitle = switch (axis) {
            case "X" -> "§c  X  §7|  Y  |  Z | §aFertig";
            case "Y" -> "§7  X  |  §cY  §7|  Z | §aFertig";
            case "Z" -> "§7  X  |  Y  |  §cZ §7| §aFertig";
            default -> null;
        };

        if(pos == null || subtitle == null) return;
        player.sendTitle("§e" + title, subtitle, 0, 20, 0);
    }

    public void switchToNextStep(Player player) {
        switch (this) {
            case HEAD_X -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, HEAD_Y);
            case HEAD_Y -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, HEAD_Z);

            case LARM_X -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, LARM_Y);
            case LARM_Y -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, LARM_Z);

            case RARM_X -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, RARM_Y);
            case RARM_Y -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, RARM_Z);

            case LLEG_X -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, LLEG_Y);
            case LLEG_Y -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, LLEG_Z);

            case RLEG_X -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, RLEG_Y);
            case RLEG_Y -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, RLEG_Z);

            case BODY_X -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, BODY_Y);
            case BODY_Y -> HyperStand.getInstance().getArmorStandManager().setEditingType(player, BODY_Z);


            case HEAD_Z, LARM_Z, RARM_Z, LLEG_Z, RLEG_Z, BODY_Z -> HyperStand.getInstance().getArmorStandManager().finishEditing(player);
        }
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
