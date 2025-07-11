package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByRegion(Region region);
}
