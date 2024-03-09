package com.example.event.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nivanov
 * @since %CURRENT_VERSION%
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20)
    private ERole name;

    public Role(ERole role) {
        this.name = role;
    }

    public Role() {

    }
}
