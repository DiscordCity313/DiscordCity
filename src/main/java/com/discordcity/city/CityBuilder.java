package com.discordcity.city;

import com.discordcity.city.tile.CityTile;
import com.discordcity.city.tile.CityTileType;
import com.discordcity.database.MySql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CityBuilder {

    private final int INITIAL_FUNDS = 500;

    private final int TILE_GRID_WIDTH = 9;
    private final int TILE_GRID_HEIGHT = 9;

    private final int TILE_COUNT = this.TILE_GRID_WIDTH * this.TILE_GRID_HEIGHT;

    private MySql database;

    public CityBuilder(MySql database) {
        this.database = database;
    }

    public City getCityForUser(String ownerUserId) throws SQLException {
        if(this.cityExists(ownerUserId)) {
            return this.buildExistingCity(ownerUserId);
        } else {
            return this.buildNewCity(ownerUserId);
        }
    }

    public City buildExistingCity(String ownerUserId) throws SQLException {
        PreparedStatement cityPropertiesQuery = this.database.getStatement("SELECT * FROM CityProperties WHERE ownerUserId = ?");
        cityPropertiesQuery.setString(1, ownerUserId);

        ResultSet cityPropertiesResult = cityPropertiesQuery.executeQuery();

        PreparedStatement cityTilesQuery = this.database.getStatement("SELECT * FROM CityTiles WHERE ownerUserId = ?");
        cityTilesQuery.setString(1, ownerUserId);

        ResultSet cityTilesResult = cityTilesQuery.executeQuery();

        cityPropertiesResult.next();
        int cityPopulation = cityPropertiesResult.getInt("population");
        int cityFunds = cityPropertiesResult.getInt("funds");

        cityTilesResult.next();
        CityTileType[] cityTilemap = this.generateTilesFromString(cityTilesResult.getString("tiles"));

        City builtCity = new City(cityPopulation, cityFunds, cityTilemap, this.TILE_GRID_WIDTH, this.TILE_GRID_HEIGHT, ownerUserId);

        return builtCity;
    }

    public City buildNewCity(String ownerUserId) throws SQLException {
        PreparedStatement createCityProperties = this.database.getStatement("INSERT INTO CityProperties (ownerUserId, population, funds) VALUES (?, ?, ?)");
        createCityProperties.setString(1, ownerUserId);
        createCityProperties.setInt(2, 0);
        createCityProperties.setInt(3, this.INITIAL_FUNDS);

        createCityProperties.execute();

        PreparedStatement createCityTiles = this.database.getStatement("INSERT INTO CityTiles (ownerUserId, tiles) VALUES (?, ?)");
        createCityTiles.setString(1, ownerUserId);

        CityTileType[] newCityTilemap = this.generateBlankTilemap();
        createCityTiles.setString(2, this.tilesToString(newCityTilemap));

        createCityTiles.execute();

        City builtCity = new City(0, this.INITIAL_FUNDS, newCityTilemap, this.TILE_GRID_WIDTH, this.TILE_GRID_HEIGHT, ownerUserId);

        return builtCity;
    }

    public boolean cityExists(String ownerUserId) throws SQLException {
        PreparedStatement cityExistsQuery = this.database.getStatement("SELECT ownerUserId FROM CityProperties WHERE ownerUserId = ?");
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

}
