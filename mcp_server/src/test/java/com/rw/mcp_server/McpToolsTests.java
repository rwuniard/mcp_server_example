package com.rw.mcp_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class McpToolsTests {

    @Autowired
    private MathService mathService;

    @Autowired
    private WeatherService weatherService;

    // MathService Tests

    @Test
    void testAddNumbers() {
        assertEquals(5.0, mathService.addNumbers(2.0, 3.0));
    }

    @Test
    void testMultiplyNumbers() {
        assertEquals(6.0, mathService.multiplyNumbers(2.0, 3.0));
    }

    @Test
    void testSubtractNumbers() {
        assertEquals(-1.0, mathService.subtractNumbers(2.0, 3.0));
    }
    
    @Test
    void testMathOperations_EdgeCases() {
     assertEquals(0.0, mathService.subtractNumbers(5.0, 5.0)); // Same numbers
        assertEquals(Double.POSITIVE_INFINITY, mathService.multiplyNumbers(Double.MAX_VALUE, 2.0)); // Overflow
        assertTrue(Double.isNaN(mathService.addNumbers(Double.NaN, 1.0))); // NaN handling
    }


    // WeatherService Tests

    @Test
    void testGetWeather_KnownCity() {
        String weather = weatherService.getWeather("london");
        assertNotNull(weather);
        assertTrue(weather.contains("Weather in London"));
    }

    @Test
    void testGetWeather_UnknownCity() {
        String cityName = "Atlantis";
        String weather = weatherService.getWeather(cityName);
        assertNotNull(weather);
        assertTrue(weather.contains("Weather data for " + cityName));
    }

    @Test
    void testGetWeatherForecast_ValidDays() {
        String forecast = weatherService.getWeatherForecast("paris", 3);
        assertNotNull(forecast);
        assertTrue(forecast.contains("Weather forecast for paris (3 days)"));
        assertTrue(forecast.contains("Day 1:"));
        assertTrue(forecast.contains("Day 2:"));
        assertTrue(forecast.contains("Day 3:"));
    }

    @Test
    void testGetWeatherForecast_InvalidDays() {
        String forecast_zero_days = weatherService.getWeatherForecast("tokyo", 0);
        assertEquals("Forecast is only available for 1-7 days", forecast_zero_days);

        String forecast_eight_days = weatherService.getWeatherForecast("tokyo", 8);
        assertEquals("Forecast is only available for 1-7 days", forecast_eight_days);
    }
}
