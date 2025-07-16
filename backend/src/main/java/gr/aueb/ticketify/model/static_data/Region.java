package gr.aueb.ticketify.model.static_data;

import gr.aueb.ticketify.model.Event;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "regions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    @Getter(AccessLevel.PRIVATE)
    private Set<Event> events = new HashSet<>();

    public Set<Event> getAllEvents() {
        return Collections.unmodifiableSet(events);
    }

    public void addEvent(Event event) {
        if (events == null) events = new HashSet<>();
        events.add(event);
        event.setRegion(this);
    }

    public void removeEvent(Event event) {
        if (events == null) events = new HashSet<>();
        events.remove(event);
        event.setRegion(null);
    }
}
