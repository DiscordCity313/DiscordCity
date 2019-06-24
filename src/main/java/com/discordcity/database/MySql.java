package com.discordcity.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySql {

    private Connection connection;

    public MySql(String driver, String connection) throws SQLException, ClassNotFoundException {
        Class.forName(driver);

        this.connection = DriverManager.getConnection(connection);
    }

    public PreparedStatement getStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

}