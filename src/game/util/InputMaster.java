package game.util;

import enums.Direction;
import java.util.Scanner;

/**
 * Handles all user input for the Sliding Penguins game.
 * This class provides methods to get validated input from the player,
 * including yes/no responses and directional commands.
 *
 * Input validation:
 * - Case-insensitive (Y/y/N/n accepted)
 * - Automatic retry on invalid input
 * - Clear error messages for user guidance
 *
 * IMPORTANT: Always call close() when finished
 * with input to properly release the Scanner resource!
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class InputMaster {
  /** Scanner for reading console input */
  private final Scanner scanner;

  /**
   * Constructs an InputMaster with a new Scanner for System.in.
   * The scanner will remain open until close() is called.
   */
  public InputMaster() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Gets yes/no input from the user with validation.
   * Continuously prompts until valid input is received.
   *
   * Valid inputs (case-insensitive):
   * - "Y" or "y" -> returns true
   * - "N" or "n" -> returns false
   *
   * Invalid input displays error message and re-prompts.
   *
   * Example usage:
   * boolean useAbility = inputMaster.getYesNoInput(
   * "Use special ability? (Y/N): "
   * );
   *
   * @param prompt The message to display to the user
   * @return true if user enters Y/y, false if user enters N/n
   * @throws IllegalArgumentException if prompt is null
   */
  public boolean getYesNoInput(String prompt) {
    if (prompt == null) {
      throw new IllegalArgumentException(
              "InputMaster Error: Prompt cannot be null."
      );
    }

    while (true) {
      try {
        System.out.print(prompt);
        String input = scanner.nextLine();

        if (InputValidator.isValidYesNo(input)) {
          return InputValidator.parseYesNo(input);
        }

        System.out.println("Invalid input. Please enter Y or N.");
      } catch (Exception e) {
        System.err.println("Error reading input: " + e.getMessage());
        System.out.println("Please try again.");
      }
    }
  }

  /**
   * Gets directional input from the user with validation.
   * Continuously prompts until valid input is received.
   *
   * Valid inputs (case-insensitive):
   * - "U" or "u" -> Direction.UP
   * - "D" or "d" -> Direction.DOWN
   * - "L" or "l" -> Direction.LEFT
   * - "R" or "r" -> Direction.RIGHT
   *
   * Invalid input displays error message and re-prompts.
   *
   * Example usage:
   * Direction dir = inputMaster.getDirectionInput(
   * "Choose direction (U/D/L/R): "
   * );
   *
   * @param prompt The message to display to the user
   * @return The Direction enum corresponding to user input
   * @throws IllegalArgumentException if prompt is null
   */
  public Direction getDirectionInput(String prompt) {
    if (prompt == null) {
      throw new IllegalArgumentException(
              "InputMaster Error: Prompt cannot be null."
      );
    }

    while (true) {
      try {
        System.out.print(prompt);
        String input = scanner.nextLine();

        if (InputValidator.isValidDirection(input)) {
          Direction direction = InputValidator.parseDirection(input);
          if (direction != null) {
            return direction;
          }
        }

        System.out.println("Invalid input. Please enter U, D, L, or R.");
      } catch (Exception e) {
        System.err.println("Error reading input: " + e.getMessage());
        System.out.println("Please try again.");
      }
    }
  }

  /**
   * Closes the scanner and releases System.in resource.
   *
   * CRITICAL: This method MUST be called when input is no
   * longer needed, typically at the end of the main method.
   *
   * Failing to close the scanner can cause:
   * - Resource leaks
   * - Problems in unit tests
   * - IDE warnings
   *
   * Example:
   * IcyTerrain terrain = new IcyTerrain();
   * terrain.closeInputMaster(); // Always close!
   */
  public void close() {
    try {
        scanner.close();
    } catch (Exception e) {
      System.err.println("Error closing scanner: " + e.getMessage());
    }
  }

  /**
   * Checks if the scanner is still open and functional.
   * Useful for testing and debugging.
   *
   * @return true if scanner can still be used, false otherwise
   */
  public boolean isOpen() {
    return scanner.hasNextLine();
  }
}