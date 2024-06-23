package com.cryptobot.service;

import com.cryptobot.client.BinanceClient;
import com.cryptobot.dto.CoinPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class CryptoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;

    public CryptoCurrencyService(BinanceClient client) {
        this.client = client;
    }

    public CoinPrice getBitcoinPrice() throws IOException {
//        if (price.get() == null) {
//            price.set(client.getBitcoinPrice());
//        }

        return client.getBitcoinPrice();
//        return price.get();
    }
}
