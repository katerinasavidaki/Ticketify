package gr.aueb.ticketify.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventCreateDTO {

    @NotEmpty(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotEmpty(message = "Description is required")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    @NotNull(message = "Date and time are required")
    @FutureOrPresent(message = "Event date and time must be in the future or present")
    private LocalDateTime eventDateTime;

    @NotNull(message = "Price is required")
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull(message = "Region is required")
    private Long regionId;

    @NotNull(message = "Total tickets are required")
    @Min(value = 1, message = "There must be at least one ticket")
    private Integer totalTickets;
}
