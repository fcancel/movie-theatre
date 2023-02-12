package com.jpmc.theatre.booking;

import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.listing.Showing;
import com.jpmc.theatre.pricing.FeeCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    @Test
    void givenReservationIsMadeReturnReservationDetails() {
        LocalDateTime reservationDate = LocalDateTime.now();
        ScheduleService scheduleService = mock(ScheduleService.class);
        Schedule schedule = mock(Schedule.class);
        int movieSequence = 3;
        Showing expectedShowing = mock(Showing.class);
        when(schedule.getShowingForSequence(movieSequence)).thenReturn(expectedShowing);
        when(scheduleService.getScheduleForDate(reservationDate)).thenReturn(schedule);

        int expectedTickets = 5;
        FeeCalculator feeCalculator = mock(FeeCalculator.class);
        BigDecimal expectedTotalFee = BigDecimal.TEN;
        when(feeCalculator.calculateFee(expectedShowing, expectedTickets)).thenReturn(expectedTotalFee);
        BookingService bookingService = new BookingService(scheduleService, feeCalculator);

        Customer customer = mock(Customer.class);
        Reservation reservation = bookingService.makeBooking(customer, reservationDate, movieSequence, expectedTickets);
        Reservation expectedReservation = new Reservation(customer, expectedShowing, expectedTickets, expectedTotalFee);

        assertThat(reservation, equalTo(expectedReservation));
    }

}