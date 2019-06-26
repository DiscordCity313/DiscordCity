package com.discordcity.city;

import com.discordcity.city.tile.CityTileType;
import com.discordcity.database.MySql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class City {

    private int population;

    private int funds;

    private CityTileType[] tiles;
    private int tileGridWidth;
    private int tileGridHeight;

    private String ownerUserId;

    public City(int population, int funds, CityTileType[] tiles, int tileGridWidth, int tileGridHeight, String ownerUserId) {
        this.population = population;
        this.funds = funds;
        this.tiles = tiles;
        this.tileGridWidth = tileGridWidth;
        this.tileGridHeight = tileGridHeight;
        this.ownerUserId = ownerUserId;
    }

    public void updateCityForTime(int secondsSinceLastUpdate) {

    }

    public void setTile(int column, int row, CityTileType cityTileType, MySql database) throws SQLException {
        int tileIndex = (row * this.tileGridWidth + column);

        this.tiles[tileIndex] = cityTileType;

        this.writeTilemapToDatabase(database);
    }

    public void modifyFunds(int modification, MySql database) throws SQLException {
        this.funds += modification;

        PreparedStatement updateFunds = database.getStatement("UPDATE CityProperties SET funds = ? WHERE ownerUserId = ?");
        updateFunds.setString(1, "" + this.funds);
        updateFunds.setString(2, this.ownerUserId);

        updateFunds.execute();
    }

    private void writeTilemapToDatabase(MySql database) throws SQLException {
        String tilemapData = new CityBuilder(database).tilesToString(this.tiles);

        PreparedStatement updateTilemap = database.getStatement("UPDATE CityTiles SET tiles = ? WHERE ownerUserId = ?");
        updateTilemap.setString(1, tilemapData);
        updateTilemap.setString(2, this.ownerUserId);

        updateTilemap.execute();
    }

    public CityTileType getTileForIndex(int index) {
        return this.tiles[index];
    }

    public int getTileGridWidth() {
        return this.tileGridWidth;
    }

    public int getTileGridHeight() {
        return this.tileGridHeight;
    }

    public int getPopulation() {
        return this.population;
    }

    public int getFunds() {
        return this.funds;
    }

}