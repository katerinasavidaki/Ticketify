package gr.aueb.ticketify.service;

import gr.aueb.ticketify.core.enums.Role;
import gr.aueb.ticketify.core.enums.TicketStatus;
import gr.aueb.ticketify.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.ticketify.core.exceptions.EntityNotAuthorizedException;
import gr.aueb.ticketify.core.exceptions.EntityNotFoundException;
import gr.aueb.ticketify.dto.TicketReadOnlyDTO;
import gr.aueb.ticketify.mapper.Mapper;
import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.Ticket;
import gr.aueb.ticketify.model.User;
import gr.aueb.ticketify.repository.EventRepository;
import gr.aueb.ticketify.repository.TicketRepository;
import gr.aueb.ticketify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public List<TicketReadOnlyDTO> getTicketsByUser(Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User", "User not found with username: " + principal.getName()));

        return ticketRepository.findByUserId(user.getId()).stream()
                .map(Mapper::mapToTicketReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadOnlyDTO> getTicketsByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User ", "User not found with id: " + userId));

        return ticketRepository.findByUserId(user.getId())
                .stream().map(Mapper::mapToTicketReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TicketReadOnlyDTO getTicketById(Long id, Principal principal) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket ", "Ticket not found with id: " + id));

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User", "User not found with username: " + principal.getName()));

        if (!ticket.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new EntityNotAuthorizedException("Ticket ", "User not authorized to access this ticket");
        }

        return Mapper.mapToTicketReadOnlyDTO(ticket);
    }

    @Override
    @Transactional
    public TicketReadOnlyDTO bookTicket(Long eventId, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User ", "User not found with username: " + principal.getName()));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event ", "Event not found with id: " + eventId));

        if (event.getAvailableTickets() <= 0) {
            throw new EntityInvalidArgumentException("Ticket ", "No available tickets for this event");
        }

        event.setAvailableTickets(event.getAvailableTickets() - 1);

        Ticket ticket = Ticket.builder()
                .user(user)
                .event(event)
                .status(TicketStatus.BOOKED)
                .bookingDate(LocalDateTime.now())
                .price(event.getPrice())
                .build();

        user.addTicket(ticket);
        event.addTicket(ticket);

        return Mapper.mapToTicketReadOnlyDTO(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketReadOnlyDTO cancelTicket(Long id, Principal principal) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket ", "Ticket not found with id: " + id));

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User", "User not found with username: " + principal.getName()));

        if (!ticket.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new EntityNotAuthorizedException("Ticket ", "User not authorized to cancel this ticket");
        }

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new EntityInvalidArgumentException("Ticket ", "Ticket is already cancelled");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.setIsCancelled(true);
        ticket.getEvent().setAvailableTickets(ticket.getEvent().getAvailableTickets() + 1);

        return Mapper.mapToTicketReadOnlyDTO(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadOnlyDTO> getAllTickets() {

        return ticketRepository.findAll().stream()
                .map(Mapper::mapToTicketReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadOnlyDTO> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId).stream()
                .map(Mapper::mapToTicketReadOnlyDTO)
                .collect(Collectors.toList());
    }
}
