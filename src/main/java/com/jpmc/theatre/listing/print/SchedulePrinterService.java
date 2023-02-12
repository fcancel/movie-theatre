package com.jpmc.theatre.listing.print;

import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.Showing;
import com.jpmc.theatre.pricing.FeeCalculator;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SchedulePrinterService {


    private final FeeCalculator feeCalculator;

    private final Map<PrinterType, Printer> printers;

    public SchedulePrinterService(FeeCalculator feeCalculator, Map<PrinterType, Printer> printers) {
        this.feeCalculator = feeCalculator;
        this.printers = printers;
    }

    public String printSchedule(PrinterType printerType, Schedule schedule) {
        List<DetailedSchedule> detailedSchedules = getDetailedSchedules(schedule);
        return printers.get(printerType).print(detailedSchedules);
    }


    private List<DetailedSchedule> getDetailedSchedules(Schedule schedule) {
        List<Showing> showings = schedule.showings();
        return IntStream.range(0, showings.size()).mapToObj(index -> {
            Showing showing = showings.get(index);

            return DetailedSchedule.builder()
                    .movieSequence(index + 1)
                    .startTime(showing.getStartTime())
                    .movieTitle(showing.getMovie().getTitle())
                    .duration(humanReadableFormat(showing.getMovie().getRunningTime()))
                    .price(feeCalculator.calculateFee(showing))
                    .build();
        }).toList();
    }

    private String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        if (value == 1) {
            return "";
        } else {
            return "s";
        }
    }

    @Builder
    protected record DetailedSchedule(int movieSequence, LocalDateTime startTime, String movieTitle, String duration,
                                    BigDecimal price) {

    }
}
