package com.discordcity.city;

import java.util.HashMap;

public class CityCache {

    private static final CityCache INSTANCE = new CityCache();

    private HashMap<String, City> cachedCities = new HashMap<String, City>();

    public void addCityToCache(String ownerUserId, City city) {
        this.cachedCities.put(ownerUserId, city);
    }

    public City getCityFromCache(String ownerUserId) {
        return this.cachedCities.get(ownerUserId);
    }

    public boolean isCityCached(String ownerUserId) {
        return this.cachedCities.containsKey(ownerUserId);
    }

    public static CityCache getInstance() {
        return INSTANCE;
    }

}