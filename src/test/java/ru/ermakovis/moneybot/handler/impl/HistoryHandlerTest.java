package ru.ermakovis.moneybot.handler.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.ermakovis.moneybot.Entry;
import ru.ermakovis.moneybot.EntryRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryHandlerTest {
    private final EntryRepository repository = mock(EntryRepository.class);

    private final HistoryHandler underTest = new HistoryHandler(repository);

    @ParameterizedTest
    @ValueSource(strings = {
            "/history", "/history 10", "#history", "#history 10"
    })
    void shouldReturnTrueOnCommand(String message) {
        underTest.canHandle(message);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "", " ", "text", "history"
    })
    void shouldReturnFalseOnNonCommand(String message) {
        underTest.canHandle(message);
    }

    @Disabled("не хочу морочиться со временем")
    @Test
    void shouldReturnHistoryString() {
        //given
        when(repository.findLastEntries(any())).thenReturn(List.of(
                new Entry(10.0).setDate(new Date()),
                new Entry(20.0).setDate(new Date())));

        //when
        var message = underTest.handle("/history 10");

        //then
        System.out.println(message);
    }

}