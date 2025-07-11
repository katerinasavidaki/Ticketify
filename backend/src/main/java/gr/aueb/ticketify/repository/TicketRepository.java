package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.core.enums.TicketStatus;
import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.Ticket;
import gr.aueb.ticketify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);
    List<Ticket> findByEvent(Event event);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByIsCancelled(Boolean isCancelled);
    boolean existsByUserAndEvent(User user, Event event);
    @Query("SELECT t FROM Ticket t JOIN FETCH t.event WHERE t.user.id = :userId")
    List<Ticket> findByUserIdWithEvent(@Param("userId") Long userId);
}
