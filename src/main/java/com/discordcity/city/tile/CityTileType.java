package com.discordcity.city.tile;

import org.apache.commons.lang3.EnumUtils;

public enum CityTileType {

    Ground(new CityTile(), '#', 0), House(new CityTile(), 'H', 15),
    Industry(new CityTile(), 'I', 25);

    CityTileType(CityTile cityTile, char symbol, int price) {
        this.CITY_TILE = cityTile;
        this.SYMBOL = symbol;
        this.PRICE = price;
    }

    public final CityTile CITY_TILE;
    public final char SYMBOL;
    public final int PRICE;

    public int getId() {
        return this.ordinal();
    }

    public static CityTileType forId(int id) {
        return CityTileType.values()[id];
    }

    public static CityTileType forName(String name) {
        CityTileType byName = EnumUtils.getEnumIgnoreCase(CityTileType.class, name);

        if(byName != null) {
            return byName;
        } else {
            return CityTileType.forSymbol(name.charAt(0));
        }
    }

    public static CityTileType forSymbol(char symbol) {
        String lowercaseSymbol = ("" + symbol).toLowerCase();

        for(CityTileType bySymbol : CityTileType.values()) {
            if(("" + bySymbol.SYMBOL).equalsIgnoreCase(lowercaseSymbol)) {
                return bySymbol;
            }
        }

        return null;
    }

    public static String getHumanTilesDisplay() {
        String humanTilesDisplay = ("");

        for(int cityTileTypeIndex = 0; cityTileTypeIndex < CityTileType.values().length; cityTileTypeIndex++) {
            CityTileType cityTileType = CityTileType.values()[cityTileTypeIndex];

            humanTilesDisplay += (cityTileType.name() + " (or " + cityTileType.SYMBOL + ")");

            boolean finalValue = (cityTileTypeIndex == CityTileType.values().length  - 1);

            if(!finalValue) {
                humanTilesDisplay += (", ");
            }
        }

        return humanTilesDisplay;
    }

}
