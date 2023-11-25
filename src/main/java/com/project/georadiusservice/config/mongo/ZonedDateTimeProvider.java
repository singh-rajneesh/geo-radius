package com.project.georadiusservice.config.mongo;

import org.springframework.data.auditing.DateTimeProvider;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class ZonedDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
