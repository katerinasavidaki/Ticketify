package gr.aueb.ticketify.service;

import gr.aueb.ticketify.dto.TicketReadOnlyDTO;
import gr.aueb.ticketify.mapper.Mapper;
import gr.aueb.ticketify.model.Ticket;
import gr.aueb.ticketify.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadOnlyDTO> getTicketsByUserId(Long userId) {

        List<Ticket> tickets = ticketRepository.findByUserIdWithEvent(userId);

        return tickets.stream()
                .map(Mapper::mapToTicketReadOnlyDTO)
                .collect(Collectors.toList());
    }
}
