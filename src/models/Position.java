package models;

import java.util.Objects;

/**
 * Represents a 2D position on the terrain grid with x and y coordinates.
 * This class provides immutable position handling with proper validation
 * and equality checking for grid-based positioning.
 *
 * <p>Coordinates must be non-negative integers. Negative values are rejected
 * during construction or modification to maintain grid integrity.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class Position {
  /** The x-coordinate (column) on the grid */
  private int x;

  /** The y-coordinate (row) on the grid */
  private int y;

  /**
   * Constructs a Position at the origin (0, 0).
   * This is the default position when no coordinates are specified.
   */
  public Position() {
    this(0, 0);
  }

  /**
   * Constructs a Position with the specified x and y coordinates.
   *
   * @param x The x-coordinate (column position)
   * @param y The y-coordinate (row position)
   * @throws IllegalArgumentException if x or y is negative
   */
  public Position(int x, int y) {
    if (x < 0) {
      throw new IllegalArgumentException(
              "Position Error: X-coordinate cannot be negative. Received: " + x
      );
    }
    if (y < 0) {
      throw new IllegalArgumentException(
              "Position Error: Y-coordinate cannot be negative. Received: " + y
      );
    }
    this.x = x;
    this.y = y;
  }

  /**
   * Copy constructor that creates a new Position from another Position.
   *
   * @param otherPosition The position to copy from
   * @throws IllegalArgumentException if otherPosition is null
   */
  public Position(Position otherPosition) {
    if (otherPosition == null) {
      throw new IllegalArgumentException(
              "Position Error: Cannot copy from null position."
      );
    }
    this.x = otherPosition.x;
    this.y = otherPosition.y;
  }

  /**
   * Sets the x-coordinate of this position.
   * Only non-negative values are accepted.
   *
   * @param x The new x-coordinate
   * @throws IllegalArgumentException if x is negative
   */
  public void setPositionX(int x) {
    if (x < 0) {
      throw new IllegalArgumentException(
              "Position Error: X-coordinate cannot be negative. Received: " + x
      );
    }
    this.x = x;
  }

  /**
   * Sets the y-coordinate of this position.
   * Only non-negative values are accepted.
   *
   * @param y The new y-coordinate
   * @throws IllegalArgumentException if y is negative
   */
  public void setPositionY(int y) {
    if (y < 0) {
      throw new IllegalArgumentException(
              "Position Error: Y-coordinate cannot be negative. Received: " + y
      );
    }
    this.y = y;
  }

  /**
   * Returns a copy of this position.
   * This ensures immutability by preventing external modification.
   *
   * @return A new Position object with the same coordinates
   */
  public Position getPosition() {
    return new Position(this.x, this.y);
  }

  /**
   * Gets the x-coordinate (column) of this position.
   *
   * @return The x-coordinate value
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the y-coordinate (row) of this position.
   *
   * @return The y-coordinate value
   */
  public int getY() {
    return y;
  }

  /**
   * Returns a formatted string representation of this position.
   * Format: "X: [x] Y: [y]"
   *
   * @return A human-readable string showing both coordinates
   */
  public String displayPosition() {
    return "X: " + x + " Y: " + y;
  }

  /**
   * Checks if this position is equal to another object.
   * Two positions are equal if they have the same x and y coordinates.
   *
   * @param o The object to compare with
   * @return true if the positions have identical coordinates, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  /**
   * Generates a hash code for this position based on its coordinates.
   * This is used for hash-based collections like HashMap.
   *
   * @return The hash code value
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Returns a string representation of this position.
   * Format: "X: [x] Y: [y]"
   *
   * @return A string representation of the position
   */
  @Override
  public String toString() {
    return "X: " + x + " Y: " + y;
  }
}