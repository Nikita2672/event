package com.example.event.client;

import feign.RequestInterceptor;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Configuration
public class MyClientConfiguration {

    @Value("${dadata.api.token}")
    private String apiToken;

    @Value("${dadata.api.secret}")
    private String secretToken;

    @Bean
    public OkHttpClient client() {
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(5000, MILLISECONDS)
                .readTimeout(5000, MILLISECONDS);

        return new OkHttpClient(builder.build());
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Authorization", "Token " + apiToken);
            requestTemplate.header("X-Secret", secretToken);
        };
    }
}