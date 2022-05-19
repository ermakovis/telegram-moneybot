package ru.ermakovis.moneybot.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Component;
import ru.ermakovis.moneybot.Entry;
import ru.ermakovis.moneybot.EntryRepository;
import ru.ermakovis.moneybot.handler.MessageHandler;
import ru.ermakovis.moneybot.util.Util;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExpressionHandler implements MessageHandler {
    private final EntryRepository repository;

    @Override
    public boolean canHandle(String message) {
        if (message == null || Util.isNumeric(message)) {
            return false;
        }

        try {
            new ExpressionBuilder(message).build();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String handle(String message) {
        log.info("Handling expression command");
        Double result = new ExpressionBuilder(message).build().evaluate();
        repository.save(new Entry(result));
        return "Записано";
    }
}
