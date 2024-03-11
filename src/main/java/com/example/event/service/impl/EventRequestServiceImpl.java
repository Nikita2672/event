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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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

        Long phoneId = phoneService.savePhone(eventRequestVo.getPhone());
        if (phoneId == 0) return null;

        EventRequest eventRequest = EventRequest.builder()
                .status(Status.DRAFT)
                .appealText(eventRequestVo.getAppealText())
                .phoneId(phoneId)
                .name(eventRequestVo.getName())
                .creationDate(LocalDateTime.now())
                .userId(userId)
                .build();
        return convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo getEventRequest(Long userId, Long eventRequestId) {
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
    public List<EventRequestVo> getEventRequests(int pageNumber, String sortOrder, String username, String eventRequestName) {
        Sort sort = sortOrder.equals("true") ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        Page<EventRequest> result;
        Long userId;
        List<User> users = userRepository.findByUsernameContaining(username);
        userId = users.isEmpty() ? null : users.get(0).getId();
        if (!username.isEmpty() && !eventRequestName.isEmpty()) {
            if (userId == null) return Collections.emptyList();
            result = eventRequestRepository.findAllByUserIdAndNameAndStatus(pageable, userId, eventRequestName, Status.SUBMIT);
        } else if (!username.isEmpty()) {
            if (userId == null) return Collections.emptyList();
            result = eventRequestRepository.findAllByUserIdAndStatus(pageable, userId, Status.SUBMIT);
        } else if (!eventRequestName.isEmpty()) {
            result = eventRequestRepository.findAllByNameAndStatus(pageable, eventRequestName, Status.SUBMIT);
        } else {
            result = eventRequestRepository.findAllByStatus(pageable, Status.SUBMIT);
        }
        return result.get().map(this::convertToVo).toList();
    }

    @Override
    public List<EventRequestVo> getEventRequestsByUsername(String username) {
        List<User> users = userRepository.findByUsernameContaining(username);
        if (users.isEmpty()) return null;
        User user = users.get(0);
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

        Long phoneId = phoneService.savePhone(eventRequestVo.getPhone());
        if (phoneId == 0) return null;
        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.DRAFT);
        eventRequest.setAppealText(eventRequestVo.getAppealText());
        eventRequest.setPhoneId(phoneId);
        eventRequest.setName(eventRequestVo.getName());

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
    public EventRequestVo acceptEventRequest(@Nonnull Long operatorId, @Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.SUBMIT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.ACCEPT);
        eventRequest.setOperatorId(operatorId);
        return this.convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public EventRequestVo rejectEventRequest(@Nonnull Long operatorId, @Nonnull Long eventRequestId) {
        Optional<EventRequest> optionalEventRequest = eventRequestRepository.findById(eventRequestId);
        if (optionalEventRequest.isEmpty()) return null;
        if (!optionalEventRequest.get().getStatus().equals(Status.SUBMIT)) return null;

        EventRequest eventRequest = optionalEventRequest.get();
        eventRequest.setStatus(Status.REJECT);
        eventRequest.setOperatorId(operatorId);
        return convertToVo(eventRequestRepository.save(eventRequest));
    }

    @Override
    public List<EventRequestVo> getAllEventRequests(int pageNumber, String sortOrder, String eventRequestName) {
        Sort sort = sortOrder.equals("true") ? Sort.by("creationDate").ascending() : Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        List<Status> statusList = List.of(Status.ACCEPT, Status.SUBMIT, Status.REJECT);
        Page<EventRequest> eventRequests;
        if (!eventRequestName.isEmpty()) {
            eventRequests = eventRequestRepository.findAllByNameAndStatusIn(pageable, eventRequestName, statusList);
        } else {
            eventRequests = eventRequestRepository.findAllByStatusIn(pageable, statusList);
        }
        return eventRequests.get()
                .map(this::convertToVo)
                .toList();
    }

    @Override
    public EventRequestVo getEventRequestById(Long eventRequestId) {
        Optional<EventRequest> eventRequest = eventRequestRepository.findById(eventRequestId);
        if (eventRequest.isEmpty() || !eventRequest.get().getStatus().equals(Status.SUBMIT)) return null;
        return convertToVo(eventRequest.get());
    }

    private EventRequestVo convertToVo(EventRequest eventRequest) {
        String username = "";
        Optional<User> optionalUser = userRepository.findById(eventRequest.getUserId());
        if (optionalUser.isPresent()) username = optionalUser.get().getUsername();
        return new EventRequestVo(
                eventRequest.getStatus(),
                eventRequest.getAppealText(),
                phoneService.findById(eventRequest.getPhoneId()).get().getPhoneNumber(),
                eventRequest.getName(),
                eventRequest.getCreationDate(),
                username
        );
    }
}
