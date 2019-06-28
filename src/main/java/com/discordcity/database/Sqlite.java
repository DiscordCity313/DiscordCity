package com.discordcity.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sqlite {

    private Connection connection;

    public Sqlite() throws SQLException, ClassNotFoundException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:./discordcity.db");
    }

    public PreparedStatement getStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

}