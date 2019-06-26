package com.discordcity.city.tile.impl;

import com.discordcity.city.City;
import com.discordcity.city.tile.CityTile;

public class CityTileHouse extends CityTile {

    private int secondsToPopulationRatio = 5;

    @Override
    public void updateForTime(int secondsSinceLastUpdate, City parentCity) {
        int populationIncreaseForTime = secondsSinceLastUpdate / this.secondsToPopulationRatio;

        parentCity.modifyPopulation(populationIncreaseForTime);
    }

}
