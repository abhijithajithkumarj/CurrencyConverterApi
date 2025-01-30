package com.currencyexchange.currency_converter.currencyconverter.model;

import lombok.Data;
import java.util.Map;

@Data
public class ExchangeRateResponse {

    private String base;

    private Map<String, Double> rates;
}
