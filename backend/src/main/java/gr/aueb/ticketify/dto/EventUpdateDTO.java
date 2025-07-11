package gr.aueb.ticketify.dto;

import gr.aueb.ticketify.core.enums.EventStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventUpdateDTO {

    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    @FutureOrPresent(message = "Event date and time must be in the future or present")
    private LocalDateTime eventDateTime;

    private Long regionId;

    @Min(value = 1, message = "Total tickets must be at least 1")
    private Integer totalTickets;

    private EventStatus status;

    @DecimalMin("0.0")
    private BigDecimal price;
}
