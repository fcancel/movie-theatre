package com.jpmc.theatre.listing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScheduleServiceTest {

    @Test
    void givenSearchForSpecificDateScheduleReturnExpectedSchedule() {
        ScheduleFixture scheduleFixture = mock(ScheduleFixture.class);
        LocalDateTime dateOfSchedule = LocalDateTime.now();
        Schedule expectedSchedule = mock(Schedule.class);
        when(scheduleFixture.getShowingsForDate(dateOfSchedule.toLocalDate())).thenReturn(expectedSchedule);
        ScheduleService scheduleService = new ScheduleService(scheduleFixture);

        Schedule scheduleForDate = scheduleService.getScheduleForDate(dateOfSchedule);

        assertThat(scheduleForDate, equalTo(expectedSchedule));
    }

}