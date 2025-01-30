package com.currencyexchange.currency_converter.currencyconverter.controller;

import com.currencyexchange.currency_converter.currencyconverter.model.ConversionRequest;
import com.currencyexchange.currency_converter.currencyconverter.model.ConversionResponse;
import com.currencyexchange.currency_converter.currencyconverter.service.CurrencyConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Currency Converter")
public class CurrencyController {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @Operation(summary = "Fetch exchange rates data")
    @GetMapping( value = "/rates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Double>> getRates(@RequestParam(defaultValue = "USD") String base) {
        log.info("Fetching exchange rates for base currency: {}", base);
        Map<String, Double> rates = currencyConverterService.getExchangeRates(base);
        return ResponseEntity.ok(rates);
    }

    @Operation(summary = "Convert currency")
    @PostMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConversionResponse> convertAmount(@RequestBody ConversionRequest request) {
        log.info("Received request to convert amount: {} {} to {}", request.getAmount(), request.getFrom(), request.getTo());
        ConversionResponse response = currencyConverterService.convertAmount(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
