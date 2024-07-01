package com.cryptobot.bot.command;

import com.cryptobot.entity.Subscribers;
import com.cryptobot.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {
    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            Subscribers subscribers = cryptoCurrencyService.getSubscriber(message.getFrom().getId());
            if (subscribers.getPricingSubscription()==null){
                answer.setText("Текущаяя подписка не установлена");
                absSender.execute(answer);
                return;
            }

            answer.setText(MessageFormat.format("""
                    Вы вы подписаны на стоимость биткоина {0} USD.
                    """, subscribers.getPricingSubscription()));
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
    }
}