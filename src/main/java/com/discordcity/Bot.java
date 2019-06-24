package com.discordcity;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;

public class Bot {

    private JDA jda;

    public Bot(JSONObject config, String game) throws LoginException {
        this.jda = new JDABuilder(config.getString("BOT_TOKEN")).setGame(Game.of(Game.GameType.DEFAULT, game)).build();
    }

}
