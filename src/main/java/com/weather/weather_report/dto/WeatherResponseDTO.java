package com.weather.weather_report.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponseDTO {
    private String pincode;
    private LocalDate date;
    private String weather;
    private double temperature;
    private double humidity;
}
