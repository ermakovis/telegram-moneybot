package ru.ermakovis.moneybot.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ermakovis.moneybot.Entry;
import ru.ermakovis.moneybot.EntryRepository;
import ru.ermakovis.moneybot.handler.MessageHandler;
import ru.ermakovis.moneybot.util.Util;

@Slf4j
@RequiredArgsConstructor
@Component
public class NumberHandler implements MessageHandler {
    private final EntryRepository repository;

    @Override
    public boolean canHandle(String message) {
        return Util.isNumeric(message);
    }

    @Override
    public String handle(String message) {
        log.info("Handling number command");
        repository.save(new Entry(Double.parseDouble(message)));
        return "Записано";
    }
}
