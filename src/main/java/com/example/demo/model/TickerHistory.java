package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDate;

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
    private float upperBound;
    private float lowerBound;

    public TickerHistory(String symbol, float avgPrice, float upperBound, float lowerBound) {
        this.symbol = symbol;
        this.date = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        this.avgPrice = avgPrice;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
        timeToBuy = avgPrice > currentPrice;
    }
}
