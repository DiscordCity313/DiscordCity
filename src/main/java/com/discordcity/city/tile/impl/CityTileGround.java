package com.discordcity.city.tile.impl;

import com.discordcity.city.City;
import com.discordcity.city.tile.CityTile;

public class CityTileGround extends CityTile {

    @Override
    public boolean updateForTime(int secondsSinceLastUpdate, City parentCity) {
        return true;
    }

}
