package com.example.event.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;


public record UserVo(
        @JsonProperty("username")
        String username,
        @JsonProperty("roles")
        Set<String> roles
) {

}
