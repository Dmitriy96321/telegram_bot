package com.cryptobot.bot.command;

import com.cryptobot.dto.CoinPrice;
import com.cryptobot.service.CryptoCurrencyService;
import com.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Обработка команды получения текущей стоимости валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetPriceCommand implements IBotCommand {

    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "get_price";
    }

    @Override
    public String getDescription() {
        return "Возвращает цену биткоина в USD";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {

            CoinPrice coinPrice = service.getBitcoinPrice();
            answer.setText("Текущая цена биткоина " + TextUtil.toString(coinPrice.getPrice()) + " USD - " +
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(coinPrice.getCloseTime()), ZoneId.systemDefault()));
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }

        Long asdf = message.getFrom().getId();
        System.out.println(asdf);
    }
}