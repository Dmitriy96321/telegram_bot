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
public class StartCommand implements IBotCommand {
    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        cryptoCurrencyService.createSubscriber(message.getFrom().getId());
        answer.setText(MessageFormat.format("""
                Привет {0}! Данный бот помогает отслеживать стоимость биткоина.
                Можно установить подписку на желаемую цену биткоина,
                 когда цена опустится до нее, бот отправит уведомление.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина.
                 /get_subscription - получить информацию о подписке.
                 /subscribe [число] - подписатся на стоимость BTC в USD.
                 /unsubscribe - отменить подписку.
                """ ,message.getFrom().getFirstName()));
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }

    }

}