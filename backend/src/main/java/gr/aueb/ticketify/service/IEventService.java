package gr.aueb.ticketify.service;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.dto.EventCreateDTO;
import gr.aueb.ticketify.dto.EventReadOnlyDTO;
import gr.aueb.ticketify.dto.EventUpdateDTO;

import java.time.LocalDate;
import java.util.List;

public interface IEventService {

    EventReadOnlyDTO getEventById(Long id);
    EventReadOnlyDTO createEvent(EventCreateDTO createDTO);
    EventReadOnlyDTO updateEvent(Long id, EventUpdateDTO updateDTO);
    EventReadOnlyDTO updateEventStatus(Long id, EventStatus status);
    EventReadOnlyDTO cancelEvent(Long id);
    void deleteEvent(Long id);
    List<EventReadOnlyDTO> getAllEvents();
    List<EventReadOnlyDTO> getEventsByRegionId(Long regionId);
    List<EventReadOnlyDTO> getEventsByStatus(EventStatus status);
    List<EventReadOnlyDTO> getEventsByTitle(String title);
    List<EventReadOnlyDTO> getUpcomingEvents(LocalDate fromDate);
    List<EventReadOnlyDTO> getAvailableEvents();
    List<EventReadOnlyDTO> getEventsByRegionAndStatus(Long regionId, EventStatus status);
}
