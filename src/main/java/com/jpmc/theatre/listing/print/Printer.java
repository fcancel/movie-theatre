package com.jpmc.theatre.listing.print;

import java.util.List;

public interface Printer {

    String print(List<SchedulePrinterService.DetailedSchedule> detailedSchedules);
}
