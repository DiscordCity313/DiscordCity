package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.CityCache;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.command.CityCommand;
import com.discordcity.command.Command;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.sql.SQLException;

public class CommandViewCity extends CityCommand {

    public CommandViewCity() {
        super(new String [] {"city", "view", "viewcity", "mycity"}, "Displays your city");
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        try {
            String userId = message.getAuthor().getId();

            this.reply(message, this.generateVisualCityDisplay(this.getCityForUser(userId, database), database, message.getJDA()));
        } catch(SQLException sqlException) {
            this.replyError(message, "There was an issue retrieving your city information");
            sqlException.printStackTrace();
        }
    }

    private String generateVisualCityDisplay(City city, MySql database, JDA jda) throws SQLException {
        String visualCityDisplay = ("```\n");

        String ownerName = jda.getUserById(city.getOwnerUserId()).getName();
        visualCityDisplay += ("-\n" + ownerName + "'s City\n-\n\n");

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

        visualCityDisplay += ("\n\nFunds: $" + city.getFunds());
        visualCityDisplay += ("\n\nPopulation: " + city.getPopulation() + "/" + city.getMaxPopulation());
        visualCityDisplay += ("\n\nDensity: " + city.getDensity() + "/" + city.getMaxDensity());
        visualCityDisplay += ("\n\n\n------------");
        visualCityDisplay += ("\nCity Commands");
        visualCityDisplay += ("\n------------");
        visualCityDisplay += ("\n\n" + this.getPrefix() + "buy house 2 1\n(Buys a house at column 2, row 1)");
        visualCityDisplay += ("\n\n" + this.getPrefix() + "city\n(Displays and updates your city)");

        visualCityDisplay += ("\n```");

        return visualCityDisplay;
    }

}
