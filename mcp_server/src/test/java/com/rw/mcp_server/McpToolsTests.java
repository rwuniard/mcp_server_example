package com.rw.mcp_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testModuloNumbers() {
        assertEquals(1.0, mathService.moduloNumbers(7.0, 3.0)); // 7 % 3 = 1
        assertEquals(0.0, mathService.moduloNumbers(6.0, 3.0)); // 6 % 3 = 0
        assertEquals(2.5, mathService.moduloNumbers(5.5, 3.0)); // 5.5 % 3 = 2.5
        assertEquals(-1.0, mathService.moduloNumbers(-7.0, 3.0)); // -7 % 3 = -1
        assertEquals(1.0, mathService.moduloNumbers(7.0, -3.0)); // 7 % -3 = 1
    }

    @Test
    void testModuloNumbers_DivisionByZero() {
        assertThrows(IllegalArgumentException.class, 
            () -> mathService.moduloNumbers(5.0, 0.0));
        assertThrows(IllegalArgumentException.class, 
            () -> mathService.moduloNumbers(-5.0, 0.0));
    }
    
    @Test
    void testModuloNumbers_EdgeCases() {
        // NaN handling
        assertTrue(Double.isNaN(mathService.moduloNumbers(Double.NaN, 3.0)));
        assertTrue(Double.isNaN(mathService.moduloNumbers(5.0, Double.NaN)));
        assertTrue(Double.isNaN(mathService.moduloNumbers(Double.NaN, Double.NaN)));
        
        // Infinity handling
        assertTrue(Double.isNaN(mathService.moduloNumbers(5.0, Double.POSITIVE_INFINITY)));
        assertTrue(Double.isNaN(mathService.moduloNumbers(5.0, Double.NEGATIVE_INFINITY)));
        assertTrue(Double.isNaN(mathService.moduloNumbers(Double.POSITIVE_INFINITY, 3.0)));
        assertTrue(Double.isNaN(mathService.moduloNumbers(Double.NEGATIVE_INFINITY, 3.0)));
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
