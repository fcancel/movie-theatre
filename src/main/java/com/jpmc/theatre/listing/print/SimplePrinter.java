package com.jpmc.theatre.listing.print;

import java.util.List;

public class SimplePrinter implements Printer{

    private static final String NEWLINE = "\n";
    private static final String LINE = "===================================================";
    @Override
    public String print(List<SchedulePrinterService.DetailedSchedule> detailedSchedules) {
        StringBuilder message = new StringBuilder();

        message.append(String.format("Schedule for date: %s", detailedSchedules.get(0).startTime().toLocalDate()))
                .append(NEWLINE)
                .append(LINE)
                .append(NEWLINE);

        detailedSchedules.forEach(ds -> message
                .append(String.format("%d: %s %s %s $%s", ds.movieSequence(), ds.startTime(), ds.movieTitle(), ds.duration(), ds.price()))
                .append(NEWLINE));
        message.append(LINE);

        return message.toString();
    }
}
