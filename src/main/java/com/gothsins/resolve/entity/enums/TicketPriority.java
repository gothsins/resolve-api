package com.gothsins.resolve.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

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
