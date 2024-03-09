package com.example.event.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;

    public JwtResponse(String token, Long id, String username, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
