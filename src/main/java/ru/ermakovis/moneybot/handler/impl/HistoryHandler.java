package ru.ermakovis.moneybot.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ermakovis.moneybot.EntryRepository;
import ru.ermakovis.moneybot.handler.MessageHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Slf4j
@RequiredArgsConstructor
@Component
public class HistoryHandler implements MessageHandler {
    private final EntryRepository entryRepository;

    @Override
    public boolean canHandle(String message) {
        if (message == null) {
            return false;
        }

        return message.matches("[/#]history \\d+?");
    }

    @Override
    public String handle(String message) {
        log.info("Handling history command");

        var words = message.split("\\s+");
        var dateFormat=  new SimpleDateFormat("HH:mm dd/MM/yyyy");

        var amount = 10;
        if (words.length == 2) {
            amount = Integer.parseInt(words[1]);
        }

        var sb = new StringBuilder();
        entryRepository.findLastEntries(amount).forEach(entry ->
                sb.append(String.format("%-10.2f %s%n", entry.getAmount(), dateFormat.format(entry.getDate()))));

        return sb.toString();

    }
}

