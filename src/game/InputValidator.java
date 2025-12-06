package game;

import enums.Direction;

/** STATIC Utility class for validating user input */
public class InputValidator {

  /** Validate yes/no input (Y/N) */
  public static boolean isValidYesNo(String input) {
    if (input == null) return false;
    String normalized = input.trim().toUpperCase();
    return normalized.equals("Y") || normalized.equals("N");
  }

  /** Validate direction input (U/D/L/R) */
  public static boolean isValidDirection(String input) {
    if (input == null) return false;
    String normalized = input.trim().toUpperCase();
    return normalized.equals("U")
        || normalized.equals("D")
        || normalized.equals("L")
        || normalized.equals("R");
  }

  /** Parse yes/no input to boolean */
  public static boolean parseYesNo(String input) {
    return input.trim().equalsIgnoreCase("Y");
  }

  /** Parse direction input to Direction enum */
  public static Direction parseDirection(String input) {
    return switch (input.trim().toUpperCase()) {
      case "U" -> Direction.UP;
      case "D" -> Direction.DOWN;
      case "L" -> Direction.LEFT;
      case "R" -> Direction.RIGHT;
      default -> throw new IllegalArgumentException("Invalid direction: " + input);
    };
  }
}
