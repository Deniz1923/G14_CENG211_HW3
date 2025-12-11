package game.util;

import enums.Direction;

/**
 * Static utility class for validating and parsing user input.
 * This class provides validation and conversion methods for various
 * input types used in the Sliding Penguins game.
 * <p>
 * All methods are static and the class is not meant to be instantiated.
 * It serves as a centralized location for input validation logic.
 * <p>
 * Input handling features:
 * - Case-insensitive validation
 * - Whitespace trimming
 * - Null-safe operations
 * - Clear validation rules
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class InputValidator {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private InputValidator() {
        throw new UnsupportedOperationException(
                "InputValidator is a utility class and should not be instantiated."
        );
    }

    /**
     * Validates yes/no input (Y/N).
     * Accepts both uppercase and lowercase, with optional whitespace.
     * <p>
     * Valid inputs:
     * - "Y" or "y" (with or without surrounding whitespace)
     * - "N" or "n" (with or without surrounding whitespace)
     * <p>
     * Example:
     * isValidYesNo("Y")    -> true
     * isValidYesNo("  n ") -> true
     * isValidYesNo("yes")  -> false
     * isValidYesNo(null)   -> false
     *
     * @param input The user input string to validate
     * @return true if input is valid Y/N, false otherwise
     */
    public static boolean isValidYesNo(String input) {
        if (input == null) {
            return false;
        }

        try {
            String normalized = input.trim().toUpperCase();
            return normalized.equals("Y") || normalized.equals("N");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates direction input (U/D/L/R).
     * Accepts both uppercase and lowercase, with optional whitespace.
     * <p>
     * Valid inputs:
     * - "U" or "u" -> Up
     * - "D" or "d" -> Down
     * - "L" or "l" -> Left
     * - "R" or "r" -> Right
     * <p>
     * Example:
     * isValidDirection("U")     -> true
     * isValidDirection("  l  ") -> true
     * isValidDirection("up")    -> false
     * isValidDirection(null)    -> false
     *
     * @param input The user input string to validate
     * @return true if input is valid direction, false otherwise
     */
    public static boolean isValidDirection(String input) {
        if (input == null) {
            return false;
        }

        try {
            String normalized = input.trim().toUpperCase();
            return normalized.equals("U")
                    || normalized.equals("D")
                    || normalized.equals("L")
                    || normalized.equals("R");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses yes/no input to boolean value.
     * Should only be called after isValidYesNo() returns true.
     * <p>
     * Conversion:
     * - "Y" or "y" -> true
     * - "N" or "n" -> false
     * <p>
     * This method is case-insensitive and trims whitespace.
     *
     * @param input The validated user input (Y or N)
     * @return true for Y, false for N
     * @throws IllegalArgumentException if input is null or invalid
     */
    public static boolean parseYesNo(String input) {
        if (input == null) {
            throw new IllegalArgumentException(
                    "InputValidator Error: Cannot parse null input."
            );
        }

        try {
            return input.trim().equalsIgnoreCase("Y");
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "InputValidator Error: Failed to parse yes/no input: " + input, e
            );
        }
    }

    /**
     * Parses direction input to Direction enum.
     * Should only be called after isValidDirection() returns true.
     * <p>
     * Conversion mapping:
     * - "U" or "u" -> Direction.UP
     * - "D" or "d" -> Direction.DOWN
     * - "L" or "l" -> Direction.LEFT
     * - "R" or "r" -> Direction.RIGHT
     * <p>
     * This method is case-insensitive and trims whitespace.
     *
     * @param input The validated user input (U/D/L/R)
     * @return The corresponding Direction enum value
     * @throws IllegalArgumentException if input is null or invalid
     */
    public static Direction parseDirection(String input) {
        if (input == null) {
            throw new IllegalArgumentException(
                    "InputValidator Error: Cannot parse null input."
            );
        }

        try {
            return switch (input.trim().toUpperCase()) {
                case "U" -> Direction.UP;
                case "D" -> Direction.DOWN;
                case "L" -> Direction.LEFT;
                case "R" -> Direction.RIGHT;
                default -> throw new IllegalArgumentException(
                        "Invalid direction input: " + input +
                                ". Expected U, D, L, or R."
                );
            };
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "InputValidator Error: Failed to parse direction: " + input, e
            );
        }
    }

    /**
     * Validates and parses yes/no input in one step.
     * Convenience method that combines validation and parsing.
     *
     * @param input The user input to validate and parse
     * @return true for valid Y input, false for valid N input
     * @throws IllegalArgumentException if input is invalid
     */
    public static boolean validateAndParseYesNo(String input) {
        if (!isValidYesNo(input)) {
            throw new IllegalArgumentException(
                    "InputValidator Error: Invalid yes/no input: " + input +
                            ". Expected Y or N."
            );
        }
        return parseYesNo(input);
    }

    /**
     * Validates and parses direction input in one step.
     * Convenience method that combines validation and parsing.
     *
     * @param input The user input to validate and parse
     * @return The corresponding Direction enum value
     * @throws IllegalArgumentException if input is invalid
     */
    public static Direction validateAndParseDirection(String input) {
        if (!isValidDirection(input)) {
            throw new IllegalArgumentException(
                    "InputValidator Error: Invalid direction input: " + input +
                            ". Expected U, D, L, or R."
            );
        }
        return parseDirection(input);
    }
}