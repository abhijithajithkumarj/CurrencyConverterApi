package com.currencyexchange.currency_converter.currencyconverter.service;

import com.currencyexchange.currency_converter.currencyconverter.model.ConversionRequest;
import com.currencyexchange.currency_converter.currencyconverter.model.ConversionResponse;

import java.util.Map;

public interface CurrencyConverterService {

    Map<String, Double> getExchangeRates(String baseCurrency);

    ConversionResponse convertAmount(ConversionRequest request);
}
