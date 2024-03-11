package com.example.event.service.impl;

import com.example.event.model.Phone;
import com.example.event.client.PhoneClient;
import com.example.event.repository.PhoneRepository;
import com.example.event.client.PhoneResponse;
import com.example.event.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneClient jsonPlaceholderClient;

    private final PhoneRepository phoneRepository;

    private static final String MOBILE_TYPE = "Мобильный";

    public Optional<Phone> findById(Long id) {
        return phoneRepository.findById(id);
    }

    public long savePhone(String phone) {
        PhoneResponse phoneResponse = getPhoneInfo(List.of(phone)).get(0);
        if (phoneResponse.type() == null || !phoneResponse.type().equals(MOBILE_TYPE)) return 0L;
        if (!phoneRepository.existsByPhoneNumber(phoneResponse.phone())) {
            return phoneRepository.save(convertToPhone(phoneResponse)).getId();
        }
        return phoneRepository.findByPhoneNumber(phoneResponse.phone()).getId();
    }

    private List<PhoneResponse> getPhoneInfo(List<String> phones) {
        return jsonPlaceholderClient.cleanPhone(phones);
    }

    private Phone convertToPhone(PhoneResponse phoneResponse) {
        return Phone.builder()
                .countryCode(phoneResponse.country_code())
                .cityCode(phoneResponse.city_code())
                .phoneNumber(phoneResponse.phone())
                .build();
    }
}

