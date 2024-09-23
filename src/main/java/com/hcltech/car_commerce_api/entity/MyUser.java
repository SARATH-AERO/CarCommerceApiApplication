package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; //  if you plan on integrating across multiple services or systems,
    // using UUID ensures no collision in ID generation, even across different services or databases

    @Column(unique = true, nullable = false)
    private String username;

    @NonNull
    private String password;

    @NonNull
    private boolean enabled;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Authority> authorities;

}
