package com.swapnil.notification_system.entity;

import com.swapnil.notification_system.enums.DeliveryStatus;
import com.swapnil.notification_system.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_history")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class NotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //to store enum as text
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    //to store enum as text
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    //notification title
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    //time when notification was processed
    @Column(nullable = false)
    private LocalDateTime sentAt;





}
