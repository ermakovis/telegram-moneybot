package ru.ermakovis.moneybot.handler.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.ermakovis.moneybot.Entry;
import ru.ermakovis.moneybot.EntryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceCommandHandlerTest {
    private final EntryRepository entryRepository = mock(EntryRepository.class);

    private final BalanceCommandHandler underTest = new BalanceCommandHandler(entryRepository);

    @ParameterizedTest
    @ValueSource(strings = {
            BalanceCommandHandler.COMMAND_HASH,
            BalanceCommandHandler.COMMAND_SLASH
    })
    void shouldReturnTrueOnCorrectCommand(String message) {
        assertTrue(underTest.canHandle(message));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "", "  ", "Text", "4", "4 + 4"
    })
    void shouldReturnFalseOnIncorrectCommand(String message) {
        assertFalse(underTest.canHandle(message));
    }

    @Test
    void shouldReturnBalance() {
        //given
        var entries = List.of(
                new Entry(10.0),
                new Entry(10.0),
                new Entry(10.0),
                new Entry(10.0)
        );
        when(entryRepository.findAll()).thenReturn(entries);

        //when
        var result = underTest.handle(BalanceCommandHandler.COMMAND_HASH);

        //then
        assertEquals("40.0", result);
    }
}