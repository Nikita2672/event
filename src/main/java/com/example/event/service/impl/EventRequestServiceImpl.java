package com.example.event.service.impl;

import com.example.event.model.EventRequest;
import com.example.event.model.Status;
import com.example.event.model.User;
import com.example.event.repository.EventRequestRepository;
import com.example.event.repository.UserRepository;
import com.example.event.service.EventRequestService;
import com.example.event.view.EventRequestVo;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {

    private final EventRequestRepository eventRequestRepository;

    private final UserRepository userRepository;

    private final PhoneServiceImpl phoneService;

    private static final int PAGE_SIZE = 5;

    @Override
    public EventRequestVo createEventRequest(EventRequestVo eventRequestVo, Long userId) {

        Long phoneId = phoneService.savePhone(eventRequestVo.phone());
        if (phoneId == 0) return null;

        EventRequest eventRequest = EventRequest.builder()
                .status(Status.DRAFT)
                .appealText(eventRequestVo.appealText())
                .phoneId(phoneId)
                .name(eventRequestVo.name())
                .creationDate(LocalDateTime.now())
                .userId(userId)
                .build();
        return convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo getEventRequestById(Long userId, Long eventRequestId) {
        Optional<EventRequest> eventRequest = eventRequestRepository.findById(eventRequestId);
        if (eventRequest.isEmpty()) return null;
        if (!Objects.equals(eventRequest.get().getUserId(), userId)) return null;
        return convertToVo(eventRequest.get());
    }

    @Override
    public List<EventRequestVo> getEventRequests(Long userId, int pageNumber, boolean sortOrder) {
        Sort sort = sortOrder ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        return eventRequestRepository.findAllByUserId(userId, pageable)
                .getContent()
                .stream()
                .map(this::convertToVo)
                .toList();
    }

    @Override
    public List<EventRequestVo> getEventRequests(int pageNumber, boolean sortOrder, String eventRequestName) {
        Sort sort = sortOrder ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        return eventRequestRepository.findAllByNameAndStatus(pageable, eventRequestName, Status.SUBMIT)
                .stream()
                .map(this::convertToVo)
                .toList();
    }

    @Override
    public List<EventRequestVo> getEventRequestsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsernameContaining(username);
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();
        return eventRequestRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::convertToVo)
                .toList();
    }

    @Override
    public EventRequestVo updateEventRequest(@Nonnull Long userId, Long eventRequestId, EventRequestVo eventRequestVo) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!Objects.equals(optionalEventRequest.get().getUserId(), userId)) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.DRAFT)) return null;

        Long phoneId = phoneService.savePhone(eventRequestVo.phone());
        if (phoneId == 0) return null;
        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.DRAFT);
        eventRequest.setAppealText(eventRequestVo.appealText());
        eventRequest.setPhoneId(phoneId);
        eventRequest.setName(eventRequestVo.name());

        return this.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo submitEventRequest(@Nonnull Long userId, @Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!Objects.equals(optionalEventRequest.get().getUserId(), userId)) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.DRAFT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.SUBMIT);
        return this.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo acceptEventRequest(@Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.SUBMIT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.ACCEPT);
        return this.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo rejectEventRequest(@Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.SUBMIT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.REJECT);
        return convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public List<EventRequestVo> getAllEventRequests(int pageNumber, String sortOrder, String eventRequestName) {
        Sort sort = sortOrder.equals("true") ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        List<Status> statusList = List.of(Status.ACCEPT, Status.SUBMIT, Status.REJECT);
        return eventRequestRepository.findAllByNameAndStatusIn(pageable, eventRequestName, statusList)
                .stream()
                .map(this::convertToVo)
                .toList();
    }

    private EventRequestVo convertToVo(EventRequest eventRequest) {
        return new EventRequestVo(
                eventRequest.getStatus(),
                eventRequest.getAppealText(),
                phoneService.findById(eventRequest.getPhoneId()).get().getPhoneNumber(),
                eventRequest.getName(),
                eventRequest.getCreationDate()
        );
    }
}
