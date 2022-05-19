package ru.ermakovis.moneybot.handler;

public interface MessageHandler {
    boolean canHandle(String message);
    String handle(String message);
}
