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

The server includes a MathService with properly configured MCP tools:

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

### Available Tools
- **Tool**: `addNumbers(double number1, double number2)`
- **Description**: Adds two numbers and returns the result
- **Location**: `src/main/java/com/rw/mcp_server/MathService.java`

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
- Tool registration is confirmed in startup logs: "Registered tools: 1"

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