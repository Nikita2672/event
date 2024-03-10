package com.example.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        value = "phoneClient",
        url = "https://cleaner.dadata.ru/api/v1/clean",
        configuration = MyClientConfiguration.class
)
public interface PhoneClient {

    @RequestMapping(method = RequestMethod.POST, value = "/phone")
    List<PhoneResponse> cleanPhone(@RequestBody List<String> phones);
}
