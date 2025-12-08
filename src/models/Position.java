package models;

import java.util.Objects;

public class Position {
  private int x;
  private int y;

  public Position() {
    this(0, 0);
  }

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position(Position otherPosition) {
    if (otherPosition != null) {
      this.x = otherPosition.x;
      this.y = otherPosition.y;
    }
  }

  public void setPositionX(int x) {
    if (x >= 0) {
      this.x = x;
    }
  }

  public void setPositionY(int y) {
    if (y >= 0) {
      this.y = y;
    }
  }

  public Position getPosition() {
    return new Position(this.x, this.y);
  }

  // Add these to Position.java
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String displayPosition() {
    return "X: " + x + " Y: " + y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "X: " + x + " Y: " + y;
  }
}
