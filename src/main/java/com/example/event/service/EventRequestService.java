package com.example.event.service;

import com.example.event.view.EventRequestVo;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public interface EventRequestService {
    EventRequestVo createEventRequest(EventRequestVo eventRequestVo, Long userId);

    EventRequestVo getEventRequestById(Long userId, Long eventRequestId);

    List<EventRequestVo> getEventRequests(Long userId, int pageNumber, boolean sortOrder);

    List<EventRequestVo> getEventRequests(int pageNumber, boolean sortOrder, String eventRequestName);

    List<EventRequestVo> getAllEventRequests(int pageNumber, String sortOrder, String eventRequestName);

    List<EventRequestVo> getEventRequestsByUsername(String username);

    EventRequestVo updateEventRequest(@Nonnull Long userId, Long eventRequestId, EventRequestVo eventRequestVo);

    EventRequestVo submitEventRequest(@Nonnull Long userId, @Nonnull Long eventId);

    EventRequestVo acceptEventRequest(@Nonnull Long eventId);

    EventRequestVo rejectEventRequest(@Nonnull Long eventRequestId);
}
