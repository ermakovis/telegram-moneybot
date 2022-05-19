package ru.ermakovis.moneybot.handler.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SplitHandlerTest {
    private final SplitHandler underTest = new SplitHandler();

    @ParameterizedTest
    @ValueSource(strings = {"/split", """
            /split
            A 100
            """})
    void shouldReturnTrueOnMessage(String message) {
        assertTrue(underTest.canHandle(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "split",
            "/splitster",
             "100",
            "",
            "  "
    })
    void shouldReturnFalseOnGenericMessage(String message) {
        assertFalse(underTest.canHandle(message));
    }

    @Test
    void shouldReturnExceptionMessageOnMissingContext() {
        //given
        var message = "/split";

        //when
        var result = underTest.handle(message);

        //then
        assertEquals(SplitHandler.MISSING_CONTEXT_MESSAGE, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
            /split
            A
            """,
            """
            /split
            A 100 100
            """
    })
    void shouldReturnIncorrectEntryMessage(String message) {
        //when
        var result = underTest.handle(message);

        //then
        assertTrue(result.startsWith(SplitHandler.INCORRECT_ENTRY_MESSAGE));
    }

    @Test
    void shouldReturnOperationList() {
        //given
        var message = """
                /split
                A 150
                B 50
                C 0
                D 0
                """;

        //when
        var result = underTest.handle(message);

        //then
        var expectedResult = """
                50 C -> A
                50 D -> A
                """;
        assertEquals(result, expectedResult);
    }
}