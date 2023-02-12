package com.jpmc.theatre.booking;

import com.jpmc.theatre.listing.Showing;

import java.math.BigDecimal;

public record Reservation(Customer customer, Showing showing, int audienceCount, BigDecimal totalFee) {
}