package gr.aueb.ticketify.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {

    @Size(min = 2, max = 30, message = "Firstname must be between 2 and 30 characters")
    private String firstname;

    @Size(min = 2, max = 30, message = "Lastname must be between 2 and 30 characters")
    private String lastname;

    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phone;

    private Long regionId;
}
