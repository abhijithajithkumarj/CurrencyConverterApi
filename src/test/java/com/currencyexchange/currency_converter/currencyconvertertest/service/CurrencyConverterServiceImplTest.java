package com.currencyexchange.currency_converter.currencyconvertertest.service;


import com.currencyexchange.currency_converter.currencyconverter.model.ConversionRequest;
import com.currencyexchange.currency_converter.currencyconverter.model.ConversionResponse;
import com.currencyexchange.currency_converter.currencyconverter.model.ExchangeRateResponse;
import com.currencyexchange.currency_converter.currencyconverter.service.impl.CurrencyConverterServiceImpl;
import com.currencyexchange.currency_converter.exception.CurrencyConverterException;
import com.currencyexchange.currency_converter.utiils.ErrorConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterServiceImplTest {

    @InjectMocks
    private CurrencyConverterServiceImpl currencyConverterService;

    @Mock
    private RestTemplate restTemplate;

    private static final String BASE_CURRENCY = "USD";

    private static final String TARGET_CURRENCY = "EUR";

    private static final double AMOUNT = 100.0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRatesSuccess() {
        Map<String, Double> mockRates = new HashMap<>();
        mockRates.put("EUR", 0.85);
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(mockRates);

        ResponseEntity<ExchangeRateResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ExchangeRateResponse.class)))
                .thenReturn(responseEntity);

        Map<String, Double> rates = currencyConverterService.getExchangeRates(BASE_CURRENCY);

        assertNotNull(rates);
        assertEquals(0.85, rates.get("EUR"));
    }

    @Test
    void testGetExchangeRatesFailure() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ExchangeRateResponse.class)))
                .thenThrow(new RuntimeException("API Error"));

        CurrencyConverterException exception = assertThrows(CurrencyConverterException.class, () -> {
            currencyConverterService.getExchangeRates(BASE_CURRENCY);
        });

        assertEquals(ErrorConstants.ERROR_WHILE_FETCHING_EXCHANGE_RATES, exception.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
    }

    @Test
    void testConvertAmountSuccess() {
        Map<String, Double> fromRates = new HashMap<>();
        fromRates.put(BASE_CURRENCY, 1.0);

        Map<String, Double> toRates = new HashMap<>();
        toRates.put(TARGET_CURRENCY, 0.85);

        ExchangeRateResponse fromResponse = new ExchangeRateResponse();
        fromResponse.setBase(BASE_CURRENCY);
        fromResponse.setRates(fromRates);

        ExchangeRateResponse toResponse = new ExchangeRateResponse();
        toResponse.setBase(TARGET_CURRENCY);
        toResponse.setRates(toRates);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ExchangeRateResponse.class)))
                .thenReturn(new ResponseEntity<>(fromResponse, HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(toResponse, HttpStatus.OK));

        ConversionRequest request = new ConversionRequest();
        request.setFrom(BASE_CURRENCY);
        request.setTo(TARGET_CURRENCY);
        request.setAmount(AMOUNT);

        ConversionResponse response = currencyConverterService.convertAmount(request);

        assertNotNull(response);
        assertEquals(AMOUNT * 0.85, response.getConvertedAmount());
        assertEquals(TARGET_CURRENCY, response.getTo());
    }

    @Test
    void testConvertAmountInvalidFromCurrency() {
        Map<String, Double> fromRates = new HashMap<>();
        fromRates.put("GBP", 0.75);

        ExchangeRateResponse fromResponse = new ExchangeRateResponse();
        fromResponse.setBase("GBP");
        fromResponse.setRates(fromRates);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ExchangeRateResponse.class)))
                .thenReturn(new ResponseEntity<>(fromResponse, HttpStatus.OK));

        ConversionRequest request = new ConversionRequest();
        request.setFrom("USD");
        request.setTo(TARGET_CURRENCY);
        request.setAmount(AMOUNT);

        CurrencyConverterException exception = assertThrows(CurrencyConverterException.class, () -> {
            currencyConverterService.convertAmount(request);
        });

        assertEquals(ErrorConstants.ERROR_INVALID_FROM_CURRENCY_CODE_PROVIDED, exception.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
    }

}