package com.discordcity.city.tile;

import com.discordcity.city.City;

public abstract class CityTile {

    public abstract void updateForTime(int secondsSinceLastUpdate, City parentCity);

}
