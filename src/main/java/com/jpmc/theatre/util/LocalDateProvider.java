package com.jpmc.theatre.util;

import java.time.LocalDate;

public class LocalDateProvider {
    private static LocalDateProvider instance = null;

    public static LocalDateProvider singleton() {
        if (instance == null) {
            instance = new LocalDateProvider();
        }
        return instance;
    }

    public LocalDate currentDate() {
        return LocalDate.now();
    }
}
