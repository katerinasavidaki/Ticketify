package gr.aueb.ticketify.dto.authentication;

import gr.aueb.ticketify.core.enums.Role;

public record AuthenticationResponseDTO(
        String username,
        String token,
        Role role
) {}
