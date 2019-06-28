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

        this.cityRenderer = new CityRenderer(cityBuilder.TILE_WIDTH, cityBuilder.TILE_HEIGHT);
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        try {
            City userCity = this.getCityForUser(message.getAuthor().getId(), database);

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
