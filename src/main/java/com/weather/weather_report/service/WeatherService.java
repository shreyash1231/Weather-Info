package com.weather.weather_report.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weather.weather_report.dto.WeatherResponseDTO;
import com.weather.weather_report.entity.PincodeLocation;
import com.weather.weather_report.entity.WeatherInfo;
import com.weather.weather_report.repository.PincodeRepository;
import com.weather.weather_report.repository.WeatherRepository;
import com.weather.weather_report.util.ExternalAPIClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final PincodeRepository pincodeRepo;
    private final WeatherRepository weatherRepo;
    private final ExternalAPIClient apiClient;

    public WeatherResponseDTO getWeatherInfo(String pincode, LocalDate forDate) {
        Optional<WeatherInfo> cached = weatherRepo.findByPincodeAndDate(pincode, forDate);
        if (cached.isPresent()) {
            return toDTO(cached.get());
        }

        PincodeLocation location = pincodeRepo.findById(pincode).orElseGet(() -> {
            double[] latLng = apiClient.getLatLongFromPincode(pincode);
            PincodeLocation loc = new PincodeLocation(pincode, latLng[0], latLng[1]);
            return pincodeRepo.save(loc);
        });

        WeatherInfo weather = apiClient.getWeather(location.getLatitude(), location.getLongitude());
        weather.setPincode(pincode);
        weather.setDate(forDate);
        weatherRepo.save(weather);
        return toDTO(weather);
    }

    private WeatherResponseDTO toDTO(WeatherInfo info) {
        return new WeatherResponseDTO(info.getPincode(), info.getDate(), info.getWeather(),
                info.getTemperature(), info.getHumidity());
    }
}

