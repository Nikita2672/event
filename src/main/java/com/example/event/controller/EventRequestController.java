package com.example.event.controller;

import com.example.event.model.User;
import com.example.event.service.impl.EventRequestServiceImpl;
import com.example.event.view.EventRequestVo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EventRequestController {

    private final EventRequestServiceImpl eventRequestService;

    @PostMapping("user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventRequestVo> createEventRequest(@RequestBody EventRequestVo eventRequestVo) {
        EventRequestVo eventRequest = eventRequestService.createEventRequest(eventRequestVo, getUserIdFromToken());
        if (eventRequest == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(eventRequest);
        }
    }

    @GetMapping("user/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventRequestVo> getEventRequest(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.getEventRequest(getUserIdFromToken(), eventId);
        if (eventRequestVo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("user/eventRequests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventRequestVo>> getEventRequest(@RequestParam(name = "ascSort", defaultValue = "true") boolean sortOrder,
                                                                @RequestParam(name = "page", defaultValue = "0") int pageNumber) {
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


    @PostMapping("operator/accept/{eventId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<EventRequestVo> acceptEvent(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.acceptEventRequest(getUserIdFromToken(), eventId);
        if (eventRequestVo == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @PostMapping("operator/reject/{eventId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<EventRequestVo> rejectEvent(@PathVariable Long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.rejectEventRequest(getUserIdFromToken(), eventId);
        if (eventRequestVo == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("operator/eventRequests")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<List<EventRequestVo>> getEventRequests(@RequestParam(name = "ascSort", defaultValue = "true") String sortOrder,
                                                                 @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                                 @RequestParam(name = "username", defaultValue = "") String username,
                                                                 @RequestParam(name = "eventName", defaultValue = "") String eventName) {
        return ResponseEntity.ok(eventRequestService.getEventRequests(pageNumber, sortOrder, username, eventName));
    }

    @GetMapping("operator/eventRequests/{eventId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<EventRequestVo> getEventRequestById(@PathVariable long eventId) {
        EventRequestVo eventRequestVo = eventRequestService.getEventRequestById(eventId);
        if (eventRequestVo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("admin/eventRequests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventRequestVo>> getAllEventRequests(@RequestParam(name = "ascSort", defaultValue = "true") String sortOrder,
                                                                    @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                                    @RequestParam(name = "eventName", defaultValue = "") String eventName) {
        return ResponseEntity.ok(eventRequestService.getAllEventRequests(pageNumber, sortOrder, eventName));
    }

    private Long getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return ((User) userDetails).getId();
        }
        return null;
    }


}
