package com.familywealth.tracker.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class Users {

    @Id
    private UUID id;

    private String name;

    @Column(name = "created_at")
    private Instant createdAt;
}