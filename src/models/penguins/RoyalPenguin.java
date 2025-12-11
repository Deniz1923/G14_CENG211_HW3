package models.penguins;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.IHazard;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;

public class RoyalPenguin extends Penguin {
  private boolean useAbilityThisTurn = false;

  public RoyalPenguin(Position position) {
    super(PenguinType.ROYAL, position);
  }

  /**
   * RoyalPenguin: Before they start sliding, they can choose to safely move into an adjacent square
   * (only horizontally and vertically). It is possible to accidentally step out of the grid and
   * fall into water while using this ability (or other similar accidents).
   */
  @Override
  public void specialAbility() {
    if (!isAbilityUsed()) {
      useAbilityThisTurn = true;
      setAbilityUsed(true);
    }
  }

  public void performSpecialMove(TerrainGrid grid, Direction direction) {
    if (!useAbilityThisTurn) {
      return;
    }

    System.out.println(getNotation() + " moves one square " + getDirectionName(direction) + ".");

    int nextY = getPosition().getY();
    int nextX = getPosition().getX();

    switch (direction) {
      case UP:
        nextY--;
        break;
      case DOWN:
        nextY++;
        break;
      case LEFT:
        nextX--;
        break;
      case RIGHT:
        nextX++;
        break;
    }

    Position nextPos = new Position(nextX, nextY);

    // Check if falling into water
    if (nextX < 0 || nextY < 0 || nextY >= 10 || nextX >= 10) {
      System.out.println(getNotation() + " falls into the water while using special ability!");
      grid.removeObject(getPosition());
      setPosition(null);
      useAbilityThisTurn = false;
      return;
    }

    ITerrainObject obstacle = grid.getObjectAt(nextPos);

    if (obstacle == null) {
      // Empty square
      updatePositionOnGrid(grid, nextPos);
    } else if (obstacle instanceof Food food) {
      // Collect food
      grid.removeObject(nextPos);
      updatePositionOnGrid(grid, nextPos);
      pickupFood(food);
    } else {
      // Hit something (hazard or penguin) - still moves but consequences apply
      updatePositionOnGrid(grid, nextPos);
      if (obstacle instanceof IHazard hazard) {
        hazard.onCollision(this, grid);
      }
    }

    useAbilityThisTurn = false;
  }

  public boolean shouldUseAbilityThisTurn() {
    return useAbilityThisTurn;
  }

  private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
    grid.removeObject(getPosition());
    setPosition(newPosition);
    grid.placeObject(newPosition, this);
  }

  private String getDirectionName(Direction dir) {
    return switch (dir) {
      case UP -> "to the UP";
      case DOWN -> "to the DOWN";
      case LEFT -> "to the LEFT";
      case RIGHT -> "to the RIGHT";
    };
  }
}
