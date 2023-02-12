package com.jpmc.theatre.listing;

import com.jpmc.theatre.film.Movie;
import com.jpmc.theatre.pricing.FeeCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SchedulePrinterTest {


    @Test
    void whenPrintingInSimpleFormatSeeExpectedShowing() {
        BigDecimal firstMoviePrice = BigDecimal.TEN;
        Showing firstMovie = Showing.builder()
                .movie(Movie.builder()
                        .title("First Movie")
                        .runningTime(Duration.ofMinutes(120))
                        .ticketPrice(firstMoviePrice)
                        .build())
                .showStartTime(LocalDateTime.of(2023, 2, 10, 10, 0))
                .build();
        BigDecimal secondMoviePrice = BigDecimal.ONE;
        Showing secondMovie = Showing.builder()
                .movie(Movie.builder()
                        .title("Second Movie")
                        .runningTime(Duration.ofMinutes(10))
                        .ticketPrice(secondMoviePrice)
                        .build())
                .showStartTime(LocalDateTime.of(2023, 2, 10, 12, 0))
                .build();
        FeeCalculator feeCalculator = mock(FeeCalculator.class);
        when(feeCalculator.calculateFee(firstMovie)).thenReturn(firstMoviePrice);
        when(feeCalculator.calculateFee(secondMovie)).thenReturn(secondMoviePrice);
        SchedulePrinter schedulePrinter = new SchedulePrinter(feeCalculator);


        Schedule schedule = new Schedule(List.of(firstMovie, secondMovie));
        String schedulePrint = schedulePrinter.printSimple(schedule);

        assertThat(schedulePrint, equalTo(
                """
                        Schedule for date: 2023-02-10
                        ===================================================
                        1: 2023-02-10T10:00 First Movie (2 hours 0 minutes) $10
                        2: 2023-02-10T12:00 Second Movie (0 hours 10 minutes) $1
                        ==================================================="""
        ));
    }
}