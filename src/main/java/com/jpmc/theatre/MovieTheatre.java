package com.jpmc.theatre;

import com.jpmc.theatre.listing.ScheduleFixture;
import com.jpmc.theatre.listing.SchedulePrinter;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.pricing.FeeCalculator;

import java.util.TimeZone;

public class MovieTheatre {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        ScheduleFixture scheduleFixture = new ScheduleFixture();
        SchedulePrinter schedulePrinter = new SchedulePrinter(new FeeCalculator(new ScheduleService(scheduleFixture)));
        System.out.println(schedulePrinter.printSimple(scheduleFixture.getShowingsForDate(LocalDateProvider.singleton().currentDate())));
    }
}
