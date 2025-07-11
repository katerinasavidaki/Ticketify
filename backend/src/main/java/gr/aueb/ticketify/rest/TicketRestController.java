package gr.aueb.ticketify.rest;

import gr.aueb.ticketify.dto.TicketReadOnlyDTO;
import gr.aueb.ticketify.service.ITicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tickets")
public class TicketRestController {

    private final ITicketService ticketService;

    @Operation(
            summary = "Get all tickets",
            description = "Retrieve all tickets (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all tickets retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketReadOnlyDTO>> getAllTickets() {
        List<TicketReadOnlyDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @Operation(
            summary = "Get a ticket by ID",
            description = "Retrieve a ticket if it belongs to the authenticated user or if user is ADMIN"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TicketReadOnlyDTO> getTicketById(@PathVariable Long id, Principal principal) {
        TicketReadOnlyDTO ticket = ticketService.getTicketById(id, principal);
        return ResponseEntity.ok(ticket);
    }

    @Operation(
            summary = "Get all tickets of the authenticated user",
            description = "Returns all tickets that belong to the logged-in user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized")
    })
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<TicketReadOnlyDTO>> getUserTickets(Principal principal) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(principal));
    }

    @Operation(
            summary = "Get all tickets for a specific event",
            description = "ADMIN-only endpoint to list tickets of a specific event"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketReadOnlyDTO>> getTicketsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEventId(eventId));
    }

    @Operation(
            summary = "Book a ticket for an event",
            description = "Creates a new ticket for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket successfully booked"),
            @ApiResponse(responseCode = "400", description = "No available tickets"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized")
    })
    @PostMapping("/book/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketReadOnlyDTO> bookTicket(@PathVariable Long eventId, Principal principal) {
        return ResponseEntity.ok(ticketService.bookTicket(eventId, principal));
    }

    @Operation(
            summary = "Cancel a ticket",
            description = "Cancels a ticket by ID if it belongs to the user or if user is ADMIN"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket successfully cancelled"),
            @ApiResponse(responseCode = "400", description = "Ticket already cancelled"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<TicketReadOnlyDTO> cancelTicket(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(ticketService.cancelTicket(id, principal));
    }
}
