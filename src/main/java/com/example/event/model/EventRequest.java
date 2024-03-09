package com.example.event.model;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "appeal_text")
    private String appealText;

    @Column(name = "phone")
    private String phone;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "user_id")
    @JoinColumn(name = "user_id", table = "users", referencedColumnName = "id")
    private Long userId;

    @Column(name = "operator_id")
    @JoinColumn(name = "operator_id", table = "users", referencedColumnName = "id")
    private Long operatorId;
}
