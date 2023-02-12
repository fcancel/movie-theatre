package com.jpmc.theatre.listing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

        List<DetailedSchedule> detailedSchedules = getDetailedSchedules(schedule);

        detailedSchedules.forEach(ds -> message
                .append(String.format("%d: %s %s %s $%s", ds.movieSequence(), ds.startTime(), ds.movieTitle(), ds.duration(), ds.price()))
                .append(NEWLINE));
        message.append(LINE);

        return message.toString();
    }

    public String printJson(Schedule schedule) throws JsonProcessingException {
        List<DetailedSchedule> detailedSchedules = getDetailedSchedules(schedule);
        ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(detailedSchedules);
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
    private record DetailedSchedule(int movieSequence, LocalDateTime startTime, String movieTitle, String duration,
                                    BigDecimal price) {

    }
}
