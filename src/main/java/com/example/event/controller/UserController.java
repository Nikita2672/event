package com.example.event.controller;

import com.example.event.service.UserService;
import com.example.event.view.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserVo>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/permission")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserVo> givePermission(@RequestBody UserVo userVo) {
        UserVo responseUserVo = userService.givePermissions(userVo.username());
        if (responseUserVo == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(responseUserVo);
    }
}
