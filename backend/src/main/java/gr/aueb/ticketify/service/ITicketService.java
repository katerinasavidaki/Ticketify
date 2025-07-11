package gr.aueb.ticketify.service;

import gr.aueb.ticketify.dto.TicketReadOnlyDTO;

import java.util.List;

public interface ITicketService {

    List<TicketReadOnlyDTO> getTicketsByUserId(Long userId);
}
