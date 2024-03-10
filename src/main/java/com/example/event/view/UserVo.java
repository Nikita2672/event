package com.example.event.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public record UserVo(
        @JsonProperty("username")
        String username,
        @JsonProperty("roles")
        Set<String> roles
) {

}
