package com.example.event.view;

import com.example.event.model.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Getter
@AllArgsConstructor
public class EventRequestVo {
        @JsonProperty("status") Status status;
        @JsonProperty("appealText") String appealText;
        @JsonProperty("phone") String phone;
        @JsonProperty("name") String name;
        @JsonProperty("creationDate") LocalDateTime creationDate;
        @JsonProperty("username") String username = "";
}
