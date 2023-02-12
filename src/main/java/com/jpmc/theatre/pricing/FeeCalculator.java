package com.jpmc.theatre.pricing;

import com.jpmc.theatre.film.Movie;
import com.jpmc.theatre.film.Trait;
import com.jpmc.theatre.listing.Schedule;
import com.jpmc.theatre.listing.ScheduleService;
import com.jpmc.theatre.listing.Showing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        FeeConditions feeConditions = new FeeConditions(movie.getTicketPrice(), movie.getTraits(), schedule.getSequenceForShowing(showing));
        return calculateFee(feeConditions).multiply(BigDecimal.valueOf(audienceCount));
    }

    private BigDecimal calculateFee(FeeConditions feeConditions) {
        return feeConditions.price().subtract(getDiscount(feeConditions));
    }

    private BigDecimal getDiscount(FeeConditions feeConditions) {
        List<BigDecimal> discounts = new ArrayList<>();

        movieTraitDiscount(feeConditions, discounts);

        movieSequenceDiscount(feeConditions, discounts);

        return getBiggestDiscount(discounts);
    }

    private static BigDecimal getBiggestDiscount(List<BigDecimal> discounts) {
        return discounts.stream().max(Comparator.naturalOrder()).stream().findFirst().orElse(BigDecimal.ZERO);
    }

    private static void movieSequenceDiscount(FeeConditions feeConditions, List<BigDecimal> discounts) {
        if (feeConditions.sequenceOfTheDay() == 1) {
            discounts.add(BigDecimal.valueOf(3)); // $3 discount for 1st show
        } else if (feeConditions.sequenceOfTheDay() == 2) {
            discounts.add(BigDecimal.valueOf(2)); // $2 discount for 2nd show
        }
    }

    private static void movieTraitDiscount(FeeConditions feeConditions, List<BigDecimal> discounts) {
        if (feeConditions.traits().contains(Trait.SPECIAL)) {
            discounts.add(discountOfPercentOfPrice(feeConditions, 20));
        }
    }

    private static BigDecimal discountOfPercentOfPrice(FeeConditions feeConditions, double percent) {
        return feeConditions.price().multiply(BigDecimal.valueOf(percent / 100));
    }

    private record FeeConditions(BigDecimal price, List<Trait> traits, int sequenceOfTheDay) {
    }
}
