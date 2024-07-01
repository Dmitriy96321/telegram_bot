package com.cryptobot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "subscribers")
@NoArgsConstructor
@AllArgsConstructor
public class Subscribers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "telegram_user_id", nullable = false)
    private Long telegramUserId;

    @Column(name = "pricing_subscription")
    private Double pricingSubscription;

    @Column(name = "last_notice")
    private LocalDateTime lastNotice;
}
