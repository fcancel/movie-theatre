package com.jpmc.theatre.pricing;

import com.jpmc.theatre.film.Movie;
import com.jpmc.theatre.film.Trait;
import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.listing.Showing;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FeeCalculatorTest {

    @Test
    void givenMovieWithOnlySpecialDiscountHave20PercentOffTotalPrice() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        Showing showing = Showing.builder()
                .movie(Movie.builder()
                        .ticketPrice(BigDecimal.valueOf(12.5))
                        .traits(List.of(Trait.SPECIAL))
                        .build())
                .showStartTime(LocalDate.now().atTime(20, 0))
                .build();
        when(schedule.getSequenceForShowing(showing)).thenReturn(5);
        when(scheduleService.getScheduleForDate(any())).thenReturn(schedule);
        FeeCalculator feeCalculator = new FeeCalculator(scheduleService);

        BigDecimal fee = feeCalculator.calculateFee(showing, 4);
        assertThat(BigDecimal.valueOf(40).compareTo(fee), equalTo(0));
    }

    @Test
    void givenMovieFirstShowingApplyThreePoundDiscount() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        Showing showing = Showing.builder()
                .movie(Movie.builder()
                        .ticketPrice(BigDecimal.valueOf(10))
                        .build())
                .showStartTime(LocalDate.now().atTime(20, 0))
                .build();
        when(schedule.getSequenceForShowing(showing)).thenReturn(1);
        when(scheduleService.getScheduleForDate(any())).thenReturn(schedule);
        FeeCalculator feeCalculator = new FeeCalculator(scheduleService);

        BigDecimal fee = feeCalculator.calculateFee(showing, 1);
        assertThat(BigDecimal.valueOf(7).compareTo(fee), equalTo(0));
    }

    @Test
    void givenMovieSecondShowingApplyTwoPoundDiscount() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        Showing showing = Showing.builder()
                .movie(Movie.builder()
                        .ticketPrice(BigDecimal.valueOf(10))
                        .build())
                .showStartTime(LocalDate.now().atTime(20, 0))
                .build();
        when(schedule.getSequenceForShowing(showing)).thenReturn(2);
        when(scheduleService.getScheduleForDate(any())).thenReturn(schedule);
        FeeCalculator feeCalculator = new FeeCalculator(scheduleService);

        BigDecimal fee = feeCalculator.calculateFee(showing, 1);
        assertThat(BigDecimal.valueOf(8).compareTo(fee), equalTo(0));
    }

    @Test
    void givenMovieSeventhShowingApplyOnePoundDiscount() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        Showing showing = Showing.builder()
                .movie(Movie.builder()
                        .ticketPrice(BigDecimal.valueOf(10))
                        .build())
                .showStartTime(LocalDate.now().atTime(20, 0))
                .build();
        when(schedule.getSequenceForShowing(showing)).thenReturn(7);
        when(scheduleService.getScheduleForDate(any())).thenReturn(schedule);
        FeeCalculator feeCalculator = new FeeCalculator(scheduleService);

        BigDecimal fee = feeCalculator.calculateFee(showing, 1);
        assertThat(BigDecimal.valueOf(9).compareTo(fee), equalTo(0));
    }

    @Test
    void givenMovieShowingBetween11And16ShouldHave25PercentOffTotalPrice() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        Showing showing = Showing.builder()
                .movie(Movie.builder()
                        .ticketPrice(BigDecimal.TEN)
                        .build())
                .showStartTime(LocalDateTime.of(2023, 2, 10, 12, 0))
                .build();
        when(schedule.getSequenceForShowing(showing)).thenReturn(10);
        when(scheduleService.getScheduleForDate(any())).thenReturn(schedule);
        FeeCalculator feeCalculator = new FeeCalculator(scheduleService);

        BigDecimal fee = feeCalculator.calculateFee(showing, 1);
        assertThat(BigDecimal.valueOf(7.5).compareTo(fee), equalTo(0));
    }

    @Test
    void givenMovieWithMultipleValidDiscountsOnlyApplyHighest() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        Showing showing = Showing.builder()
                .movie(Movie.builder()
                        .ticketPrice(BigDecimal.valueOf(12.5))
                        .traits(List.of(Trait.SPECIAL))
                        .build())
                .showStartTime(LocalDate.now().atTime(20, 0))
                .build();
        when(schedule.getSequenceForShowing(showing)).thenReturn(1);
        when(scheduleService.getScheduleForDate(any())).thenReturn(schedule);
        FeeCalculator feeCalculator = new FeeCalculator(scheduleService);

        BigDecimal fee = feeCalculator.calculateFee(showing, 1);
        assertThat(BigDecimal.valueOf(9.5).compareTo(fee), equalTo(0));
    }

}