package com.cryptobot.service;

import com.cryptobot.client.BinanceClient;
import com.cryptobot.entity.Subscribers;
import com.cryptobot.repository.SubscribersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class CryptoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;
    private final SubscribersRepository subscribersRepository;

    public double getBitcoinPrice(){
        if (price.get() == null) {
            updatePrice();
        }
        return price.get();
    }

    public List<Subscribers> getSubscribers(){
        return subscribersRepository.findAll();
    }
    public Subscribers getSubscriber(Long telegramId) {
        return subscribersRepository.findByTelegramUserId(telegramId);
    }
    @Transactional
    public void setSubscribers(Long telegramUserId, Double pricingSubscription) {
        Subscribers subscribers = subscribersRepository.findByTelegramUserId(telegramUserId);
        subscribers.setPricingSubscription(pricingSubscription);
    }
    @Transactional
    public void unsubscribe(Long telegramUserId) {
        Subscribers subscribers = subscribersRepository.findByTelegramUserId(telegramUserId);
        subscribers.setPricingSubscription(null);
    }

    @Transactional
    public void createSubscriber(Long subscriberId) {
        if (!subscribersRepository.existsByTelegramUserId(subscriberId)){
            Subscribers subscribers = new Subscribers();
            subscribers.setTelegramUserId(subscriberId);
            subscribersRepository.save(subscribers);
        }
    }

    public void updateLastNotice(Subscribers subscribers, LocalDateTime lastNotice) {
        subscribers.setLastNotice(lastNotice);
        subscribersRepository.save(subscribers);
    }

    public void updatePrice() {
        try {
            price.set(client.getBitcoinPrice().getPrice());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Subscribers> getSubscribersBefore(Double subscription) {
        return subscribersRepository.findByPricingSubscriptionAfter(subscription);
    }

}
