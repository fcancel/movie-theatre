package com.jpmc.theatre.listing;

import com.jpmc.theatre.LocalDateProvider;
import com.jpmc.theatre.pricing.FeeCalculator;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SchedulePrinter {

    public static final String NEWLINE = "\n";
    public static final String LINE = "===================================================";
    private final FeeCalculator feeCalculator;

    public SchedulePrinter(FeeCalculator feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    public String printSimple(Schedule schedule) {

        StringBuilder message = new StringBuilder();

        message.append(String.format("Schedule for date: %s", schedule.showings().get(0).getStartTime().toLocalDate()))
                .append(NEWLINE)
                .append(LINE)
                .append(NEWLINE);

        List<Showing> showings = schedule.showings();
        List<DetailedSchedule> detailedSchedules = getDetailedSchedules(showings);

        detailedSchedules.forEach(ds -> {
            message.append(String.format("%d: %s %s %s $%s", ds.movieSequence(), ds.startTime(), ds.movieTitle(), humanReadableFormat(ds.duration()), ds.price()))
                    .append(NEWLINE);
        });
        message.append(LINE);

        return message.toString();
    }

    private List<DetailedSchedule> getDetailedSchedules(List<Showing> showings) {
        List<DetailedSchedule> detailedSchedules = IntStream.range(0, showings.size()).mapToObj(index -> {
            Showing showing = showings.get(index);

            return DetailedSchedule.builder()
                    .movieSequence(index + 1)
                    .startTime(showing.getStartTime())
                    .movieTitle(showing.getMovie().getTitle())
                    .duration(showing.getMovie().getRunningTime())
                    .price(feeCalculator.calculateFee(showing))
                    .build();
        }).toList();
        return detailedSchedules;
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
    private record DetailedSchedule(int movieSequence, LocalDateTime startTime, String movieTitle, Duration duration,
                                    BigDecimal price) {

    }
}
