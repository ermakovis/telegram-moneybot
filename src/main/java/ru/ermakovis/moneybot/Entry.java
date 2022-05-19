package ru.ermakovis.moneybot;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Date;

@Document(collection = "transactions")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Entry {

    private @Id BigInteger id;
    private Double amount;
    private @CreatedDate Date date;
    private @Version Long version;

    public Entry(Double amount) {
        this.amount = amount;
    }
}
