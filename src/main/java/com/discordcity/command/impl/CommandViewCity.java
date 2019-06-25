package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.CityCache;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.command.Command;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

import java.sql.SQLException;

public class CommandViewCity extends Command {

    public CommandViewCity() {
        super("city", "Displays your city");
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        try {
            String userId = message.getAuthor().getId();

            this.reply(message, this.generateVisualCityDisplay(this.getCityForUser(userId, database)));
        } catch(SQLException sqlException) {
            this.replyError(message, "There was an issue retrieving your city information");
            sqlException.printStackTrace();
        }
    }

    private City getCityForUser(String userId, MySql database) throws SQLException {
        CityCache cityCache = CityCache.getInstance();

        City userCity = null;

        if(cityCache.isCityCached(userId)) {
            userCity = cityCache.getCityFromCache(userId);
        } else {
            userCity = new CityBuilder(database).getCityForUser(userId);
            cityCache.addCityToCache(userId, userCity);
        }

        int secondsSinceLastUpdate = 0;
        userCity.updateCityForTime(secondsSinceLastUpdate);

        return userCity;
    }

    private String generateVisualCityDisplay(City city) {
        String visualCityDisplay = ("```\n ");

        int tileGridWidth = city.getTileGridWidth();
        int tileGridHeight = city.getTileGridHeight();

        for(int columnNumberDisplay = 0; columnNumberDisplay < tileGridWidth; columnNumberDisplay++) {
            visualCityDisplay += (columnNumberDisplay);
        }

        for(int row = 0; row < tileGridHeight; row++) {
            visualCityDisplay += ("\n" + row);

            for(int column = 0; column < tileGridWidth; column++) {
                int totalTileIndex = (row * tileGridWidth + column);

                CityTileType tileType = city.getTileForIndex(totalTileIndex);

                visualCityDisplay += (tileType.SYMBOL);
            }
        }

        visualCityDisplay += ("\n```");

        return visualCityDisplay;
    }

}
