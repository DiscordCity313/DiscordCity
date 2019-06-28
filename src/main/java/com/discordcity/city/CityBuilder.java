package com.discordcity.city;

import com.discordcity.city.tile.CityTile;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.database.MySql;
import com.discordcity.util.TimeUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CityBuilder {

    private static final CityBuilder INSTANCE = new CityBuilder();

    private final int INITIAL_FUNDS = 1500;

    private final int INITAL_MAX_DENSITY = 20;

    public final int TILE_GRID_WIDTH = 9;
    public final int TILE_GRID_HEIGHT = 9;

    public final int TILE_WIDTH = 16;
    public final int TILE_HEIGHT = 16;

    private final int TILE_COUNT = this.TILE_GRID_WIDTH * this.TILE_GRID_HEIGHT;

    public final int DAMAGING_UNEMPLOYMENT_RATE = 30;

    public City getCityForUser(String ownerUserId, MySql database) throws SQLException {
        if(this.cityExists(ownerUserId, database)) {
            return this.buildExistingCity(ownerUserId, database);
        } else {
            return this.buildNewCity(ownerUserId, database);
        }
    }

    public City buildExistingCity(String ownerUserId, MySql database) throws SQLException {
        PreparedStatement cityPropertiesQuery = database.getStatement("SELECT * FROM CityProperties WHERE ownerUserId = ?");
        cityPropertiesQuery.setString(1, ownerUserId);

        ResultSet cityPropertiesResult = cityPropertiesQuery.executeQuery();

        PreparedStatement cityTilesQuery = database.getStatement("SELECT * FROM CityTiles WHERE ownerUserId = ?");
        cityTilesQuery.setString(1, ownerUserId);

        ResultSet cityTilesResult = cityTilesQuery.executeQuery();

        cityPropertiesResult.next();
        int cityPopulation = cityPropertiesResult.getInt("population");
        int cityFunds = cityPropertiesResult.getInt("funds");
        int maxDensity = cityPropertiesResult.getInt("maxDensity");

        cityTilesResult.next();
        CityTileType[] cityTilemap = this.generateTilesFromString(cityTilesResult.getString("tiles"));

        City builtCity = new City(cityPopulation, cityFunds, maxDensity, cityTilemap, this.TILE_GRID_WIDTH, this.TILE_GRID_HEIGHT, ownerUserId);

        return builtCity;
    }

    public City buildNewCity(String ownerUserId, MySql database) throws SQLException {
        PreparedStatement createCityProperties = database.getStatement("INSERT INTO CityProperties (ownerUserId, population, funds, maxDensity, lastUpdated) VALUES (?, ?, ?, ?, ?)");
        createCityProperties.setString(1, ownerUserId);
        createCityProperties.setInt(2, 0);
        createCityProperties.setInt(3, this.INITIAL_FUNDS);
        createCityProperties.setInt(4, this.INITAL_MAX_DENSITY);
        createCityProperties.setTimestamp(5, TimeUtil.getInstance().getCurrentTime());

        createCityProperties.execute();

        PreparedStatement createCityTiles = database.getStatement("INSERT INTO CityTiles (ownerUserId, tiles) VALUES (?, ?)");
        createCityTiles.setString(1, ownerUserId);

        CityTileType[] newCityTilemap = this.generateBlankTilemap();
        createCityTiles.setString(2, this.tilesToString(newCityTilemap));

        createCityTiles.execute();

        City builtCity = new City(0, this.INITIAL_FUNDS, this.INITAL_MAX_DENSITY, newCityTilemap, this.TILE_GRID_WIDTH, this.TILE_GRID_HEIGHT, ownerUserId);

        return builtCity;
    }

    public City resetCity(String ownerUserId, MySql database) throws SQLException {
        PreparedStatement resetPropertiesStatement = database.getStatement("DELETE FROM CityProperties WHERE ownerUserId = ?");
        resetPropertiesStatement.setString(1, ownerUserId);

        resetPropertiesStatement.execute();

        PreparedStatement resetTilesStatemet = database.getStatement("DELETE FROM CityTiles WHERE ownerUserId = ?");
        resetTilesStatemet.setString(1, ownerUserId);

        resetTilesStatemet.execute();

        return this.buildNewCity(ownerUserId, database);
    }

    public boolean cityExists(String ownerUserId, MySql database) throws SQLException {
        PreparedStatement cityExistsQuery = database.getStatement("SELECT ownerUserId FROM CityProperties WHERE ownerUserId = ?");
        cityExistsQuery.setString(1, ownerUserId);

        return cityExistsQuery.executeQuery().next();
    }

    public String tilesToString(CityTileType[] tiles) {
        String tileData = ("");

        for(CityTileType cityTileType : tiles) {
            tileData += (cityTileType.getId() + ",");
        }

        return tileData;
    }

    private CityTileType[] generateTilesFromString(String tileData) {
        String[] individualTiles = tileData.split(",");

        CityTileType[] tiles = new CityTileType[individualTiles.length];

        for(int individualTileIndex = 0; individualTileIndex < individualTiles.length; individualTileIndex++) {
            int individualTileId = Integer.parseInt(individualTiles[individualTileIndex]);
            CityTileType individualTileType = CityTileType.forId(individualTileId);
            tiles[individualTileIndex] = individualTileType;
        }

        return tiles;
    }

    private CityTileType[] generateBlankTilemap() {
        CityTileType[] tiles = new CityTileType[this.TILE_COUNT];

        for(int tilesGenerated = 0; tilesGenerated < this.TILE_COUNT; tilesGenerated++) {
            tiles[tilesGenerated] = CityTileType.Ground;
        }

        return tiles;
    }

    public static CityBuilder getInstance() {
        return INSTANCE;
    }

}
