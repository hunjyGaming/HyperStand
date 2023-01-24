package de.hunjy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnection {
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
        if (this.isConnected()) {
            try {
                this.con.close();
                System.out.println("[MySQL] Verbindung zur MySQL beendet!");
            } catch (SQLException var2) {
                var2.printStackTrace();
                System.out.println("[MySQL] §4Fehler: §c" + var2.getMessage());
            }
        }

    }

    public boolean isConnected() {
        return this.con != null;
    }

    public void query(String qry) {
        if (!this.isConnected()) {
            this.connect();
        }

        if (this.isConnected()) {
            try {
                this.con.createStatement().executeUpdate(qry);
            } catch (SQLException var3) {
                var3.printStackTrace();
            }
        }

    }

    public ResultSet getResult(String qry) {
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
