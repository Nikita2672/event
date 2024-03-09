package com.example.event.controller;

import com.example.event.service.impl.EventRequestServiceImpl;
import com.example.event.service.auth.UserDetailsImpl;
import com.example.event.view.EventRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EventRequestController {

    private final EventRequestServiceImpl eventRequestService;

    @Autowired
    public EventRequestController(EventRequestServiceImpl eventRequestService) {
        this.eventRequestService = eventRequestService;
    }

    @PostMapping("user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventRequestVo> createEventRequest(@RequestBody EventRequestVo eventRequestVo) {
        return ResponseEntity.ok(eventRequestService.createEventRequest(eventRequestVo, getUserIdFromToken()));
    }

    @GetMapping("user/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventRequestVo> getEventRequest(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.getEventRequestById(getUserIdFromToken(), eventId);
        if (eventRequestVo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("user/eventRequests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventRequestVo>> getEventRequest(@RequestParam(name = "ascSort", defaultValue = "true") boolean sortOrder,
                                                                @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(eventRequestService.getEventRequests(getUserIdFromToken(), pageNumber, sortOrder));
    }

    @PostMapping("user/updateEvent/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventRequestVo> updateEventRequest(@RequestBody EventRequestVo eventRequestVo,
                                                             @PathVariable Long eventId) {

        EventRequestVo eventRequestVoResult = eventRequestService.updateEventRequest(getUserIdFromToken(), eventId, eventRequestVo);
        if (eventRequestVoResult == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventRequestVoResult);
    }


    @PostMapping("user/submit/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventRequestVo> submitEventRequest(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.submitEventRequest(getUserIdFromToken(), eventId);
        if (eventRequestVo == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventRequestVo);
    }


    @PostMapping("moderator/accept/{eventId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<EventRequestVo> acceptEvent(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.acceptEventRequest(eventId);
        if (eventRequestVo == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @PostMapping("moderator/reject/{eventId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<EventRequestVo> rejectEvent(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.rejectEventRequest(eventId);
        if (eventRequestVo == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("moderator/eventRequests")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<List<EventRequestVo>> getEventRequests(@RequestParam(name = "ascSort", defaultValue = "true") boolean sortOrder,
                                                                 @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(eventRequestService.getEventRequests(getUserIdFromToken(), pageNumber, sortOrder));
    }

    private Long getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return ((UserDetailsImpl) userDetails).getId();
        }
        return null;
    }
}
