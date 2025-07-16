package gr.aueb.ticketify.rest;

import gr.aueb.ticketify.core.enums.EventStatus;
import gr.aueb.ticketify.core.exceptions.ValidationException;
import gr.aueb.ticketify.dto.EventCreateDTO;
import gr.aueb.ticketify.dto.EventReadOnlyDTO;
import gr.aueb.ticketify.dto.EventUpdateDTO;
import gr.aueb.ticketify.service.IEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventRestController {

    private final IEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventReadOnlyDTO>> getAllEvents() {
        List<EventReadOnlyDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventReadOnlyDTO> getEventById(@PathVariable Long id) {
        EventReadOnlyDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EventReadOnlyDTO> createEvent(@Valid @RequestBody EventCreateDTO dto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        EventReadOnlyDTO createdEvent = eventService.createEvent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EventReadOnlyDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventUpdateDTO dto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        EventReadOnlyDTO updatedEvent = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<EventReadOnlyDTO> updateEventStatus(
            @PathVariable Long id,
            @RequestParam EventStatus status) {

        return ResponseEntity.ok(eventService.updateEventStatus(id, status));

    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventReadOnlyDTO> cancelEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.cancelEvent(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<EventReadOnlyDTO>> getEventsByRegionId(@PathVariable Long regionId) {
        List<EventReadOnlyDTO> events = eventService.getEventsByRegionId(regionId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventReadOnlyDTO>> getEventsByStatus(@PathVariable EventStatus status) {
        return ResponseEntity.ok(eventService.getEventsByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventReadOnlyDTO>> getEventsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(eventService.getEventsByTitle(title));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventReadOnlyDTO>> getUpcomingEvents(
            @RequestParam(required = false) LocalDateTime fromDate) {

        LocalDateTime date = fromDate != null ? fromDate : LocalDateTime.now();
        return ResponseEntity.ok(eventService.getUpcomingEvents(date));
    }

    @GetMapping("/available")
    public ResponseEntity<List<EventReadOnlyDTO>> getAvailableEvents() {
        return ResponseEntity.ok(eventService.getAvailableEvents());
    }

    @GetMapping("/region/{regionId}/status/{status}")
    public ResponseEntity<List<EventReadOnlyDTO>> getEventsByRegionAndStatus(
            @PathVariable Long regionId,
            @PathVariable EventStatus status) {

        return ResponseEntity.ok(eventService.getEventsByRegionAndStatus(regionId, status));
    }
}
