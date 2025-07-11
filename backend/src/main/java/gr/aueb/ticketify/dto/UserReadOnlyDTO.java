package gr.aueb.ticketify.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadOnlyDTO {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private Long regionId;
    private Integer totalTicketsBought;
}
