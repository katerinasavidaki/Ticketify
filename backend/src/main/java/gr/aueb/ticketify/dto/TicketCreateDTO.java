package gr.aueb.ticketify.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCreateDTO {

    @NotNull(message = "Event is required")
    private Long eventId;
}
