package com.example.event.controller;

import com.example.event.exception.IllegalRequestException;
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

import static com.example.event.service.impl.EventRequestServiceImpl.EVENT_REQUEST_NOT_FOUND;


@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventRequestController {

    private final EventRequestServiceImpl eventRequestService;

    @PostMapping("user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createEventRequest(@RequestBody EventRequestVo eventRequestVo) {
        EventRequestVo eventRequest;
        try {
            eventRequest = eventRequestService.createEventRequest(eventRequestVo, getUserIdFromToken());
        } catch (IllegalRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(eventRequest);
    }

    @GetMapping("user/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getEventRequest(@PathVariable long eventId) {
        EventRequestVo eventRequestVo;
        try {
            eventRequestVo = eventRequestService.getEventRequest(getUserIdFromToken(), eventId);
        } catch (IllegalRequestException e) {
            if (e.getMessage().equals(EVENT_REQUEST_NOT_FOUND)) return ResponseEntity.notFound().build();
            else return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("user/eventRequests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventRequestVo>> getEventRequest(@RequestParam(name = "ascSort", defaultValue = "true") String sortOrder,
                                                                @RequestParam(name = "page", defaultValue = "0") int pageNumber) {
        return ResponseEntity.ok(eventRequestService.getEventRequests(getUserIdFromToken(), pageNumber, sortOrder));
    }

    @PostMapping("user/updateEvent/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateEventRequest(@RequestBody EventRequestVo eventRequestVo,
                                                @PathVariable long eventId) {
        EventRequestVo eventRequestVoResult;
        try {
            eventRequestVoResult = eventRequestService.updateEventRequest(getUserIdFromToken(), eventId, eventRequestVo);
        } catch (IllegalRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(eventRequestVoResult);
    }


    @PostMapping("user/submit/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submitEventRequest(@PathVariable long eventId) {
        EventRequestVo eventRequestVo;
        try {
            eventRequestVo = eventRequestService.submitEventRequest(getUserIdFromToken(), eventId);
        } catch (IllegalRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(eventRequestVo);
    }


    @PostMapping("operator/accept/{eventId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<?> acceptEvent(@PathVariable long eventId) {
        EventRequestVo eventRequestVo;
        try {
            eventRequestVo = eventRequestService.acceptEventRequest(getUserIdFromToken(), eventId);
        } catch (IllegalRequestException e) {
            if (e.getMessage().equals(EVENT_REQUEST_NOT_FOUND)) return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(eventRequestVo);
    }

    @PostMapping("operator/reject/{eventId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<?> rejectEvent(@PathVariable long eventId) {
        EventRequestVo eventRequestVo;
        try {
            eventRequestVo = eventRequestService.rejectEventRequest(getUserIdFromToken(), eventId);
        } catch (IllegalRequestException e) {
            if (e.getMessage().equals(EVENT_REQUEST_NOT_FOUND)) return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        EventRequestVo eventRequestVo;
        try {
            eventRequestVo = eventRequestService.getEventRequestById(eventId);
        } catch (IllegalRequestException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventRequestVo);
    }

    @GetMapping("admin/eventRequests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventRequestVo>> getAllEventRequests(@RequestParam(name = "ascSort", defaultValue = "true") String sortOrder,
                                                                    @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                                    @RequestParam(name = "eventName", defaultValue = "") String eventName) {
        return ResponseEntity.ok(eventRequestService.getAllEventRequests(pageNumber, sortOrder, eventName));
    }

    private long getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return ((User) userDetails).getId();
        }
        return 0L;
    }
}
