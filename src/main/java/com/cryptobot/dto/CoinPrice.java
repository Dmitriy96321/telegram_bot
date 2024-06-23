package com.cryptobot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CoinPrice {
    double price;
    long closeTime;
}
