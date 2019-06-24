package com.discordcity;

import com.discordcity.command.CommandListener;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot {

    private JDA jda;

    private MySql database;

    public Bot(JSONObject config, String game) throws LoginException, SQLException, ClassNotFoundException {
        this.jda = new JDABuilder(config.getString("BOT_TOKEN")).setGame(Game.of(Game.GameType.DEFAULT, game)).build();

        this.setupDatabase(config);
        this.setupCommandListener(config);
    }

    private void setupDatabase(JSONObject config) throws SQLException, ClassNotFoundException {
        String databaseHost = config.getString("DB_HOST");
        String databaseName = config.getString("DB_NAME");
        String databasePassword = config.getString("DB_PASSWORD");
        String databaseUser = config.getString("DB_USER");

        String databaseDriver = ("com.mysql.jdbc.Driver");
        String databaseConnection = ("jdbc:mysql://" + databaseHost + "/" + databaseName + "?autoReconnect=true&user=" + databaseUser + "&password=" + databasePassword);

        this.database = new MySql(databaseDriver, databaseConnection);
    }

    private void setupCommandListener(JSONObject config) {
        String prefix = config.getString("BOT_PREFIX");

        this.jda.addEventListener(new CommandListener(prefix, this.database));
    }

}
