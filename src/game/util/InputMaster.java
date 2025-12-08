package game.util;

import enums.Direction;

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
      String input = scanner.nextLine();

      if (InputValidator.isValidYesNo(input)) {
        return InputValidator.parseYesNo(input);
      }
      System.out.println("Invalid input. Please enter Y or N.");
    }
  }

  /** Get direction input from user */
  public Direction getDirectionInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();

      if (InputValidator.isValidDirection(input)) {
        Direction direction = InputValidator.parseDirection(input);
        if (direction != null) {
          return direction;
        }
      }
      System.out.println("Invalid input. Please enter U, D, L, or R.");
    }
  }

  /** Close the scanner when done */
  public void close() {
    scanner.close();
  }
}
