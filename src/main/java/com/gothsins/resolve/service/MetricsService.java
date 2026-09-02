package com.gothsins.resolve.service;

import com.gothsins.resolve.entity.enums.TicketPriority;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementTicketCreated(TicketPriority priority) {
        meterRegistry.counter("tickets.created", "priority", priority.name()).increment();
    }

    public void incrementTicketStatusChanged(String oldStatus, String newStatus) {
        meterRegistry.counter("tickets.status.changed",
                "from", oldStatus, "to", newStatus).increment();
    }

    public void incrementUserRegistered() {
        meterRegistry.counter("users.registered").increment();
    }
}