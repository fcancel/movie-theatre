package com.jpmc.theatre;

import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleFixture;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.listing.print.*;
import com.jpmc.theatre.pricing.FeeCalculator;
import com.jpmc.theatre.util.LocalDateProvider;

import java.util.Map;
import java.util.TimeZone;

public class MovieTheatre {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        ScheduleFixture scheduleFixture = new ScheduleFixture();
        Map<PrinterType, Printer> printers = Map.of(PrinterType.SIMPLE, new SimplePrinter(),
                PrinterType.JSON, new JsonPrinter());
        SchedulePrinterService schedulePrinterService = new SchedulePrinterService(new FeeCalculator(new ScheduleService(scheduleFixture)), printers);
        Schedule showingsForDate = scheduleFixture.getShowingsForDate(LocalDateProvider.singleton().currentDate());
        printers.keySet().forEach(k ->
                System.out.println(schedulePrinterService.printSchedule(k, showingsForDate)));
    }
}
