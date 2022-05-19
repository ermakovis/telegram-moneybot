package ru.ermakovis.moneybot.util;

public class Util {
    public static boolean isNumeric(String message) {
        return message.matches("-?\\d+(\\.\\d+)?");
    }
}
