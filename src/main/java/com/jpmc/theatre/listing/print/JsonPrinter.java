package com.jpmc.theatre.listing.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.util.List;

public class JsonPrinter implements Printer{
    @SneakyThrows
    @Override
    public String print(List<SchedulePrinterService.DetailedSchedule> detailedSchedules) {
        ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(detailedSchedules);
    }
}
