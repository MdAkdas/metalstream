package com.akdas.stock.marketdata.model;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceTick {

    private String symbol;
    private BigDecimal price;
    private Instant timestamp;
}
