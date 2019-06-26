package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.command.CityCommand;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;
import java.sql.SQLException;

public class CommandPurchaseTile extends CityCommand {

    public CommandPurchaseTile() {
        super(new String[] {"buy", "purchase", "tile"}, "Buy and place a tile for your city");
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        String userId = message.getAuthor().getId();

        try {
            City userCity = this.getCityForUser(userId, database);

            CityTileType purchasedTile = CityTileType.forName(arguments[0]);

            if(purchasedTile != null) {
                int price = purchasedTile.PRICE;

                if(userCity.getFunds() >= price) {
                    int column = Integer.parseInt(arguments[1]) - 1;
                    int row = Integer.parseInt(arguments[2]) - 1;

                    userCity.setTile(column, row, purchasedTile, database);
                    userCity.modifyFunds(-price, database);

                    this.reply(message, "You've purchased 1 **" + purchasedTile.name() + "** for your city at column " + (column + 1) + " row " + (row + 1) + "! Type **" + this.getPrefix() + "city** to view your city");
                } else {
                    this.reply(message, "Insufficient funds! You currently have $" + userCity.getFunds() + ", while that tile costs $" + price + ". Your city will generate funds over time: Check on your city again soon!");
                }
            } else {
                this.reply(message, "Invalid tile name! Available options: " + CityTileType.getHumanTilesDisplay());
            }
        } catch(SQLException sqlException) {
            this.replyError(message, "Failed to fetch or write city information");
            sqlException.printStackTrace();
        } catch(NumberFormatException invalidPosition) {
            this.reply(message, "Please specify a valid position for your tile! Example: **" + this.getPrefix() + arguments[0] + " 3 6 to purchase a tile at column 3, row 6");
        }
    }

}
