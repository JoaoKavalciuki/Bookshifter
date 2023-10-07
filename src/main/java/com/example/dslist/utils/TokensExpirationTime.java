package com.example.dslist.utils;

import java.util.Calendar;
import java.util.Date;

public class TokensExpirationTime {
    private static final int EXPIRATION_TIME = 5;

    public static Date getExpirantionTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
