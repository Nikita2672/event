package com.example.event.repository;

import com.example.event.model.EventRequest;
import com.example.event.model.Status;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    Page<EventRequest> findAllByUserId(Long userId, @Nonnull Pageable pageable);

    List<EventRequest> findAllByUserId(Long userId);

    Page<EventRequest> findAllByNameAndStatus(@Nonnull Pageable pageable, String name, Status status);

    Page<EventRequest> findAllByNameAndStatusIn(@Nonnull Pageable pageable, String name, List<Status> statusList);

}
