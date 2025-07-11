package gr.aueb.ticketify.service;

import gr.aueb.ticketify.core.enums.TicketStatus;
import gr.aueb.ticketify.dto.TicketReadOnlyDTO;

import java.security.Principal;
import java.util.List;

public interface ITicketService {

    TicketReadOnlyDTO getTicketById(Long id, Principal principal);
    TicketReadOnlyDTO bookTicket(Long eventId, Principal principal);
    TicketReadOnlyDTO cancelTicket(Long id, Principal principal);
    List<TicketReadOnlyDTO> getAllTickets(); // admin only
    List<TicketReadOnlyDTO> getTicketsByUser(Principal principal);
    List<TicketReadOnlyDTO> getTicketsByEventId(Long eventId);
    List<TicketReadOnlyDTO> getTicketsByUserId(Long userId);
}
