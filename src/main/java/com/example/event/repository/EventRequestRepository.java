package com.example.event.repository;

import com.example.event.model.EventRequest;
import com.example.event.model.Status;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    Page<EventRequest> findAllByUserId(long userId, @Nonnull Pageable pageable);

    List<EventRequest> findAllByUserId(long userId);

    Page<EventRequest> findAllByStatus(@Nonnull Pageable pageable, @Nonnull Status status);

    Page<EventRequest> findAllByUserIdAndNameAndStatus(@Nonnull Pageable pageable, long userId, String name, @Nonnull Status status);

    Page<EventRequest> findAllByUserIdAndStatus(@Nonnull Pageable pageable, long userId, @Nonnull Status status);

    Page<EventRequest> findAllByNameAndStatus(@Nonnull Pageable pageable, @Nonnull String name, @Nonnull Status status);


    Page<EventRequest> findAllByNameAndStatusIn(@Nonnull Pageable pageable, @Nonnull String name, @Nonnull List<Status> statusList);

    Page<EventRequest> findAllByStatusIn(@Nonnull Pageable pageable, @Nonnull List<Status> statusList);

}
