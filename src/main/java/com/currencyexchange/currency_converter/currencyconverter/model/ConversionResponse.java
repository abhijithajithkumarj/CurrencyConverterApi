package com.currencyexchange.currency_converter.currencyconverter.model;

import lombok.Data;

@Data
public class ConversionResponse {

    private String from;

    private String to;

    private double amount;

    private double convertedAmount;
}
