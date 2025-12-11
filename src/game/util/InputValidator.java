package game.util;

import enums.Direction;

/** STATIC Utility class for validating user input */
public class InputValidator {

  /** Validate yes/no input (Y/N) */
  public static boolean isValidYesNo(String input) {
    if (input == null) return false;
    try {
      String normalized = input.trim().toUpperCase();
      return normalized.equals("Y") || normalized.equals("N");
    } catch (Exception e) {
      return false;
    }
  }

    /** Validate direction input (U/D/L/R) */
  public static boolean isValidDirection(String input) {
    if (input == null) return false;
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

  /** Parse yes/no input to boolean */
  public static boolean parseYesNo(String input) {
    try {
      if (input == null) return false;
      return input.trim().equalsIgnoreCase("Y");
    } catch (Exception e) {
      System.err.println("Error parsing Yes/No: " + e.getMessage());
      return false;
    }
  }

  /** Parse direction input to Direction enum */
  public static Direction parseDirection(String input) {
    try {
      if (input == null) throw new IllegalArgumentException("Input cannot be null");
      return switch (input.trim().toUpperCase()) {
        case "U" -> Direction.UP;
        case "D" -> Direction.DOWN;
        case "L" -> Direction.LEFT;
        case "R" -> Direction.RIGHT;
        default -> throw new IllegalArgumentException("Invalid direction: " + input);
      };
    } catch (IllegalArgumentException e) {
      throw e; // Re-throw expected validations
    } catch (Exception e) {
      throw new IllegalArgumentException("Unexpected error parsing direction: " + e.getMessage());
    }
  }
}
