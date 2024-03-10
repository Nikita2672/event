package com.example.event.service;

import com.example.event.model.Phone;

import java.util.Optional;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
public interface PhoneService {
    Optional<Phone> findById(Long id);

    Long savePhone(String phone);
}
