package com.cryptobot.bot.command;

import com.cryptobot.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscribeCommand implements IBotCommand {
    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        try {
            answer.setChatId(message.getChatId());
            Double subscribe = Double.valueOf(arguments[0]
                    .replaceAll("[^0-9,.]", "")
                    .replaceAll(",",".")
            );
            cryptoCurrencyService.setSubscribers(message.getChatId(), subscribe);
            answer.setText(MessageFormat.format("""
                    {0} Вы установили подписку на {1} стоимость BTC!
                    """, message.getFrom().getFirstName(), subscribe));

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            answer.setText("Задан не верный формат. " +
                    "Верный формат \"/subscribe 1234.5678\"");

        }
        try {
            absSender.execute(answer);
        } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }

    }
}