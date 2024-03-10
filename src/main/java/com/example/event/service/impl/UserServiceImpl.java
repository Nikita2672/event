package com.example.event.service.impl;

import com.example.event.model.ERole;
import com.example.event.model.User;
import com.example.event.repository.RoleRepository;
import com.example.event.repository.UserRepository;
import com.example.event.service.UserService;
import com.example.event.view.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                .map(this::convertToVo)
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
            return convertToVo(userRepository.save(user));
        }
        return null;
    }

    private UserVo convertToVo(User user) {
        Set<String> roleNames = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new UserVo(user.getUsername(), roleNames);
    }

}
