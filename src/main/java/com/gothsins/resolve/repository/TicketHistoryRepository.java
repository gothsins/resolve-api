package com.gothsins.resolve.repository;

import com.gothsins.resolve.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    @Query("select th from TicketHistory th " +
           "join fetch th.ticket t " +
           "join fetch t.requester r " +
           "left join fetch t.assignedAgent a " +
           "where th.id = :id")
    Optional<TicketHistory> findByWithTicket(@Param("id") Long id);
    List<TicketHistory> findByTicketIdOrderByCreatedAtDesc(Long ticketId);
}
