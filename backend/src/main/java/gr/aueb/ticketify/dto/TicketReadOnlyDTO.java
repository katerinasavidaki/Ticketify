package gr.aueb.ticketify.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketReadOnlyDTO {

    private Long id;
    private String eventTitle;
    private String regionName;
    private LocalDateTime eventDateTime;
    private LocalDateTime bookingDateTime;
    private BigDecimal price;
    private Boolean cancelled;
}
