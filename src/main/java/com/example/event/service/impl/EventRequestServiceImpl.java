package com.example.event.service.impl;

import com.example.event.config.converter.EventRequestConverter;
import com.example.event.model.EventRequest;
import com.example.event.model.Status;
import com.example.event.model.User;
import com.example.event.repository.EventRequestRepository;
import com.example.event.repository.UserRepository;
import com.example.event.service.EventRequestService;
import com.example.event.view.EventRequestVo;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventRequestServiceImpl implements EventRequestService {

    private final EventRequestRepository eventRequestRepository;

    private final UserRepository userRepository;

    private final EventRequestConverter eventRequestConverter;

    private static final int PAGE_SIZE = 5;

    @Autowired
    public EventRequestServiceImpl(EventRequestRepository eventRequestRepository, UserRepository userRepository, EventRequestConverter eventRequestConverter) {
        this.eventRequestRepository = eventRequestRepository;
        this.userRepository = userRepository;
        this.eventRequestConverter = eventRequestConverter;
    }

    @Override
    public EventRequestVo createEventRequest(EventRequestVo eventRequestVo, Long userId) {
        EventRequest eventRequest = EventRequest.builder()
                .status(eventRequestVo.status())
                .appealText(eventRequestVo.appealText())
                .phone(eventRequestVo.phone())
                .name(eventRequestVo.name())
                .creationDate(LocalDateTime.now())
                .userId(userId)
                .build();
        return eventRequestConverter.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo getEventRequestById(Long userId, Long eventRequestId) {
        Optional<EventRequest> eventRequest = eventRequestRepository.findById(eventRequestId);
        if (eventRequest.isEmpty()) return null;
        if (!Objects.equals(eventRequest.get().getUserId(), userId)) return null;
        return eventRequestConverter.convertToVo(eventRequest.get());
    }

    @Override
    public List<EventRequestVo> getEventRequests(Long userId, int pageNumber, boolean sortOrder) {
        Sort sort = sortOrder ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        return eventRequestRepository.findAllByUserId(userId, pageable)
                .getContent()
                .stream()
                .map(eventRequestConverter::convertToVo)
                .toList();
    }

    @Override
    public List<EventRequestVo> getEventRequests(int pageNumber, boolean sortOrder, String eventRequestName) {
        Sort sort = sortOrder ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        return eventRequestRepository.findAllByNameAndStatus(pageable, eventRequestName, Status.SUBMIT)
                .stream()
                .map(eventRequestConverter::convertToVo)
                .toList();
    }

    @Override
    public List<EventRequestVo> getEventRequestsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsernameContaining(username);
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();
        return eventRequestRepository.findAllByUserId(user.getId())
                .stream()
                .map(eventRequestConverter::convertToVo)
                .toList();
    }

    @Override
    public EventRequestVo updateEventRequest(@Nonnull Long userId, Long eventRequestId, EventRequestVo eventRequestVo) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!Objects.equals(optionalEventRequest.get().getUserId(), userId)) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.DRAFT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(eventRequestVo.status());
        eventRequest.setAppealText(eventRequestVo.appealText());
        eventRequest.setPhone(eventRequestVo.phone());
        eventRequest.setName(eventRequestVo.name());

        return eventRequestConverter.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo submitEventRequest(@Nonnull Long userId, @Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!Objects.equals(optionalEventRequest.get().getUserId(), userId)) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.DRAFT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.SUBMIT);
        return eventRequestConverter.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo acceptEventRequest(@Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.SUBMIT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.ACCEPT);
        return eventRequestConverter.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo rejectEventRequest(@Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.SUBMIT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.REJECT);
        return eventRequestConverter.convertToVo(eventRequestRepository.save(eventRequest));
    }
}
