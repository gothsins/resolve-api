package com.gothsins.resolve.service;

import com.gothsins.resolve.entity.Ticket;
import com.gothsins.resolve.entity.enums.SlaStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class SlaService {

    private static final double AT_RISK_THRESHOLD = 0.2;

    public SlaStatus calculateStatus(Ticket ticket) {
        LocalDateTime referenceTime = resolveReferenceTime(ticket);

        if (referenceTime.isAfter(ticket.getSlaDeadline())) {
            return SlaStatus.VIOLATED;
        }

        Duration total = ticket.getPriority().getSlaDuration();
        Duration remaining = Duration.between(referenceTime, ticket.getSlaDeadline());

        double remainingRatio = (double) remaining.toMinutes() / total.toMinutes();

        return remainingRatio <= AT_RISK_THRESHOLD ? SlaStatus.AT_RISK : SlaStatus.ON_TIME;
    }

    private LocalDateTime resolveReferenceTime(Ticket ticket) {
        if (ticket.getResolvedAt() != null) {
            return ticket.getResolvedAt();
        }
        if (ticket.getClosedAt() != null) {
            return ticket.getClosedAt();
        }
        return LocalDateTime.now();
    }
}