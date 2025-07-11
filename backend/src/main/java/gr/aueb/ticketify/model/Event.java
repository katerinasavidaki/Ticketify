package gr.aueb.ticketify.model;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.core.enums.TicketStatus;
import gr.aueb.ticketify.model.static_data.Region;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@Builder
public class Event extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime dateTime;

    private Integer totalTickets;

    private BigDecimal price;

    private Integer availableTickets;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status", nullable = false)
    private EventStatus eventStatus = EventStatus.ACTIVE;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Getter(AccessLevel.PRIVATE)
    private Set<Ticket> tickets = new HashSet<>();

    public Set<Ticket> getAllTickets() {
        return Collections.unmodifiableSet(tickets);
    }

    public void addTicket(Ticket ticket) {
        if (tickets == null) tickets = new HashSet<>();
        tickets.add(ticket);
        ticket.setEvent(this);
    }

    public void removeTicket(Ticket ticket) {
        if (tickets == null) tickets = new HashSet<>();
        tickets.remove(ticket);
        ticket.setEvent(null);
    }

    public int getTicketsRemaining() {
        return totalTickets - (int) tickets.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.BOOKED)
                .count();
    }
}
