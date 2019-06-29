package com.discordcity.command.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.render.CityRenderer;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.command.CityCommand;
import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;
import java.sql.SQLException;

public class CommandPurchaseTile extends CityCommand {

    private CityRenderer cityRenderer;

    public CommandPurchaseTile(String prefix) throws IOException {
        super(new String[] {"buy", "purchase", "tile"}, "<building> <column> <row> ", "Buy and place a building for your city");
        this.setExampleUsage(prefix + "buy house 2 5 - (Place a house at column 2, row 5)");
        this.setExampleUsage(this.getExampleUsage() + "\n" + prefix + "buy industry 3 7 - (Place industry at column 3, row 7)");
        this.setExampleUsage(this.getExampleUsage() + "\n\n" + "_Letters (h, i) can be used in place of building names_");

        CityBuilder cityBuilder = CityBuilder.getInstance();

        this.cityRenderer = new CityRenderer(cityBuilder.TILE_WIDTH, cityBuilder.TILE_HEIGHT);
    }

    @Override
    public void use(Message message, String[] arguments, Sqlite database) {
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
                    userCity.modifyFunds(-price);

                    userCity.updateCityForTime(0, database);

                    this.sendCityDisplayMessage("You paid **$" + price + "** for one **" + purchasedTile.name().toLowerCase() + "**!", message.getTextChannel(), message.getAuthor().getId(), database);
                } else {
                    this.reply(message, "Insufficient funds! You currently have $" + userCity.getFunds() + ", while that tile costs $" + price + ". Your city will generate funds over time: Check on your city again soon!");
                }
            } else {
                this.reply(message, "Invalid tile name! Available options: " + this.getOptionsDisplay());
            }
        } catch(SQLException sqlException) {
            this.replyError(message, "Failed to fetch or write city information");
            sqlException.printStackTrace();
        } catch(NumberFormatException | ArrayIndexOutOfBoundsException invalidPosition) {
            this.reply(message, "Please specify a valid position for your tile!" + "\n\nExample Usage:\n" + this.getExampleUsage());
        }

    }

    private String getOptionsDisplay() {
        return CityTileType.getHumanTilesDisplay() + "\n\nExample Usage:\n" + this.getExampleUsage();
    }

}
