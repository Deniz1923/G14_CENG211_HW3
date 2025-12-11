package models;

import java.util.Objects;

/** Represents a 2D coordinate with non-negative X and Y values. */
public class Position {
  private final int x; // Made final for immutability
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // Defensive Copy Constructor
  public Position(Position other) {
    if (other == null) {
      throw new IllegalArgumentException("Position cannot be null");
    }
    this.x = other.x;
    this.y = other.y;
  }

  /** Initializes a new position at (0, 0). */
  public Position() {
    this(0, 0);
  }

  /**
   * Returns a new Position object representing the current coordinates.
   *
   * @return A copy of the current position.
   */
  public Position getPosition() {
    return new Position(this.x, this.y);
  }

  /**
   * Gets the current x-coordinate.
   *
   * @return The x-coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the current y-coordinate.
   *
   * @return The y-coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Returns a formatted string display of the coordinates.
   *
   * @return String in format "X: [x] Y: [y]".
   */
  public String displayPosition() {
    return "X: " + x + " Y: " + y;
  }

  /**
   * Checks if this position is equal to another object based on coordinates.
   *
   * @param o The object to compare.
   * @return True if coordinates match, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  /**
   * Generates a hash code for this position.
   *
   * @return The hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Returns the string representation of the position.
   *
   * @return String in format "X: [x] Y: [y]".
   */
  @Override
  public String toString() {
    return "X: " + x + " Y: " + y;
  }
}
