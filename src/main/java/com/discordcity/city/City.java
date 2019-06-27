package com.discordcity.city;

import com.discordcity.city.tile.CityTileType;
import com.discordcity.database.MySql;
import com.discordcity.util.TimeUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class City {

    private int population;

    private int funds;

    private int maxDensity;

    private CityTileType[] tiles;
    private int tileGridWidth;
    private int tileGridHeight;

    private String ownerUserId;

    public City(int population, int funds, int maxDensity, CityTileType[] tiles, int tileGridWidth, int tileGridHeight, String ownerUserId) {
        this.population = population;
        this.funds = funds;
        this.maxDensity = maxDensity;
        this.tiles = tiles;
        this.tileGridWidth = tileGridWidth;
        this.tileGridHeight = tileGridHeight;
        this.ownerUserId = ownerUserId;
    }

    public void updateCityForTime(int secondsSinceLastUpdate, MySql database) throws SQLException {
        System.out.println("Seconds since last update: " + secondsSinceLastUpdate);
        for(CityTileType cityTileType : this.tiles) {
            cityTileType.CITY_TILE.updateForTime(secondsSinceLastUpdate, this);
        }

        this.updateDatabaseProperties(database);
    }

    public void modifyPopulation(int modification) {
        this.population += modification;

        if(this.population > this.getMaxPopulation()) {
            this.population = this.getMaxPopulation();
        }

        if(this.population < 0) {
            this.population = 0;
        }
    }

    public void setTile(int column, int row, CityTileType cityTileType, MySql database) throws SQLException {
        int tileIndex = (row * this.tileGridWidth + column);

        this.tiles[tileIndex] = cityTileType;

        this.writeTilemapToDatabase(database);
    }

    public void modifyFunds(int modification) {
        this.funds += modification;
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

    private int tileCountForType(CityTileType cityTileType) {
        int tileCount = 0;

        for(CityTileType possibleMatch : this.tiles) {
            if(possibleMatch == cityTileType) {
                tileCount++;
            }
        }

        return tileCount;
    }

    public int getDensity() {
        try {
            int houses = this.tileCountForType(CityTileType.House);

            int density = this.population / houses;

            return density;
        } catch(ArithmeticException zeroPopulation) {
            return 0;
        }
    }

    public Timestamp getLastUpdated(MySql database) throws SQLException {
        PreparedStatement lastUpdatedQuery = database.getStatement("SELECT lastUpdated FROM CityProperties WHERE ownerUserId = ?");
        lastUpdatedQuery.setString(1, this.ownerUserId);

        ResultSet lastUpdatedResult = lastUpdatedQuery.executeQuery();
        lastUpdatedResult.next();

        return lastUpdatedResult.getTimestamp("lastUpdated");
    }

    public void updateDatabaseProperties(MySql database) throws SQLException {
        this.updateDatabaseTime(database);
        this.updateDatabasePopulation(database);
        this.updateDatabaseFunds(database);
        this.updateDatabaseMaxDensity(database);
    }

    public void updateDatabaseTime(MySql database) throws SQLException {
        Timestamp currentTime = TimeUtil.getInstance().getCurrentTime();

        PreparedStatement updateTime = database.getStatement("UPDATE CityProperties SET lastUpdated = ? WHERE ownerUserId = ?");
        updateTime.setTimestamp(1, currentTime);
        updateTime.setString(2, this.ownerUserId);

        updateTime.execute();
    }

    public void updateDatabasePopulation(MySql database) throws SQLException {
        PreparedStatement updatePopulation = database.getStatement("UPDATE CityProperties SET population = ? WHERE ownerUserId = ?");
        updatePopulation.setInt(1, this.population);
        updatePopulation.setString(2, this.ownerUserId);

        updatePopulation.execute();
    }

    public void updateDatabaseFunds(MySql database) throws SQLException {
        PreparedStatement updateFunds = database.getStatement("UPDATE CityProperties SET funds = ? WHERE ownerUserId = ?");
        updateFunds.setString(1, "" + this.funds);
        updateFunds.setString(2, this.ownerUserId);

        updateFunds.execute();
    }

    public void updateDatabaseMaxDensity(MySql database) throws SQLException {
        PreparedStatement updateMaxDensity = database.getStatement("UPDATE CityProperties SET maxDensity = ? WHERE ownerUserId = ?");
        updateMaxDensity.setInt(1, this.maxDensity);
        updateMaxDensity.setString(2, this.ownerUserId);

        updateMaxDensity.execute();
    }

    public int getSecondsSinceLastUpdate(MySql database) throws SQLException {
        Timestamp lastUpdatedTime = this.getLastUpdated(database);
        Timestamp currentTime = TimeUtil.getInstance().getCurrentTime();

        long differenceMillis = currentTime.getTime() - lastUpdatedTime.getTime();

        return (int) differenceMillis / 1000;
    }

    public int getMaxDensity() {
        return this.maxDensity;
    }

    public int getMaxPopulation() {
        return this.maxDensity * this.tileCountForType(CityTileType.House);
    }

    public String getOwnerUserId() {
        return this.ownerUserId;
    }

}