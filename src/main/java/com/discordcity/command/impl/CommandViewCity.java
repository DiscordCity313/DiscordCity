package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.render.CityRenderer;
import com.discordcity.command.CityCommand;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CommandViewCity extends CityCommand {

    private CityRenderer cityRenderer;

    public CommandViewCity() throws IOException {
        super(new String [] {"city", "view", "viewcity", "mycity"}, "Displays your city");
        CityBuilder cityBuilder = CityBuilder.getInstance();

        int mapGridWidth = cityBuilder.TILE_GRID_WIDTH;
        int mapGridHeight = cityBuilder.TILE_GRID_HEIGHT;

        int tileWidth = cityBuilder.TILE_WIDTH;
        int tileHeight = cityBuilder.TILE_HEIGHT;

        this.cityRenderer = new CityRenderer(mapGridWidth, mapGridHeight, tileWidth, tileHeight);
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        try {
            String userId = message.getAuthor().getId();

            City userCity = this.getCityForUser(userId, database);

            File cityImage = this.cityRenderer.render(userCity);

            message.getTextChannel().sendFile(cityImage).queue();
        } catch(SQLException sqlException) {
            this.replyError(message, "There was an issue retrieving your city information");
            sqlException.printStackTrace();
        } catch(IOException renderException) {
            this.replyError(message, "There was an issue rendering your city");
        }
    }

}
