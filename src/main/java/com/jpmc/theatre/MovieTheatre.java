package com.jpmc.theatre;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleFixture;
import com.jpmc.theatre.listing.SchedulePrinter;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.pricing.FeeCalculator;

import java.util.TimeZone;

public class MovieTheatre {
    public static void main(String[] args) throws JsonProcessingException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        ScheduleFixture scheduleFixture = new ScheduleFixture();
        SchedulePrinter schedulePrinter = new SchedulePrinter(new FeeCalculator(new ScheduleService(scheduleFixture)));
        Schedule showingsForDate = scheduleFixture.getShowingsForDate(LocalDateProvider.singleton().currentDate());
        System.out.println(schedulePrinter.printSimple(showingsForDate));
        System.out.println(schedulePrinter.printJson(showingsForDate));
    }
}
