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

import java.util.regex.Pattern;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final EntryRepository repository;

    @Value("${bot.name}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();

        if (message == null) return;

        log.info("Message recieved: " + message);
        if ("/balance".equals(message) || "#balance".equals(message)) {
            double sum = repository.findAll()
                    .stream()
                    .mapToDouble(Entry::getAmount)
                    .sum();
            sendMsg(chatId, Double.toString(sum));
        } else if (isNumeric(message)) {
            repository.save(new Entry(Double.parseDouble(message)));
            sendMsg(chatId, "Записано");
        } else if (isMathExpression(message)) {
            Double result = new ExpressionBuilder(message).build().evaluate();
            repository.save(new Entry(result));
            sendMsg(chatId, "Записано " + result);
        }
    }

    private boolean isMathExpression(String message) {
        try {
            new ExpressionBuilder(message).build();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isNumeric(String line) {
        return Pattern.compile("-?\\d+(\\.\\d+)?")
                .matcher(line)
                .matches();
    }
}
