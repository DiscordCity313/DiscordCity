package com.discordcity.command;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.CityCache;
import com.discordcity.database.MySql;

import java.sql.SQLException;

public abstract class CityCommand extends Command {

    public CityCommand(String[] identifiers, String[] arguments, String description) {
        super(identifiers, arguments, description);
    }

    public CityCommand(String[] identifiers, String description) {
        super(identifiers, description);
    }

    public City getCityForUser(String userId, MySql database) throws SQLException {
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

}
