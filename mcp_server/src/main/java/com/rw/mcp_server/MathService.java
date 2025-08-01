package com.rw.mcp_server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class MathService {
    
    @Tool(description = "Add two numbers and return the result")
    public double addNumbers(double number1, double number2) {
        return number1 + number2;
    }
    
    @Tool(description = "Multiply two numbers and return the result")
    public double multiplyNumbers(double number1, double number2) {
        return number1 * number2;
    }
}