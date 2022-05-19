package ru.ermakovis.moneybot.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ermakovis.moneybot.Entry;
import ru.ermakovis.moneybot.EntryRepository;
import ru.ermakovis.moneybot.handler.MessageHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class BalanceCommandHandler implements MessageHandler {
    public static final String COMMAND_HASH = "#balance";
    public static final String COMMAND_SLASH = "/balance";

    private final EntryRepository repository;

    @Override
    public boolean canHandle(String message) {
        return COMMAND_HASH.equals(message) ||
                COMMAND_SLASH.equals(message);
    }

    @Override
    public String handle(String message) {
        log.info("Handling balance command");
        double sum = repository.findAll()
                .stream()
                .mapToDouble(Entry::getAmount)
                .sum();
        return String.valueOf(sum);
    }
}
