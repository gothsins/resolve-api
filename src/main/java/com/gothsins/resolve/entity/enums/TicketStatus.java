package com.gothsins.resolve.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static TicketStatus from(String value) {
        if (value == null) return null;
        return TicketStatus.valueOf(value.trim().toUpperCase());
    }
}
