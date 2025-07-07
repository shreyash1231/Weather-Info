package com.weather.weather_report.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.weather.weather_report.entity.WeatherInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExternalAPIClient {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] getLatLongFromPincode(String pincode) {
        String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + apiKey;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map body = response.getBody();
        return new double[]{(double) body.get("lat"), (double) body.get("lon")};
    }

    public WeatherInfo getWeather(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                     "&lon=" + lon + "&appid=" + apiKey + "&units=metric";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map body = response.getBody();

        WeatherInfo info = new WeatherInfo();
        info.setWeather(((Map)((List) body.get("weather")).get(0)).get("description").toString());
        info.setTemperature(((Map) body.get("main")).get("temp") instanceof Number
                ? ((Number) ((Map) body.get("main")).get("temp")).doubleValue() : 0.0);
        info.setHumidity(((Number) ((Map) body.get("main")).get("humidity")).doubleValue());

        return info;
    }
}
