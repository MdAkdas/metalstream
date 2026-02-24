package com.akdas.stock.marketdata.simulator;

import com.akdas.stock.marketdata.model.PriceTick;
import com.akdas.stock.marketdata.producer.PriceProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MarketSimulator {

    private final PriceProducer producer;
    private final Random random = new Random();

    private final List<String> symbols =
            List.of("TCS","INFY","RELIANCE","HDFCBANK");

    @Scheduled(fixedRate = 1500)
    public void generateTick() {

        String symbol = symbols.get(random.nextInt(symbols.size()));

        PriceTick tick = new PriceTick(
                symbol,
                BigDecimal.valueOf(1000 + random.nextDouble() * 3000),
                Instant.now()
        );

        producer.send(tick);

        System.out.println("Produced: " + tick);
    }
}
