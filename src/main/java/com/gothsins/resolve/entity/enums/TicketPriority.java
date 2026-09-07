package com.gothsins.resolve.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.Duration;

public enum TicketPriority {
    LOW(Duration.ofHours(72)),
    MEDIUM(Duration.ofHours(48)),
    HIGH(Duration.ofHours(24)),
    CRITICAL(Duration.ofHours(12));

    private final Duration slaDuration;

    TicketPriority(Duration slaDuration) {
        this.slaDuration = slaDuration;
    }

    public Duration getSlaDuration() {
        return slaDuration;
    }

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static TicketPriority from(String value) {
        if (value == null) return null;
        return TicketPriority.valueOf(value.trim().toUpperCase());
    }
}
