package com.discordcity.city.tile.impl;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.tile.CityTile;

public class CityTileHouse extends CityTile {

    private int secondsToPopulationRatio = 5;

    private int secondsToIncomeRatio = 5;

    @Override
    public boolean updateForTime(int secondsSinceLastUpdate, City parentCity) {
        if(parentCity.getUnemployment() < CityBuilder.getInstance().DAMAGING_UNEMPLOYMENT_RATE) {
            int populationIncreaseForTime = this.getSecondsSinceLastValidTileUpdate() / this.secondsToPopulationRatio;

            parentCity.modifyPopulation(populationIncreaseForTime);

            int incomeIncreaseForTime = this.getSecondsSinceLastValidTileUpdate() / this.secondsToIncomeRatio;

            parentCity.modifyFunds(incomeIncreaseForTime);

            boolean validTileUpdate = (populationIncreaseForTime > 0 && incomeIncreaseForTime > 0);
            return validTileUpdate;
        }

        return false;
    }

}
