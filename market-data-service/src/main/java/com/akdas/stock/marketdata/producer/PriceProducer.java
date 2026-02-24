package com.akdas.stock.marketdata.producer;

import com.akdas.stock.marketdata.model.PriceTick;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceProducer {

    private final KafkaTemplate<String, PriceTick> kafkaTemplate;

    public void send(PriceTick tick) {
        kafkaTemplate.send("price-ticks", tick.getSymbol(), tick);
    }
}