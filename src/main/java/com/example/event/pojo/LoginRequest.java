package com.example.event.pojo;

import lombok.Data;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
