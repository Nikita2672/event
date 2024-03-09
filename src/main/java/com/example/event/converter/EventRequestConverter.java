package com.example.event.converter;

import com.example.event.model.EventRequest;
import com.example.event.view.EventRequestVo;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public interface EventRequestConverter {

    EventRequestVo convertToVo(EventRequest eventRequest);
}
