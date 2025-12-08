package game;

import enums.Direction;

/** Handles all movement logic for penguins and sliding objects. */
public class MovementController {
  private final TerrainGrid grid;

  public MovementController(TerrainGrid grid) {
    this.grid = grid;
  }

  private String getDirectionName(Direction dir) {
    return switch (dir) {
      case UP -> "upwards";
      case DOWN -> "downwards";
      case LEFT -> "to the left";
      case RIGHT -> "to the right";
    };
  }
}
