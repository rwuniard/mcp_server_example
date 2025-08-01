package com.rw.mcp_server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    
    @Tool(description = "Get current weather information for a city")
    public String getWeather(String cityName) {
        // Simulate weather data (in a real implementation, you'd call a weather API)
        return switch (cityName.toLowerCase()) {
            case "new york", "nyc" -> 
                "Weather in New York: 72°F (22°C), Partly Cloudy, Humidity: 65%, Wind: 8 mph NW";
            case "london" -> 
                "Weather in London: 59°F (15°C), Overcast, Humidity: 78%, Wind: 12 mph SW";
            case "tokyo" -> 
                "Weather in Tokyo: 75°F (24°C), Sunny, Humidity: 60%, Wind: 5 mph E";
            case "paris" -> 
                "Weather in Paris: 66°F (19°C), Light Rain, Humidity: 82%, Wind: 10 mph W";
            case "sydney" -> 
                "Weather in Sydney: 68°F (20°C), Clear, Humidity: 55%, Wind: 15 mph SE";
            case "toronto" -> 
                "Weather in Toronto: 45°F (7°C), Snow, Humidity: 85%, Wind: 20 mph N";
            case "los angeles", "la" -> 
                "Weather in Los Angeles: 70°F (21°C), Sunny, Humidity: 60%, Wind: 10 mph E";
            default -> 
                String.format("Weather data for %s: 70°F (21°C), Partly Cloudy, Humidity: 60%%, Wind: 10 mph", cityName);
        };
    }
    
    @Tool(description = "Get weather forecast for a city for the next few days")
    public String getWeatherForecast(String cityName, int days) {
        if (days < 1 || days > 7) {
            return "Forecast is only available for 1-7 days";
        }
        
        StringBuilder forecast = new StringBuilder();
        forecast.append(String.format("Weather forecast for %s (%d days):\n", cityName, days));
        
        String[] conditions = {"Sunny", "Partly Cloudy", "Cloudy", "Light Rain", "Clear"};
        int baseTemp = 70;
        
        for (int i = 1; i <= days; i++) {
            String condition = conditions[(i - 1) % conditions.length];
            int temp = baseTemp + (i % 3) * 5 - 5; // Simulate temperature variation
            forecast.append(String.format("Day %d: %d°F (%d°C), %s\n", 
                i, temp, (temp - 32) * 5 / 9, condition));
        }
        
        return forecast.toString();
    }
}