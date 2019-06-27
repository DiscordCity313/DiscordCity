package com.discordcity.city.tile.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.tile.CityTile;

public class CityTileHouse extends CityTile {

    private int secondsToPopulationRatio = 5;

    private int secondsToIncomeRatio = 5;

    @Override
    public void updateForTime(int secondsSinceLastUpdate, City parentCity) {
        if(parentCity.getUnemployment() < CityBuilder.getInstance().DAMAGING_UNEMPLOYMENT_RATE) {
            int populationIncreaseForTime = secondsSinceLastUpdate / this.secondsToPopulationRatio;

            parentCity.modifyPopulation(populationIncreaseForTime);

            int incomeIncreaseForTime = secondsSinceLastUpdate / this.secondsToIncomeRatio;

            parentCity.modifyFunds(incomeIncreaseForTime);
        }
    }

}
