package com.example.event.config.converter.impl;

import com.example.event.config.converter.EventRequestConverter;
import com.example.event.model.EventRequest;
import com.example.event.view.EventRequestVo;
import org.springframework.stereotype.Component;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Component
public class EventRequestConverterImpl implements EventRequestConverter {

    @Override
    public EventRequestVo convertToVo(EventRequest eventRequest) {
        return new EventRequestVo(
                eventRequest.getStatus(),
                eventRequest.getAppealText(),
                eventRequest.getPhone(),
                eventRequest.getName()
        );
    }
}
