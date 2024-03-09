package com.example.event.service.impl;

import com.example.event.model.ERole;
import com.example.event.model.Role;
import com.example.event.model.User;
import com.example.event.repository.RoleRepository;
import com.example.event.repository.UserRepository;
import com.example.event.service.UserService;
import com.example.event.view.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Override
    public List<UserVo> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserVo(user.getUsername(), user.getRoles()))
                .toList();
    }

    @Override
    public UserVo givePermissions(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_MODERATOR)) {
                return null;
            }
            user.getRoles().add(roleRepository.findByName(ERole.ROLE_MODERATOR));
            user = userRepository.save(user);
            return new UserVo(user.getUsername(), user.getRoles());
        }
        return null;
    }
}
