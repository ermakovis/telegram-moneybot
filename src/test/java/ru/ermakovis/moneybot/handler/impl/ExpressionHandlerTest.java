package ru.ermakovis.moneybot.handler.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import ru.ermakovis.moneybot.Entry;
import ru.ermakovis.moneybot.EntryRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpressionHandlerTest {
    private final EntryRepository entryRepository = mock(EntryRepository.class);

    private final ExpressionHandler underTest = new ExpressionHandler(entryRepository);

    @ParameterizedTest
    @ValueSource(strings = {
            "1 + 1", "1 * (1 + 1)"
    })
    void shouldReturnTrueOnExpression(String message) {
        assertTrue(underTest.canHandle(message));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "1", BalanceCommandHandler.COMMAND_SLASH, "Text", "", " "
    })
    void shouldReturnFalseOnNonExpression(String message) {
        assertFalse(underTest.canHandle(message));
    }

    @Test
    void shouldSaveExpressionResult() {
        //given
        var entryCaptor = ArgumentCaptor.forClass(Entry.class);

        //when
        underTest.handle("10 + (15 * 2)");

        //then
        verify(entryRepository).save(entryCaptor.capture());

        assertEquals(40.0, entryCaptor.getValue().getAmount());

    }
}