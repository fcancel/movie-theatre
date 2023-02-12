package com.jpmc.theatre.listing.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpmc.theatre.film.Movie;
import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.Showing;
import com.jpmc.theatre.pricing.FeeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SchedulePrinterServiceTest {

    private Schedule schedule;

    private SchedulePrinterService schedulePrinterService;

    @BeforeEach
    void setUp() {
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
                        .runningTime(Duration.ofMinutes(1))
                        .ticketPrice(secondMoviePrice)
                        .build())
                .showStartTime(LocalDateTime.of(2023, 2, 10, 12, 0))
                .build();
        FeeCalculator feeCalculator = mock(FeeCalculator.class);
        when(feeCalculator.calculateFee(firstMovie)).thenReturn(firstMoviePrice);
        when(feeCalculator.calculateFee(secondMovie)).thenReturn(secondMoviePrice);
        schedulePrinterService = new SchedulePrinterService(feeCalculator, Map.of(PrinterType.SIMPLE, new SimplePrinter(), PrinterType.JSON, new JsonPrinter()));


        schedule = new Schedule(List.of(firstMovie, secondMovie));
    }


    @Test
    void whenPrintingInSimpleFormatSeeExpectedShowing() {
        String schedulePrint = schedulePrinterService.printSchedule(PrinterType.SIMPLE, schedule);

        assertThat(schedulePrint, equalTo(
                """
                        Schedule for date: 2023-02-10
                        ===================================================
                        1: 2023-02-10T10:00 First Movie (2 hours 0 minutes) $10
                        2: 2023-02-10T12:00 Second Movie (0 hours 1 minute) $1
                        ==================================================="""
        ));
    }

    @Test
    void whenPrintingInJsonFormatSeeExpectedShowing() throws JsonProcessingException {
        String schedulePrint = schedulePrinterService.printSchedule(PrinterType.JSON, schedule);

        assertThat(schedulePrint, equalTo(
                """
                        [ {\r
                          \"movieSequence\" : 1,\r
                          \"startTime\" : [ 2023, 2, 10, 10, 0 ],\r
                          \"movieTitle\" : \"First Movie\",\r
                          \"duration\" : \"(2 hours 0 minutes)\",\r
                          \"price\" : 10\r
                        }, {\r
                          \"movieSequence\" : 2,\r
                          \"startTime\" : [ 2023, 2, 10, 12, 0 ],\r
                          \"movieTitle\" : \"Second Movie\",\r
                          \"duration\" : \"(0 hours 1 minute)\",\r
                          \"price\" : 1\r
                        } ]"""
        ));
    }


}