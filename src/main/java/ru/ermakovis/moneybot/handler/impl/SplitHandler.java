package ru.ermakovis.moneybot.handler.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import ru.ermakovis.moneybot.exception.IncorrectInputException;
import ru.ermakovis.moneybot.handler.MessageHandler;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class SplitHandler implements MessageHandler {
    public static final String MISSING_CONTEXT_MESSAGE = "Missing context";
    public static final String INCORRECT_ENTRY_MESSAGE = "Incorrect entry";

    @Override
    public boolean canHandle(String message) {
        var lines = message.split("\n");
        if (lines.length == 0) {
            return false;
        }
        return lines[0].equals("/split");
    }

    @Override
    public String handle(String message) {
        try {
            var lines = message.split("\n");
            if (lines.length < 2) {
                throw new IncorrectInputException(MISSING_CONTEXT_MESSAGE);
            }
            lines = Arrays.copyOfRange(lines, 1, lines.length);

            var personList = Arrays.stream(lines)
                    .map(Person::parse)
                    .toList();

            var sum = personList.stream().map(Person::getBalance).reduce(0, Integer::sum);
            var share = sum / personList.size();
            personList.forEach(person -> person.setBalance(person.getBalance() - share));

            var transactionList = getTransactionList(personList);
            var sb = new StringBuilder();
            transactionList.forEach(transaction -> sb.append(String.format("%d %s -> %s%n",
                    transaction.getAmount(), transaction.getFrom(), transaction.getTo())));


            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    private List<Transaction> getTransactionList(List<Person> personList) {
        var sortedList = personList.stream()
                .sorted(Comparator.comparing(Person::getBalance))
                .toArray(Person[]::new);

        var i = 0;
        var j = sortedList.length - 1;
        var transactionList = new ArrayList<Transaction>();

        while (i < j) {
            var dept = Math.min(- sortedList[i].getBalance(), sortedList[j].getBalance());
            sortedList[i].increaseBalance(dept);
            sortedList[j].decreaseBalance(dept);

            var transaction = new Transaction()
                    .setFrom(sortedList[i].getName())
                    .setTo(sortedList[j].getName())
                    .setAmount(dept);

            if(sortedList[i].getBalance() == 0) {
                i++;
            }
            if(sortedList[j].getBalance() == 0) {
                j--;
            }

            transactionList.add(transaction);
        }
        return transactionList;
    }

    @Data
    @AllArgsConstructor
    private static class Person {
        @NotNull
        private String name;
        @NotNull
        private Integer balance;

        public static Person parse(String line) {
            var words = line.split("\s+");
            if (words.length != 2) {
                throw new IncorrectInputException(String.format("%s - %s", INCORRECT_ENTRY_MESSAGE, line));
            }

            var name = words[0];
            var balance = Integer.parseInt(words[1]);

            return new Person(name, balance);
        }

        public void increaseBalance(Integer change) {
            balance += change;
        }

        public void decreaseBalance(Integer change) {
            balance -= change;
        }
    }

    @Data
    @Accessors(chain = true)
    private static class Transaction {
        @NotNull
        private String from;
        @NotNull
        private String to;
        @NotNull
        private Integer amount;
    }
}
