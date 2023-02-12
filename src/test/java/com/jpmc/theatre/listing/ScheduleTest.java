package com.jpmc.theatre.listing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ScheduleTest {

    @Test
    void givenMoviesWithDifferentSchedulesObtainCorrectOrder() {
        LocalDateTime startTimeFirstMovie = LocalDateTime.of(2023, 2, 10, 9, 0);
        Showing firstMovie = Showing.builder()
                .showStartTime(startTimeFirstMovie)
                .build();
        Showing secondMovie = Showing.builder()
                .showStartTime(startTimeFirstMovie.plusHours(1))
                .build();
        Showing thirdMovie = Showing.builder()
                .showStartTime(startTimeFirstMovie.plusHours(2))
                .build();
        Schedule schedule = new Schedule(List.of(thirdMovie, firstMovie, secondMovie));

        assertThat(schedule.getShowingForSequence(1), equalTo(firstMovie));
        assertThat(schedule.getShowingForSequence(2), equalTo(secondMovie));
        assertThat(schedule.getShowingForSequence(3), equalTo(thirdMovie));
    }

}