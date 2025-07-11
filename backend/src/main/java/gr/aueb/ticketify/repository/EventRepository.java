package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByRegionId(Long regionId);
    List<Event> findByEventStatus(EventStatus status);
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByRegionIdAndStatus(Long regionId, EventStatus status);
    List<Event> findByDateAfter(LocalDate date);
    @Query("SELECT e FROM Event e WHERE e.availableTickets > 0")
    List<Event> findAvailableEvents();
    @Query("SELECT e.availableTickets FROM Event e WHERE e.id = :eventId")
    Integer findAvailableTicketsByEventId(@Param("eventId") Long eventId);
}
