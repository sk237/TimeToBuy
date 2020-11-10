package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@Entity
@NoArgsConstructor
public class TickerHistory {

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String symbol;
    private Timestamp date;
    private float avgPrice;
    private float currentPrice;
    private boolean timeToBuy;
    public TickerHistory(String symbol, float avgPrice) {
        this.symbol = symbol;
        this.date = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        this.avgPrice = avgPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
        timeToBuy = avgPrice > currentPrice;
    }
}
