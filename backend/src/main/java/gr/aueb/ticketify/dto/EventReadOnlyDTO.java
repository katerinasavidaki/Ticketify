package gr.aueb.ticketify.dto;

import gr.aueb.ticketify.core.enums.EventStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventReadOnlyDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private BigDecimal price;
    private String regionName;
    private Integer totalTickets;
    private Integer availableTickets;
    private EventStatus status;
}
