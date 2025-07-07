package com.weather.weather_report.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.weather_report.dto.WeatherResponseDTO;
import com.weather.weather_report.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

   @GetMapping
    public ResponseEntity<?> getWeather(
        @RequestParam String pincode,
        @RequestParam("for_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate forDate
    ) {
        try {
            WeatherResponseDTO response = weatherService.getWeatherInfo(pincode, forDate);
            return ResponseEntity.ok(response);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Something went wrong: missing or null data.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

}
