package com.swapnil.notification_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean emailEnabled;

    @Column(nullable = false)
    private boolean smsEnabled;

    @Column(nullable = false)
    private boolean pushEnabled;

    @Column(nullable = false)
    private boolean inAppEnabled;
}
