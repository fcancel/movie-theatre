package com.jpmc.theatre;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheatreTests {
    @Test
    void totalFeeForCustomer() {
        Theatre theatre = new Theatre(LocalDateProvider.singleton());
        Customer john = new Customer("John Doe", "id-12345");
        Reservation reservation = theatre.reserve(john, 2, 4);
//        System.out.println("You have to pay " + reservation.getTotalFee());
        assertEquals(reservation.totalFee(), 50);
    }

    @Test
    void printMovieSchedule() {
        Theatre theatre = new Theatre(LocalDateProvider.singleton());
        theatre.printSchedule();
    }
}
