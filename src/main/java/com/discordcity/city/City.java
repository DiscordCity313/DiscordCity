package com.discordcity.city;

import com.discordcity.city.tile.CityTileType;

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

    public CityTileType getTileForIndex(int index) {
        return this.tiles[index];
    }

    public int getTileGridWidth() {
        return this.tileGridWidth;
    }

    public int getTileGridHeight() {
        return this.tileGridHeight;
    }

}