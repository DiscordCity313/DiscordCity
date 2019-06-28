package com.discordcity.command.impl;

import com.discordcity.city.CityBuilder;
import com.discordcity.city.render.CityRenderer;
import com.discordcity.command.CityCommand;
import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public class CommandViewCity extends CityCommand {

    private CityRenderer cityRenderer;

    public CommandViewCity() throws IOException {
        super(new String [] {"city", "view", "viewcity", "mycity"}, "Displays your city");
        CityBuilder cityBuilder = CityBuilder.getInstance();

        this.cityRenderer = new CityRenderer(cityBuilder.TILE_WIDTH, cityBuilder.TILE_HEIGHT);
    }

    @Override
    public void use(Message message, String[] arguments, Sqlite database) {
        this.sendCityDisplayMessage("", message.getTextChannel(), message.getAuthor().getId(), database);
    }

}
