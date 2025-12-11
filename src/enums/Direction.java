package enums;

/** Represents the four cardinal directions for movement on the grid. */
public enum Direction {
  UP,
  DOWN,
  LEFT,
  RIGHT;

  /**
   * Returns the opposite cardinal direction.
   *
   * <p>Useful for logic such as the Sea Lion collision, where the penguin bounces back in the
   * reverse direction of its travel.
   *
   * @return The opposite Direction constant.
   */
  public Direction opposite() {
    return switch (this) {
      case UP -> DOWN;
      case DOWN -> UP;
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
    };
  }
}
