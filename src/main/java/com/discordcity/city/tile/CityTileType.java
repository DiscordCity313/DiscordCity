package com.discordcity.city.tile;

public enum CityTileType {

    Ground(new CityTile(), '#'), House(new CityTile(), 'H'), Industry(new CityTile(), 'I');

    CityTileType(CityTile cityTile, char symbol) {
        this.CITY_TILE = cityTile;
        this.SYMBOL = symbol;
    }

    public final CityTile CITY_TILE;
    public final char SYMBOL;

    public int getId() {
        return this.ordinal();
    }

    public static CityTileType forId(int id) {
        return CityTileType.values()[id];
    }

}
