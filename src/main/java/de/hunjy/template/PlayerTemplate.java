package de.hunjy.template;

import de.hunjy.HyperStand;
import de.hunjy.mysql.ArmorstandQueryListener;
import de.hunjy.mysql.MySQLConnection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

public class PlayerTemplate {

    private static MySQLConnection connection = HyperStand.getInstance().getMySQLConnection();

    public static void get(Player player, ArmorstandQueryListener armorstandQueryListener) {
        connection.loadTemplatesFromDatabase(player.getUniqueId(), armorstandQueryListener);
    }

    public static void saveTemplate(Player player, String name, String description, ArmorStand armorStand) {
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

        try {
            PreparedStatement preparedStatement = connection.getCon().prepareStatement("INSERT INTO hyperstand(UUID, name, rawData, description) VALUES (?,?,?,?)");
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, data);
            preparedStatement.setString(4, description);
            connection.query(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        player.sendMessage(HyperStand.getInstance().getMessageManager().get("create_template_success", true));
    }

    public static void trySaveTemplate(Player player, String name, String description, ArmorStand armorStand) {
        player.sendMessage(HyperStand.getInstance().getMessageManager().get("create_template", true));

        get(player, new ArmorstandQueryListener() {
            @Override
            public void onQueryResult(List<ArmorStandTemplate> templates) {
                if (templates.size() >= 14) {
                    player.sendMessage(HyperStand.getInstance().getMessageManager().get("create_template_max", true));
                    return;
                }

                for(ArmorStandTemplate template: templates) {
                    if(template.getName().equalsIgnoreCase(name)) {
                        player.sendMessage(HyperStand.getInstance().getMessageManager().get("create_template_exist", true));
                        return;
                    }
                }
                saveTemplate(player, name, description, armorStand);
            }

            @Override
            public void onQueryError(Exception exception) {
                onQueryError(exception);
            }
        });
    }

    public static void remove(Player player, String name) {
        get(player, new ArmorstandQueryListener() {
            @Override
            public void onQueryResult(List<ArmorStandTemplate> templates) {
                for (ArmorStandTemplate template : templates) {
                    if (template.getName().equalsIgnoreCase(name)) {
                        try {
                            PreparedStatement preparedStatement = connection.getCon().prepareStatement("DELETE FROM hyperstand WHERE UUID=? AND name=?");
                            preparedStatement.setString(1, player.getUniqueId().toString());
                            preparedStatement.setString(2, name);
                            connection.query(preparedStatement);
                            player.sendMessage(HyperStand.getInstance().getMessageManager().get("TEMPLATE_DELETE_COMPLETE", true));
                            return;
                        } catch (SQLException e) {
                            e.printStackTrace();
                            player.sendMessage(HyperStand.getInstance().getMessageManager().get("TEMPLATE_DELETE_ERROR", true));
                        }
                    }
                }

                player.sendMessage(HyperStand.getInstance().getMessageManager().get("TEMPLATE_DELETE_NOT_EXIST", true));
            }

            @Override
            public void onQueryError(Exception exception) {

            }
        });
    }

}
