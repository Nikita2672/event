package com.example.event.view;

import com.example.event.model.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public record EventRequestVo(
        @JsonProperty("status") Status status,
        @JsonProperty("appealText") String appealText,
        @JsonProperty("phone") String phone,
        @JsonProperty("name") String name
) {
}
