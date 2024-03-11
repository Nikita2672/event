package com.example.event.controller;

import com.example.event.service.UserService;
import com.example.event.view.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserVo>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/permission")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> givePermission(@RequestBody String username) {
        UserVo responseUserVo = userService.givePermissions(username);
        if (responseUserVo == null) {
            return ResponseEntity.badRequest().body("There is no user with " + username + "name");
        }
        return ResponseEntity.ok(responseUserVo);
    }
}
