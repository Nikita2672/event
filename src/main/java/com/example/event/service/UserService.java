package com.example.event.service;

import com.example.event.view.UserVo;

import java.util.List;


public interface UserService {

    List<UserVo> getUsers();

    UserVo givePermissions(String username);
}
