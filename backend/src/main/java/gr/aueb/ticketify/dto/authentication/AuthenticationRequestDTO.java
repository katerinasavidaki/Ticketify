package gr.aueb.ticketify.dto.authentication;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDTO(
        @NotNull
        String username,
        @NotNull
        String password
) {}
