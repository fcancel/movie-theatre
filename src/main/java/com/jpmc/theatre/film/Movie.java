package com.jpmc.theatre.film;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class Movie {
    private String title;
    private String description;
    private Duration runningTime;
    private BigDecimal ticketPrice;
    private int specialCode;

    @Builder.Default
    private List<Trait> traits = Collections.emptyList();
}