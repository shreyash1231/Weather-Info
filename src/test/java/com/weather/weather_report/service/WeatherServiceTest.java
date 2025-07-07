
package com.weather.weather_report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.weather.weather_report.dto.WeatherResponseDTO;
import com.weather.weather_report.entity.PincodeLocation;
import com.weather.weather_report.entity.WeatherInfo;
import com.weather.weather_report.repository.PincodeRepository;
import com.weather.weather_report.repository.WeatherRepository;
import com.weather.weather_report.util.ExternalAPIClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WeatherServiceTest {

    @Mock
    private PincodeRepository pincodeRepo;

    @Mock
    private WeatherRepository weatherRepo;

    @Mock
    private ExternalAPIClient apiClient;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeatherInfo_WhenCachedExists() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);

        WeatherInfo cached = new WeatherInfo();
        cached.setPincode(pincode);
        cached.setDate(date);
        cached.setWeather("clear sky");
        cached.setTemperature(28.5);
        cached.setHumidity(60.0);

        when(weatherRepo.findByPincodeAndDate(pincode, date)).thenReturn(Optional.of(cached));

        WeatherResponseDTO result = weatherService.getWeatherInfo(pincode, date);

        assertEquals("clear sky", result.getWeather());
        assertEquals(28.5, result.getTemperature());
        verify(weatherRepo, times(1)).findByPincodeAndDate(pincode, date);
        verifyNoMoreInteractions(apiClient);
    }

    @Test
    void testGetWeatherInfo_WhenPincodeNotExistsInDb() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);

        double[] latLng = {18.5204, 73.8567};
        PincodeLocation savedLocation = new PincodeLocation(pincode, latLng[0], latLng[1]);

        WeatherInfo fetchedWeather = new WeatherInfo();
        fetchedWeather.setWeather("cloudy");
        fetchedWeather.setTemperature(25.0);
        fetchedWeather.setHumidity(80.0);

        when(weatherRepo.findByPincodeAndDate(pincode, date)).thenReturn(Optional.empty());
        when(pincodeRepo.findById(pincode)).thenReturn(Optional.empty());
        when(apiClient.getLatLongFromPincode(pincode)).thenReturn(latLng);
        when(pincodeRepo.save(any(PincodeLocation.class))).thenReturn(savedLocation);
        when(apiClient.getWeather(latLng[0], latLng[1])).thenReturn(fetchedWeather);
        when(weatherRepo.save(any(WeatherInfo.class))).thenReturn(fetchedWeather);

        WeatherResponseDTO result = weatherService.getWeatherInfo(pincode, date);

        assertEquals("cloudy", result.getWeather());
        assertEquals(25.0, result.getTemperature());
        verify(apiClient).getLatLongFromPincode(pincode);
        verify(apiClient).getWeather(latLng[0], latLng[1]);
    }
}
