package com.example.event.service;

import com.example.event.exception.IllegalRequestException;
import com.example.event.view.EventRequestVo;

import java.util.List;


public interface EventRequestService {
    EventRequestVo createEventRequest(EventRequestVo eventRequestVo, long userId) throws IllegalRequestException;

    EventRequestVo getEventRequest(long userId, long eventRequestId) throws IllegalRequestException;

    EventRequestVo getEventRequestById(long eventRequestId) throws IllegalRequestException;

    List<EventRequestVo> getEventRequests(long userId, int pageNumber, String sortOrder);

    List<EventRequestVo> getEventRequests(int pageNumber, String sortOrder, String username, String eventRequestName);

    List<EventRequestVo> getAllEventRequests(int pageNumber, String sortOrder, String eventRequestName);

    EventRequestVo updateEventRequest(long userId, long eventRequestId, EventRequestVo eventRequestVo) throws IllegalRequestException;

    EventRequestVo submitEventRequest(long userId, long eventId) throws IllegalRequestException;

    EventRequestVo acceptEventRequest(long operatorId, long eventId) throws IllegalRequestException;

    EventRequestVo rejectEventRequest(long operatorId, long eventRequestId) throws IllegalRequestException;
}
