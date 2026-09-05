package com.gothsins.resolve.service;

import com.gothsins.resolve.entity.Ticket;
import com.gothsins.resolve.entity.enums.SlaStatus;
import com.gothsins.resolve.entity.enums.TicketPriority;
import com.gothsins.resolve.repository.TicketRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final TicketRepository ticketRepository;
    private final SlaService slaService;

    public MetricsService(MeterRegistry meterRegistry, TicketRepository ticketRepository, SlaService slaService) {
        this.meterRegistry = meterRegistry;
        this.ticketRepository = ticketRepository;
        this.slaService = slaService;

        Gauge.builder("tickets.sla.at_risk", this, MetricsService::countAtRiskTickets)
                .description("Tickets abertos em risco de violar o SLA")
                .register(meterRegistry);

        Gauge.builder("tickets.sla.violated", this, MetricsService::countViolatedTickets)
                .description("Tickets abertos que já violaram o SLA")
                .register(meterRegistry);
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

    private double countAtRiskTickets() {
        return countOpenTicketsByStatus(SlaStatus.AT_RISK);
    }

    private double countViolatedTickets() {
        return countOpenTicketsByStatus(SlaStatus.VIOLATED);
    }

    private long countOpenTicketsByStatus(SlaStatus targetStatus) {
        return ticketRepository.findAll().stream()
                .filter(t -> t.getResolvedAt() == null && t.getClosedAt() == null)
                .filter(t -> slaService.calculateStatus(t) == targetStatus)
                .count();
    }
}