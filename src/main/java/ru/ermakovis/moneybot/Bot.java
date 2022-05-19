package ru.ermakovis.moneybot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ermakovis.moneybot.handler.MessageHandler;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final List<MessageHandler> handlerList;

    @Value("${bot.name}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();

        handlerList.forEach(handler -> {
                if (handler.canHandle(message)) {
                    sendMsg(chatId, handler.handle(message));
                }
            });
    }

    public synchronized void sendMsg(String chatId, String s) {
        if (s == null || s.isEmpty()) {
            return;
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
