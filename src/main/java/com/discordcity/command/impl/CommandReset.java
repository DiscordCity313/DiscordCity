package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.CityCache;
import com.discordcity.command.CityCommand;
import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;
import java.sql.SQLException;

public class CommandReset extends CityCommand {

    public CommandReset() throws IOException {
        super(new String[] {"reset"}, "Resets your city");
    }

    @Override
    public void use(Message message, String[] arguments, Sqlite database) {
        try {
            if (arguments[0].equalsIgnoreCase("confirm")) {
                City newCity = CityBuilder.getInstance().resetCity(message.getAuthor().getId(), database);

                CityCache.getInstance().addCityToCache(message.getAuthor().getId(), newCity);

                this.sendCityDisplayMessage("**City reset!**", message.getTextChannel(), message.getAuthor().getId(), database);
            } else {
                this.reply(message, "**You are about to reset your city!** Please type " + this.getPrefix() + "reset confirm to confirm");
            }
        } catch(SQLException cityBuildException) {
            this.replyError(message, "Failed to create your city in the database");
        }
    }

}
