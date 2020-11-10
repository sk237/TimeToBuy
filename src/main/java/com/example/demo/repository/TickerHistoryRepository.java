package com.example.demo.repository;

import com.example.demo.model.TickerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface TickerHistoryRepository extends JpaRepository<TickerHistory, String> {
    Optional<TickerHistory> findBySymbolAndDate(String symbol, Timestamp date);
}
