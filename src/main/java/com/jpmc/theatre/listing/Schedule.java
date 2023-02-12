package com.jpmc.theatre.listing;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public record Schedule(List<Showing> showings) {

    public Showing getShowingForSequence(int sequence) {
        int index = sequence - 1;
        try {
            return getShowingsOrderedByAscendingTime().get(index);
        } catch (RuntimeException ex) {
            log.error("Error while trying to get the schedule for sequence {}", sequence);
            throw new IllegalStateException("not able to find any showing for given sequence " + sequence);
        }
    }

    public int getSequenceForShowing(Showing showing) {
        List<Showing> orderedShowings = getShowingsOrderedByAscendingTime();
        return IntStream.range(0, showings.size())
                .filter(i -> showing.equals(orderedShowings.get(i)))
                .findFirst().orElseThrow(() -> new RuntimeException("No valid sequence found for showing " + showing));
    }

    private List<Showing> getShowingsOrderedByAscendingTime() {
        return showings.stream().sorted(Comparator.comparing(Showing::getStartTime)).toList();
    }
}
