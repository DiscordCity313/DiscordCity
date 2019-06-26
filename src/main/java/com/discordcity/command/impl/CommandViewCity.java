package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.CityCache;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.command.CityCommand;
import com.discordcity.command.Command;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

import java.sql.SQLException;

public class CommandViewCity extends CityCommand {

    public CommandViewCity() {
        super(new String [] {"city", "view", "viewcity", "mycity"}, "Displays your city");
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        try {
            String userId = message.getAuthor().getId();

            this.reply(message, this.generateVisualCityDisplay(this.getCityForUser(userId, database), database));
        } catch(SQLException sqlException) {
            this.replyError(message, "There was an issue retrieving your city information");
            sqlException.printStackTrace();
        }
    }

    private String generateVisualCityDisplay(City city, MySql database) throws SQLException {
        String visualCityDisplay = ("```\n ");

        int tileGridWidth = city.getTileGridWidth();
        int tileGridHeight = city.getTileGridHeight();

        for(int columnNumberDisplay = 0; columnNumberDisplay < tileGridWidth; columnNumberDisplay++) {
            visualCityDisplay += (columnNumberDisplay + 1);
        }

        for(int row = 0; row < tileGridHeight; row++) {
            visualCityDisplay += ("\n" + (row + 1));

            for(int column = 0; column < tileGridWidth; column++) {
                int totalTileIndex = (row * tileGridWidth + column);

                CityTileType tileType = city.getTileForIndex(totalTileIndex);

                visualCityDisplay += (tileType.SYMBOL);
            }
        }

        visualCityDisplay += ("\n\nPopulation: " + city.getPopulation());
        visualCityDisplay += ("\n\nFunds: $" + city.getFunds());
        visualCityDisplay += ("\n\nDensity: " + city.getDensity() + "/" + city.getMaxDensity());

        visualCityDisplay += ("\n```");

        return visualCityDisplay;
    }

}
