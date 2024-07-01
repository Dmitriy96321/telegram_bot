package com.cryptobot.bot;

import com.cryptobot.entity.Subscribers;
import com.cryptobot.service.CryptoCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingCommandBot {
    private final CryptoCurrencyService cryptoService;
    private final String botUsername;


    public CryptoBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            List<IBotCommand> commandList, CryptoCurrencyService cryptoService) {
        super(botToken);
        this.botUsername = botUsername;

        commandList.forEach(this::register);
        this.cryptoService = cryptoService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
    }

    @Scheduled(fixedRate = 120_000)
    public void scheduleCheckBTC(){
        cryptoService.updatePrice();
        double btcPrice = cryptoService.getBitcoinPrice();
        List<Subscribers> list = cryptoService.getSubscribersBefore(btcPrice);
        if (!list.isEmpty()) {
            list.stream()
                    .filter(subscriber -> subscriber.getLastNotice() == null ||
                            (Duration.between(subscriber.getLastNotice(), LocalDateTime.now()).toMinutes() > 9))
                    .forEach(subscriber -> {
                cryptoService.updateLastNotice(subscriber, LocalDateTime.now());
                SendMessage answer = new SendMessage();
                answer.setChatId(subscriber.getTelegramUserId());
                        System.out.println();
                answer.setText("Пора покупать, стоимость биткоина  опустилась до "
                        + btcPrice + " USD.");
                try {
                    execute(answer);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
