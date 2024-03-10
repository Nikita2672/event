package com.example.event.view;

import com.example.event.model.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public record EventRequestVo(
        @JsonProperty("status") Status status,
        @JsonProperty("appealText") String appealText,
        @JsonProperty("phone") String phone,
        @JsonProperty("name") String name,
        @JsonProperty("creationDate") LocalDateTime creationDate
) {

}
