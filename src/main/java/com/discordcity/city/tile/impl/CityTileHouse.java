package com.discordcity.city.tile.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.tile.CityTile;

import java.util.HashMap;

public class CityTileHouse extends CityTile {

    private int secondsToPopulationRatio = 4;

    private int secondsToIncomeRatio = 4;

    @Override
    public boolean updateForTime(int secondsSinceLastUpdate, City parentCity) {
        int populationIncreaseForTime = this.getSecondsSinceLastValidTileUpdate(secondsSinceLastUpdate) / this.secondsToPopulationRatio;

        if(parentCity.getUnemployment() < CityBuilder.getInstance().DAMAGING_UNEMPLOYMENT_RATE) {
            parentCity.modifyPopulation(populationIncreaseForTime);
        }

        int incomeIncreaseForTime = this.getSecondsSinceLastValidTileUpdate(secondsSinceLastUpdate) / this.secondsToIncomeRatio;

        parentCity.modifyFunds(incomeIncreaseForTime);

        boolean validTileUpdate = (populationIncreaseForTime > 0 || incomeIncreaseForTime > 0);
        return validTileUpdate;
    }

}
