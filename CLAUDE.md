# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot Model Context Protocol (MCP) server project using Spring AI. The project is configured to use Java 21 and Spring Boot 3.5.4 with Spring AI 1.0.0.

## Development Commands

### Build and Run
```bash
cd mcp_server
./mvnw spring-boot:run        # Run the application
./mvnw clean compile          # Clean and compile
./mvnw package               # Build JAR package
```

### Testing
```bash
./mvnw test                  # Run all tests
./mvnw test -Dtest=McpServerApplicationTests  # Run specific test class
```

### Development
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"  # Run with dev profile
```

## Architecture

### Project Structure
- **Main application**: `src/main/java/com/rw/mcp_server/McpServerApplication.java` - Standard Spring Boot application entry point
- **Package structure**: `com.rw.mcp_server` - Root package for all application code
- **Maven wrapper**: Use `./mvnw` instead of `mvn` for consistent Maven version

### Key Dependencies
- **Spring Boot Web**: For REST API functionality
- **Spring AI MCP Server WebMVC**: Core MCP server implementation with Spring MVC integration
- **Spring Boot Test**: Testing framework with JUnit 5

### Configuration
- **Application properties**: `src/main/resources/application.properties` contains MCP server configuration including SSE endpoints
- **SSE Endpoint**: `/sse` - Main MCP connection endpoint
- **Message Endpoint**: `/mcp/messages` - Internal MCP messaging

## MCP Tools

The server includes multiple services with properly configured MCP tools:

### Creating MCP Tools
1. **Annotate methods** with `@Tool` annotation:
   ```java
   @Tool(description = "Add two numbers and return the result")
   public double addNumbers(double number1, double number2) {
       return number1 + number2;
   }
   ```

2. **Register tool provider** in main application:
   ```java
   @Bean
   public ToolCallbackProvider mathTools(MathService mathService) {
       return MethodToolCallbackProvider.builder()
           .toolObjects(mathService)
           .build();
   }
   ```

### Available Services & Tools

#### MathService (`src/main/java/com/rw/mcp_server/MathService.java`)
- **`addNumbers(double number1, double number2)`**: Adds two numbers and returns the result
- **`multiplyNumbers(double number1, double number2)`**: Multiplies two numbers and returns the result

#### WeatherService (`src/main/java/com/rw/mcp_server/WeatherService.java`)
- **`getWeather(String cityName)`**: Get current weather information for a city
  - Supports major cities: New York, London, Tokyo, Paris, Sydney, Toronto
  - Returns temperature, conditions, humidity, and wind information
- **`getWeatherForecast(String cityName, int days)`**: Get weather forecast for multiple days (1-7 days)
  - Returns detailed daily forecast with temperature and conditions

### Tool Usage Examples

**Math Operations:**
```
addNumbers: number1=5.0, number2=3.0 → Result: 8.0
multiplyNumbers: number1=4.0, number2=6.0 → Result: 24.0
```

**Weather Information:**
```
getWeather: cityName="New York" → "Weather in New York: 72°F (22°C), Partly Cloudy, Humidity: 65%, Wind: 8 mph NW"
getWeatherForecast: cityName="London", days=3 → Multi-day forecast with daily breakdown
```

## Testing the MCP Server

### Run the Server
```bash
cd mcp_server
./mvnw spring-boot:run
```

The server will start on port 8080 with the following MCP capabilities enabled:
- Tools capabilities (with change notifications)
- Resources capabilities (with change notifications)  
- Prompts capabilities (with change notifications)
- Completions capabilities

### Test with MCP Inspector
```bash
# Install the MCP inspector tool
npm install -g @modelcontextprotocol/inspector

# Run the inspector to test the server
npx @modelcontextprotocol/inspector@latest
```

**MCP Inspector Connection Settings:**
- **Transport Type**: SSE (Server-Sent Events)
- **URL**: `http://localhost:8080/sse`
- **Authentication**: None required for local development

## Development Notes

- MCP tools must be annotated with `@Tool` and registered via `ToolCallbackProvider` beans
- The server uses SSE (Server-Sent Events) transport for MCP protocol communication
- The project uses Maven wrapper (`mvnw`) for build consistency
- Java 21 is required for this project
- Spring AI MCP Server starter provides auto-configuration for MCP protocol endpoints
- Tool registration is confirmed in startup logs: "Registered tools: 4"
- Each service requires its own `ToolCallbackProvider` bean registration
- Multiple services can be combined in a single MCP server
- Weather data is simulated (in production, integrate with real weather APIs)

## Required Dependencies

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

## Required Imports for MCP Tools

```java
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
```

## Project Structure

```
src/main/java/com/rw/mcp_server/
├── McpServerApplication.java    # Main application with tool registrations
├── MathService.java            # Mathematical operations (add, multiply)  
└── WeatherService.java         # Weather information tools
```

## Extending the Server

To add new MCP tools:

1. **Create a new service class** with `@Service` annotation
2. **Add tool methods** with `@Tool` annotations and descriptions
3. **Register the service** by adding a `ToolCallbackProvider` bean in `McpServerApplication`
4. **Test** - the new tools will automatically appear in MCP Inspector

Example of adding a new service:
```java
@Bean
public ToolCallbackProvider newServiceTools(NewService newService) {
    return MethodToolCallbackProvider.builder()
        .toolObjects(newService)
        .build();
}
```