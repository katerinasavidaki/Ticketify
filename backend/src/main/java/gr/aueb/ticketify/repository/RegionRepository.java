package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    // Additional query methods can be defined here if needed
}
