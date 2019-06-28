package com.discordcity;

import com.discordcity.command.CommandListener;
import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class Bot {

    private JDA jda;

    private Sqlite database;

    public Bot(JSONObject config, String game) throws LoginException, SQLException, ClassNotFoundException {
        try {
            this.jda = new JDABuilder(config.getString("BOT_TOKEN")).setGame(Game.of(Game.GameType.DEFAULT, game)).build();
            this.setupDatabase(config);
            this.setupCommandListener(config);
        } catch(IOException assetLoadException) {
            assetLoadException.printStackTrace();
        }
    }

    private void setupDatabase(JSONObject config) throws SQLException, ClassNotFoundException {
        this.database = new Sqlite();
    }

    private void setupCommandListener(JSONObject config) throws IOException {
        String prefix = config.getString("BOT_PREFIX");

        this.jda.addEventListener(new CommandListener(prefix, this.database));
    }

}
