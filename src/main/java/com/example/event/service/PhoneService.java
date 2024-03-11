package com.example.event.service;

import com.example.event.model.Phone;

import java.util.Optional;


public interface PhoneService {
    Optional<Phone> findById(Long id);

    long savePhone(String phone);
}
