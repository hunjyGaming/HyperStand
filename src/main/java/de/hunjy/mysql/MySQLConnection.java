package de.hunjy.mysql;

import de.hunjy.logger.ILogger;
import de.hunjy.template.ArmorStandTemplate;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MySQLConnection {
    private final ExecutorService service = Executors.newFixedThreadPool(1);
    public Connection con;
    String host;
    String name;
    String password;
    String database;

    public MySQLConnection(String host, String name, String password, String database) {
        this.host = host;
        this.name = name;
        this.password = password;
        this.database = database;
    }

    public void connect() {
        if (!this.isConnected()) {
            try {
                this.con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":3306/" + this.database + "?autoReconnect=true", this.name, this.password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void close() {
        try {
            this.service.shutdown();
            this.service.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.isConnected()) {
            try {
                this.con.close();
                ILogger.log("[MySQL] Verbindung zur MySQL beendet!");
            } catch (SQLException var2) {
                var2.printStackTrace();
                ILogger.log("[MySQL] §4Fehler: §c" + var2.getMessage());
            }
        }

    }

    public boolean isConnected() {
        return this.con != null;
    }

    public void query(PreparedStatement qry) {
        this.service.execute(() -> {
            if (!this.isConnected()) {
                this.connect();
            }

            if (this.isConnected()) {
                try {
                    qry.executeUpdate();
                } catch (SQLException var3) {
                    var3.printStackTrace();
                }
            }
        });
    }

    public void query(String qry) {
        this.service.execute(() -> {
            if (!this.isConnected()) {
                this.connect();
            }

            if (this.isConnected()) {
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(qry);
                    preparedStatement.executeUpdate();
                } catch (SQLException var3) {
                    var3.printStackTrace();
                }
            }
        });
    }

    public void loadTemplatesFromDatabase(UUID playerUuid, ArmorstandQueryListener queryListener) {
        Objects.requireNonNull(playerUuid);
        Objects.requireNonNull(queryListener);
        this.service.execute(() -> {
            if (this.isConnected()) {
                List<ArmorStandTemplate> templates = new ArrayList<>();
                try (PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM hyperstand WHERE UUID=?")) {
                    stmt.setString(1, playerUuid.toString());
                    try (ResultSet set = stmt.executeQuery()) {
                        while (set.next()) {
                            templates.add(new ArmorStandTemplate(
                                    set.getString("name"),
                                    new String(Base64.getDecoder().decode(set.getString("rawData").getBytes())),
                                    set.getString("description")
                            ));
                        }
                    }
                } catch (SQLException var3) {
                    queryListener.onQueryError(var3);
                    return;
                }
                queryListener.onQueryResult(templates);
            }
        });
    }

    private ResultSet getResult(String qry) {
        if (!this.isConnected()) {
            this.connect();
        }

        if (this.isConnected()) {
            try {
                return this.con.createStatement().executeQuery(qry);
            } catch (SQLException var3) {
                var3.printStackTrace();
            }
        }

        return null;
    }

    public Connection getCon() {
        return this.con;
    }

}
