package com.example.event.view;

import com.example.event.model.Role;

import java.util.Set;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public record UserVo(
        String username,
        Set<Role> roles
) {

}
