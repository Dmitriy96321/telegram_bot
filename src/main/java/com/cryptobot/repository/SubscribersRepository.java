package com.cryptobot.repository;

import com.cryptobot.entity.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribersRepository extends JpaRepository<Subscribers, Long> {

    Subscribers findByTelegramUserId(Long telegramUserId);

    boolean existsByTelegramUserId(Long telegramUserId);

    List<Subscribers> findByPricingSubscriptionAfter(Double pricingSubscriptionBefore);
}
