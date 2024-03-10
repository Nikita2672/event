package com.example.event.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Entity
@Table(name = "phones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
}
