package com.gothsins.resolve.repository;

import com.gothsins.resolve.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
