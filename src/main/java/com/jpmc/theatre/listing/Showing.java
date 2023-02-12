package com.jpmc.theatre.listing;

import com.jpmc.theatre.film.Movie;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Showing {
    private Movie movie;
    private LocalDateTime showStartTime;

    public Showing(Movie movie, LocalDateTime showStartTime) {
        this.movie = movie;
        this.showStartTime = showStartTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return showStartTime;
    }

}
