package com.discordcity.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class TimeUtil {

    private static final TimeUtil INSTANCE = new TimeUtil();

    public Timestamp getCurrentTime() {
        return Timestamp.from(Instant.now());
    }

    public static TimeUtil getInstance() {
        return INSTANCE;
    }
}
