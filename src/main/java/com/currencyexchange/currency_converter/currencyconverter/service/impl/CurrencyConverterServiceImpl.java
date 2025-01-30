package com.currencyexchange.currency_converter.currencyconverter.service.impl;

import com.currencyexchange.currency_converter.currencyconverter.model.ConversionRequest;
import com.currencyexchange.currency_converter.currencyconverter.model.ConversionResponse;
import com.currencyexchange.currency_converter.currencyconverter.model.ExchangeRateResponse;
import com.currencyexchange.currency_converter.currencyconverter.service.CurrencyConverterService;
import com.currencyexchange.currency_converter.exception.CurrencyConverterException;
import com.currencyexchange.currency_converter.utiils.ErrorConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private static final String BASE_URL = "https://api.exchangeratesapi.io/v1/latest?access_key=a92de0a4f42c4315807efd8e762ca4c4";

    private static final String symbols = "&symbols=";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Double> getExchangeRates(String baseCurrency) {
        String url = BASE_URL + symbols + baseCurrency;

        try {
            ResponseEntity<ExchangeRateResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRateResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getRates();
            } else {
                throw new CurrencyConverterException(ErrorConstants.FAILED_TO_FETCH_EXCHANGE_RANGE,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new CurrencyConverterException(ErrorConstants.ERROR_WHILE_FETCHING_EXCHANGE_RATES,
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public ConversionResponse convertAmount(ConversionRequest request) {
        Map<String, Double> fromRates  = getExchangeRates(request.getFrom());
        Map<String, Double> toRates = getExchangeRates(request.getTo());

        if (!fromRates.containsKey(request.getFrom())) {
            throw new CurrencyConverterException(ErrorConstants.ERROR_INVALID_FROM_CURRENCY_CODE_PROVIDED,
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (!toRates.containsKey(request.getTo())) {
            throw new CurrencyConverterException(ErrorConstants.ERROR_INVALID_TO_CURRENCY_CODE_PROVIDED,
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        double fromToRate = fromRates.get(request.getFrom());
        double toCurrencyRate = toRates.get(request.getTo());
        double convertedAmount = (request.getAmount() / fromToRate) * toCurrencyRate;

        ConversionResponse response = new ConversionResponse();
        BeanUtils.copyProperties(request, response);
        response.setConvertedAmount(convertedAmount);
        response.setTo(request.getTo());
        return response;
    }
}
