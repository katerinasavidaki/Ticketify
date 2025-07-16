package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByRegionId(Long regionId);
    List<Event> findByEventStatus(EventStatus status);
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByRegionIdAndEventStatus(Long regionId, EventStatus status);
    List<Event> findByDateTimeAfter(LocalDateTime dateTime);
    @Query("SELECT e FROM Event e WHERE e.availableTickets > 0")
    List<Event> findAvailableEvents();
    @Query("SELECT e.availableTickets FROM Event e WHERE e.id = :eventId")
    Integer findAvailableTicketsByEventId(@Param("eventId") Long eventId);
}
