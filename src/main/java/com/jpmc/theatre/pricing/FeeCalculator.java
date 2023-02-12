package com.jpmc.theatre.pricing;

import com.jpmc.theatre.film.Movie;
import com.jpmc.theatre.film.Trait;
import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.listing.Showing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeeCalculator {

    private final ScheduleService scheduleService;

    public FeeCalculator(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    public BigDecimal calculateFee(Showing showing) {
        return calculateFee(showing, 1);
    }

    public BigDecimal calculateFee(Showing showing, int audienceCount) {
        Movie movie = showing.getMovie();
        LocalDateTime startTime = showing.getStartTime();
        Schedule schedule = scheduleService.getScheduleForDate(startTime);
        FeeConditions feeConditions = new FeeConditions(movie.getTicketPrice(), movie.getTraits(), schedule.getSequenceForShowing(showing), showing.getStartTime());

        return calculateFee(feeConditions).multiply(BigDecimal.valueOf(audienceCount));
    }

    private BigDecimal calculateFee(FeeConditions feeConditions) {
        BigDecimal discount = getDiscount(feeConditions);
        return feeConditions.price().subtract(discount);
    }

    private BigDecimal getDiscount(FeeConditions feeConditions) {
        List<BigDecimal> discounts = new ArrayList<>();

        movieTraitDiscount(feeConditions, discounts);

        movieSequenceDiscount(feeConditions, discounts);

        movieTimeDiscount(feeConditions, discounts);

        return getBiggestDiscount(discounts);
    }

    private void movieTimeDiscount(FeeConditions feeConditions, List<BigDecimal> discounts) {
        if(isAfterEleven(feeConditions) && isBeforeSixteen(feeConditions)) {
            discounts.add(discountOfPercentOfPrice(feeConditions, 25));
        }
    }

    private static boolean isAfterEleven(FeeConditions feeConditions) {
        return feeConditions.startTime().toLocalTime().isAfter(LocalTime.of(11, 0));
    }
    private static boolean isBeforeSixteen(FeeConditions feeConditions) {
        return feeConditions.startTime().toLocalTime().isBefore(LocalTime.of(16, 0));
    }

    private static void movieSequenceDiscount(FeeConditions feeConditions, List<BigDecimal> discounts) {
        switch (feeConditions.sequenceOfTheDay()) {
            case 1 -> discounts.add(BigDecimal.valueOf(3));
            case 2 -> discounts.add(BigDecimal.valueOf(2));
            case 7 -> discounts.add(BigDecimal.valueOf(1));
        }
    }

    private static BigDecimal getBiggestDiscount(List<BigDecimal> discounts) {
        return discounts.stream().max(Comparator.naturalOrder()).stream().findFirst().orElse(BigDecimal.ZERO);
    }

    private static void movieTraitDiscount(FeeConditions feeConditions, List<BigDecimal> discounts) {
        if (feeConditions.traits().contains(Trait.SPECIAL)) {
            discounts.add(discountOfPercentOfPrice(feeConditions, 20));
        }
    }

    private static BigDecimal discountOfPercentOfPrice(FeeConditions feeConditions, double percent) {
        return feeConditions.price().multiply(BigDecimal.valueOf(percent / 100));
    }

    private record FeeConditions(BigDecimal price, List<Trait> traits, int sequenceOfTheDay, LocalDateTime startTime) {
    }
}
