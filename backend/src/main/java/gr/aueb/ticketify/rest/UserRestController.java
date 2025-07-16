package gr.aueb.ticketify.rest;

import gr.aueb.ticketify.core.exceptions.ValidationException;
import gr.aueb.ticketify.dto.TicketReadOnlyDTO;
import gr.aueb.ticketify.dto.UserReadOnlyDTO;
import gr.aueb.ticketify.dto.UserUpdateDTO;
import gr.aueb.ticketify.service.ITicketService;
import gr.aueb.ticketify.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserService userService;
    private final ITicketService ticketService;

    @Operation(
            summary = "Get a user by ID",
            description = "Returns user information by ID. Requires USER or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserReadOnlyDTO> getUserById(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(userService.getUserById(id, principal));
    }

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users. Only accessible to ADMIN users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserReadOnlyDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Update a user",
            description = "Updates user information by ID. Requires USER or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserReadOnlyDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO updateDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(userService.updateUser(id, updateDTO));
    }

    @Operation(
            summary = "Get any user's tickets (admin only)",
            description = "Returns all tickets for a specific user. Requires admin privileges.",
            security = @SecurityRequirement(name = "Bearer Authentication\"")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketReadOnlyDTO>> getUserTickets(@PathVariable Long id) {

        List<TicketReadOnlyDTO> tickets = ticketService.getTicketsByUserId(id);
        return ResponseEntity.ok(tickets);
    }

    @Operation(
            summary = "Get authenticated user's tickets",
            description = "Returns the tickets of the currently authenticated user.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketReadOnlyDTO>> getMyTickets(Principal principal) {
        List<TicketReadOnlyDTO> tickets = ticketService.getTicketsByUser(principal);
        return ResponseEntity.ok(tickets);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by ID. Only accessible to ADMIN users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
