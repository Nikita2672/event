package com.example.event.service;

import com.example.event.view.UserVo;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public interface UserService {

    List<UserVo> getUsers();

    UserVo givePermissions(String username);
}
