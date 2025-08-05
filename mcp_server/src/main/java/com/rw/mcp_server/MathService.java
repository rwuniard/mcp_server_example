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

    @Tool(description = "Subtract two numbers and return the result")
    public double subtractNumbers(double number1, double number2) {
        return number1 - number2;
    }

    /**
     * Calculate the modulo (remainder) of two numbers.
     * This method follows IEEE 754 floating-point arithmetic rules for special values.
     * 
     * @param number1 the dividend
     * @param number2 the divisor
     * @return the remainder of number1 divided by number2
     * @throws IllegalArgumentException if number2 is zero
     */
    @Tool(description = "Calculate the modulo (remainder) of two numbers")
    public double moduloNumbers(double number1, double number2) {
        // Handle zero divisor case
        if (number2 == 0.0) {
            throw new IllegalArgumentException("Cannot calculate modulo with divisor zero");
        }
        
        // Handle NaN cases - return NaN if either input is NaN
        if (Double.isNaN(number1) || Double.isNaN(number2)) {
            return Double.NaN;
        }
        
        // Handle infinity cases - return NaN for any infinity input
        if (Double.isInfinite(number1) || Double.isInfinite(number2)) {
            return Double.NaN;
        }
        
        return number1 % number2;
    }
}