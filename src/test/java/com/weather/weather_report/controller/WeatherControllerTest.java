
package com.weather.weather_report.controller;

import com.weather.weather_report.dto.WeatherResponseDTO;
import com.weather.weather_report.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetWeather_Returns200() throws Exception {
        WeatherResponseDTO mockResponse = new WeatherResponseDTO("411014", LocalDate.of(2020, 10, 15), "clear sky", 28.5, 60.0);
        Mockito.when(weatherService.getWeatherInfo(eq("411014"), eq(LocalDate.of(2020, 10, 15))))
               .thenReturn(mockResponse);

        mockMvc.perform(get("/api/weather")
                        .param("pincode", "411014")
                        .param("for_date", "2020-10-15"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pincode").value("411014"))
               .andExpect(jsonPath("$.weather").value("clear sky"))
               .andExpect(jsonPath("$.temperature").value(28.5));
    }
}
