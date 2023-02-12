package com.jpmc.theatre.booking;

import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.listing.Showing;
import com.jpmc.theatre.pricing.FeeCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingService {

    private final ScheduleService scheduleService;

    private final FeeCalculator feeCalculator;

    public BookingService(ScheduleService scheduleService, FeeCalculator feeCalculator) {
        this.scheduleService = scheduleService;
        this.feeCalculator = feeCalculator;
    }

    public Reservation makeBooking(Customer customer, LocalDateTime movieDate, int movieSequence, int howManyTickets) {
        Schedule schedule = scheduleService.getScheduleForDate(movieDate);
        Showing showing = schedule.getShowingForSequence(movieSequence);
        BigDecimal totalFee = feeCalculator.calculateFee(showing, howManyTickets);
        return new Reservation(customer, showing, howManyTickets, totalFee);
    }
}
