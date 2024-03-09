package com.example.event.service;

import com.example.event.view.UserVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Service
public interface UserService {

    List<UserVo> getUsers();

    UserVo givePermissions(String username);
}
