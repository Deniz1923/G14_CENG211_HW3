package game.util;

import enums.Direction;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class for taking input from the User. Please call {@link #close()} when the last input is taken.
 */
public class InputMaster {
  private final Scanner scanner;

  public InputMaster() {
    this.scanner = new Scanner(System.in);
  }

  /** Get yes/no input from user */
  public boolean getYesNoInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      try {
        String input = scanner.nextLine();

        if (InputValidator.isValidYesNo(input)) {
          return InputValidator.parseYesNo(input);
        }
        System.out.println("Invalid input. Please enter Y or N.");

      } catch (NoSuchElementException | IllegalStateException e) {
        System.err.println("Error reading input. The input stream might be closed.");
        // Return a default safe value or re-throw depending on game design
        return false;
      } catch (Exception e) {
        System.out.println("An unexpected error occurred reading input: " + e.getMessage());
      }
    }
  }

  /** Get direction input from user */
  public Direction getDirectionInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      try {
        String input = scanner.nextLine();

        if (InputValidator.isValidDirection(input)) {
          // Inner try-catch for the parsing logic specifically
          try {
            Direction direction = InputValidator.parseDirection(input);
            if (direction != null) {
              return direction;
            }
          } catch (IllegalArgumentException ex) {
            System.out.println("Error processing direction: " + ex.getMessage());
          }
        }
        System.out.println("Invalid input. Please enter U, D, L, or R.");

      } catch (NoSuchElementException | IllegalStateException e) {
        System.err.println("Error reading input. The input stream might be closed.");
        return Direction.UP; // Return default to prevent null pointers upstream
      } catch (Exception e) {
        System.out.println("An unexpected error occurred reading input: " + e.getMessage());
      }
    }
  }

  /** Close the scanner when done */
  public void close() {
    try {
      scanner.close();
    } catch (Exception e) {
      System.err.println("Warning: Failed to close scanner properly.");
    }
  }
}
