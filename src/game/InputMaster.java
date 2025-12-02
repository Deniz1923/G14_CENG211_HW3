package game;

import enums.Direction;
import java.util.Scanner;

public class InputMaster {
  private final Scanner scanner;

  public InputMaster() {
    this.scanner = new Scanner(System.in);
  }

  /** Get yes/no input from user */
  private boolean getYesNoInput(String prompt) {
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
  private Direction getDirectionInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();

      if (InputValidator.isValidDirection(input)) {
        return InputValidator.parseDirection(input);
      }
      System.out.println("Invalid input. Please enter U, D, L, or R.");
    }
  }
}
