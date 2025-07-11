package gr.aueb.ticketify.service;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.core.exceptions.EntityNotFoundException;
import gr.aueb.ticketify.dto.EventCreateDTO;
import gr.aueb.ticketify.dto.EventReadOnlyDTO;
import gr.aueb.ticketify.dto.EventUpdateDTO;
import gr.aueb.ticketify.mapper.Mapper;
import gr.aueb.ticketify.model.Event;
import gr.aueb.ticketify.model.static_data.Region;
import gr.aueb.ticketify.repository.EventRepository;
import gr.aueb.ticketify.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService implements IEventService {

    private final EventRepository eventRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional(readOnly = true)
    public EventReadOnlyDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event ", "Event with id " + id + " not found"));

        return Mapper.mapToEventReadOnlyDTO(event);
    }

    @Override
    @Transactional
    public EventReadOnlyDTO createEvent(EventCreateDTO createDTO) {

        Region region = regionRepository.findById(createDTO.getRegionId())
                .orElseThrow(() -> new EntityNotFoundException("Region", "Region with id " + createDTO.getRegionId() + " not found"));

        Event event = Mapper.mapEventCreateToModel(createDTO);
        event.setRegion(region);
        event.setAvailableTickets(createDTO.getTotalTickets());
        event.setEventStatus(EventStatus.ACTIVE);

        return Mapper.mapToEventReadOnlyDTO(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventReadOnlyDTO updateEvent(Long id, EventUpdateDTO updateDTO) {

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event", "Event with id " + id + " not found"));

        if (updateDTO.getRegionId() != null) {
            Region region = regionRepository.findById(updateDTO.getRegionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region", "Region with id " + updateDTO.getRegionId() + " not found"));
            existingEvent.setRegion(region);
        }

        Event updatedEvent = Mapper.mapEventUpdateToModel(updateDTO, existingEvent);
        eventRepository.save(updatedEvent);
        return Mapper.mapToEventReadOnlyDTO(updatedEvent);
    }

    @Override
    @Transactional
    public EventReadOnlyDTO updateEventStatus(Long id, EventStatus status) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event", "Event with id " + id + " not found"));

        event.setEventStatus(status);
        eventRepository.save(event);
        return Mapper.mapToEventReadOnlyDTO(event);
    }

    @Override
    @Transactional
    public EventReadOnlyDTO cancelEvent(Long id) {

        return updateEventStatus(id, EventStatus.CANCELLED);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event", "Event with id " + id + " not found"));

        event.setEventStatus(EventStatus.CANCELLED);
        eventRepository.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getEventsByRegionId(Long regionId) {
        return eventRepository.findByRegionId(regionId).stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getEventsByStatus(EventStatus status) {
        return eventRepository.findByEventStatus(status).stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getEventsByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getUpcomingEvents(LocalDate fromDate) {
        return eventRepository.findByDateAfter(fromDate).stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getAvailableEvents() {
        return eventRepository.findAvailableEvents().stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadOnlyDTO> getEventsByRegionAndStatus(Long regionId, EventStatus status) {
        return eventRepository.findByRegionIdAndStatus(regionId, status).stream()
                .map(Mapper::mapToEventReadOnlyDTO)
                .collect(Collectors.toList());
    }
}
