package com.cryptobot.repository;

import com.cryptobot.entity.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribersRepository extends JpaRepository<Subscribers, Long> {
}
