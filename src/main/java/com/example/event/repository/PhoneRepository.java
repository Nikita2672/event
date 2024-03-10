package com.example.event.repository;

import com.example.event.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Phone findByPhoneNumber(String phoneNumber);
}
