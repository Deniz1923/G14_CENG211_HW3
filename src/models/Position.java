package models;

import java.util.Objects;

/**
 * Represents a position on the terrain grid with x and y coordinates.
 * Coordinates can be any integer value to allow for out-of-bounds checking.
 */
public class Position {
  private int x;
  private int y;

  /**
   * Creates a position at (0, 0).
   */
  public Position() {
    this(0, 0);
  }

  /**
   * Creates a position at the specified coordinates.
   *
   * @param x The x-coordinate
   * @param y The y-coordinate
   */
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Creates a copy of another position.
   *
   * @param otherPosition The position to copy
   */
  public Position(Position otherPosition) {
    if (otherPosition != null) {
      this.x = otherPosition.x;
      this.y = otherPosition.y;
    }
  }


  /**
   * Returns a copy of this position.
   *
   * @return A new Position object with the same coordinates
   */
  public Position getPosition() {
    return new Position(this.x, this.y);
  }

  /**
   * Gets the x-coordinate.
   *
   * @return The x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the y-coordinate.
   *
   * @return The y-coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Returns a string representation of this position for display purposes.
   *
   * @return A formatted string showing the coordinates
   */
  public String displayPosition() {
    return "X: " + x + " Y: " + y;
  }

  /**
   * Checks if this position is equal to another object.
   *
   * @param o The object to compare with
   * @return true if the positions have the same coordinates
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
   * @return The hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Returns a string representation of this position.
   *
   * @return A formatted string showing the coordinates
   */
  @Override
  public String toString() {
    return "X: " + x + " Y: " + y;
  }
}