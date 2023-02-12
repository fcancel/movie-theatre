package com.jpmc.theatre.listing;

import java.time.LocalDateTime;

public class ScheduleService {


    private final ScheduleFixture scheduleFixture;

    public ScheduleService(ScheduleFixture scheduleFixture) {
        this.scheduleFixture = scheduleFixture;
    }

    public Schedule getScheduleForDate(LocalDateTime dateTime) {
        return scheduleFixture.getShowingsForDate(dateTime.toLocalDate());
    }

}
