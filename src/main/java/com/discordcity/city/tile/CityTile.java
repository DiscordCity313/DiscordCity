package com.discordcity.city.tile;

import com.discordcity.city.City;
import com.discordcity.util.TimeUtil;

import java.sql.Timestamp;
import java.time.Instant;

public abstract class CityTile {

    private Timestamp lastValidTileUpdate;

    public CityTile() {
        this.resetTileUpdateTime();
    }

    /**
     * @return Whether there was a valid tile update
     */
    public abstract boolean updateForTime(int secondsSinceLastCityUpdate, City parentCity);

    public void resetTileUpdateTime() {
        this.lastValidTileUpdate = Timestamp.from(Instant.now());
    }

    public Timestamp getLastValidTileUpdate() {
        return this.lastValidTileUpdate;
    }

    public int getSecondsSinceLastValidTileUpdate() {
        Timestamp currentTime = TimeUtil.getInstance().getCurrentTime();

        long differenceMillis = currentTime.getTime() - this.getLastValidTileUpdate().getTime();

        return (int) differenceMillis / 1000;
    }

}
