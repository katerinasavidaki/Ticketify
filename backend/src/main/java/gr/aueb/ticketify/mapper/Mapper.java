package gr.aueb.ticketify.mapper;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.core.enums.Role;
import gr.aueb.ticketify.dto.*;
import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.Ticket;
import gr.aueb.ticketify.model.User;
import gr.aueb.ticketify.model.static_data.Region;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    // USER

    public static UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {

        return UserReadOnlyDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .phone(user.getPhone())
                .regionId(user.getRegion().getId())
                .totalTicketsBought(user.getAllTickets().size())
                .build();
    }

    public static User mapUserCreateToModel(UserRegisterDTO registerDTO) {
        return User.builder()
                .username(registerDTO.getUsername())
                .password(registerDTO.getPassword()) // Note: Password should be hashed before saving in service layer
                .role(Role.USER)
                .phone(registerDTO.getPhone())
                .isActive(true)
                .build();
    }

    public static User mapUserUpdateToModel(UserUpdateDTO userUpdateDTO, User existingUser) {
        if (userUpdateDTO.getFirstname() != null) existingUser.setFirstname(userUpdateDTO.getFirstname());
        if (userUpdateDTO.getLastname() != null) existingUser.setLastname(userUpdateDTO.getLastname());
        if (userUpdateDTO.getPhone() != null) existingUser.setPhone(userUpdateDTO.getPhone());

        return existingUser;
    }

    // REGION
    public static RegionDTO mapToRegionDTO(Region region) {
        return RegionDTO.builder()
                .id(region.getId())
                .name(region.getName())
                .build();
    }

    // EVENT
    public static EventReadOnlyDTO mapToEventReadOnlyDTO(Event event) {
        return EventReadOnlyDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDateTime(event.getDateTime())
                .price(event.getPrice())
                .regionName(event.getRegion().getName())
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getAvailableTickets())
                .status(event.getEventStatus())
                .build();
    }

    public static Event mapEventCreateToModel(EventCreateDTO dto) {

        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dateTime(dto.getEventDateTime())
                .price(dto.getPrice())
                .totalTickets(dto.getTotalTickets())
                .eventStatus(EventStatus.ACTIVE)
                .build();
    }

    public static Event mapEventUpdateToModel(EventUpdateDTO dto, Event existingEvent) {
        if (dto.getTitle() != null) existingEvent.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existingEvent.setDescription(dto.getDescription());
        if (dto.getEventDateTime() != null) existingEvent.setDateTime(dto.getEventDateTime());
        if (dto.getPrice() != null) existingEvent.setPrice(dto.getPrice());
        if (dto.getTotalTickets() != null) existingEvent.setTotalTickets(dto.getTotalTickets());
        if (dto.getStatus() != null) existingEvent.setEventStatus(dto.getStatus());

        return existingEvent;
    }

    // TICKET
    public static TicketReadOnlyDTO mapToTicketReadOnlyDTO(Ticket ticket) {

        return TicketReadOnlyDTO.builder()
                .id(ticket.getId())
                .regionName(ticket.getEvent().getRegion().getName())
                .eventTitle(ticket.getEvent().getTitle())
                .eventDateTime(ticket.getEvent().getDateTime())
                .bookingDateTime(ticket.getBookingDate())
                .price(ticket.getPrice())
                .cancelled(ticket.getIsCancelled())
                .build();
    }

}
