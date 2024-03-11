package com.example.event.client;


public record PhoneResponse(
        String source,
        String type,
        String phone,
        String country_code,
        String city_code,
        String number,
        String extension,
        String provider,
        String country,
        String region,
        String city,
        String timezone,
        int qc_conflict,
        int qc
) {
}

