package com.jpmc.theatre.listing;

import com.jpmc.theatre.LocalDateProvider;
import com.jpmc.theatre.film.Movie;
import com.jpmc.theatre.film.Trait;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleFixture {
    //Mock class that should be replaced by a real class that has access to a database to obtain the different schedules

    private final Map<LocalDate, Schedule> scheduleFixture;

    public ScheduleFixture() {
        scheduleFixture = new HashMap<>();

        LocalDateProvider localDateProvider = LocalDateProvider.singleton();

        Movie spiderMan = Movie.builder().title("Spider-Man: No Way Home")
                .runningTime(Duration.ofMinutes(90))
                .ticketPrice(BigDecimal.valueOf(12.5))
                .traits(List.of(Trait.SPECIAL))
                .build();
        Movie turningRed = Movie.builder().title("Turning Red")
                .runningTime(Duration.ofMinutes(85))
                .ticketPrice(BigDecimal.valueOf(11))
                .build();
        Movie theBatMan = Movie.builder().title("The Batman")
                .runningTime(Duration.ofMinutes(95))
                .ticketPrice(BigDecimal.valueOf(9))
                .build();
        LocalDate currentDate = localDateProvider.currentDate();

        scheduleFixture.put(currentDate, new Schedule(List.of(
                new Showing(turningRed, LocalDateTime.of(currentDate, LocalTime.of(9, 0))),
                new Showing(spiderMan, LocalDateTime.of(currentDate, LocalTime.of(11, 0))),
                new Showing(theBatMan, LocalDateTime.of(currentDate, LocalTime.of(12, 50))),
                new Showing(turningRed, LocalDateTime.of(currentDate, LocalTime.of(14, 30))),
                new Showing(spiderMan, LocalDateTime.of(currentDate, LocalTime.of(16, 10))),
                new Showing(theBatMan, LocalDateTime.of(currentDate, LocalTime.of(17, 50))),
                new Showing(turningRed, LocalDateTime.of(currentDate, LocalTime.of(19, 30))),
                new Showing(spiderMan, LocalDateTime.of(currentDate, LocalTime.of(21, 10))),
                new Showing(theBatMan, LocalDateTime.of(currentDate, LocalTime.of(23, 0)))
        )));
    }

    public Schedule getShowingsForDate(LocalDate date) {
        return scheduleFixture.get(date);
    }
}
