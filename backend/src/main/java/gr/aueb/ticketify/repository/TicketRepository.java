package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.core.enums.TicketStatus;
import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.Ticket;
import gr.aueb.ticketify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByEventId(Long eventId);
    List<Ticket> findByStatus(TicketStatus status);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    @Query("SELECT t FROM Ticket t JOIN FETCH t.event WHERE t.user.id = :userId")
    List<Ticket> findByUserIdWithEvent(@Param("userId") Long userId);
    @Query("SELECT t FROM Ticket t JOIN FETCH t.event JOIN FETCH t.user WHERE t.id = :id")
    Optional<Ticket> findByIdWithEventAndUser(@Param("id") Long id);
}
