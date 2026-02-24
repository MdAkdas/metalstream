package com.akdas.stock.marketdata.simulator;

import com.akdas.stock.marketdata.model.PriceTick;
import com.akdas.stock.marketdata.producer.PriceProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MarketSimulator {

    private final PriceProducer producer;
    private final Random random = new Random();

    private final List<String> symbols =
            List.of("GLD", "IAU", "SLV", "SIVR");

    private final Map<String, double[]> priceBands = Map.of(
            "GLD", new double[]{180, 260},
            "IAU", new double[]{35, 55},
            "SLV", new double[]{18, 38},
            "SIVR", new double[]{20, 40}
    );
    private final Map<String, Double> maxMovePct = Map.of(
            "GLD", 0.0040,
            "IAU", 0.0045,
            "SLV", 0.0075,
            "SIVR", 0.0075
    );
    private final Map<String, Double> lastPrices = new java.util.HashMap<>();

    @Scheduled(fixedRate = 1500)
    public void generateTick() {

        String symbol = symbols.get(random.nextInt(symbols.size()));
        double[] band = priceBands.get(symbol);
        double previousPrice = lastPrices.computeIfAbsent(symbol,
                s -> band[0] + random.nextDouble() * (band[1] - band[0]));

        double stepPct = (random.nextDouble() * 2 - 1) * maxMovePct.get(symbol);
        double nextPrice = previousPrice * (1 + stepPct);
        double boundedPrice = Math.max(band[0], Math.min(band[1], nextPrice));
        lastPrices.put(symbol, boundedPrice);

        PriceTick tick = new PriceTick(
                symbol,
                BigDecimal.valueOf(boundedPrice).setScale(2, RoundingMode.HALF_UP),
                Instant.now()
        );

        producer.send(tick);

        System.out.println("Produced: " + tick);
    }
}
