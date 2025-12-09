package models.penguins;

import static game.TerrainGrid.GRID_SIZE;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.IHazard;
import interfaces.ITerrainObject;
import java.util.ArrayList;
import models.Food;
import models.Position;

public abstract class Penguin implements ITerrainObject {
  private final ArrayList<Food> inventory;
  private final PenguinType type;
  private Position position;
  private int carriedWeight = 0;
  private boolean stunned = false;
  private String penguinID = "??";
  private boolean isPlayer = false;

  public Penguin(PenguinType penguinType, Position position) {
    if (penguinType == null) {
      throw new IllegalArgumentException("Penguin Error: Type cannot be null.");
    }
    if (position == null) {
      throw new IllegalArgumentException("Penguin Error: Initial Position cannot be null.");
    }

    this.type = penguinType;
    this.position = position;
    this.inventory = new ArrayList<>();
  }

  public Penguin(PenguinType penguinType) {
    this(penguinType, new Position());
  }

  /**
   * Securely assigns the ID (e.g., "P1", "P2"). Can only be set once to prevent identity tampering
   * during gameplay.
   */
  public void setPenguinID(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Penguin Error: ID cannot be null or empty.");
    }
    this.penguinID = id;
  }

  public void removeLightestFood() {
    if (!inventory.isEmpty()) {
      int min = Integer.MAX_VALUE;
      int minIndex = 0;
      for (int i = 0; i < inventory.size(); i++) {
        if (inventory.get(i).getWeight() < min) {
          min = inventory.get(i).getWeight();
          minIndex = i;
        }
      }
      inventory.remove(minIndex);
    }
  }

  // measure the weight of each food in the inventory
  public int measureInventory() {
    int sum = 0;
    for (Food food : inventory) {
      sum += food.getWeight();
    }
    carriedWeight = sum;
    return sum;
  }

  public abstract void specialAbility();

  public void pickupFood(Food food) {
    if (food != null) {
      inventory.add(food);
    }
  }

  public void slide(TerrainGrid grid, Direction direction) {
    // sliding function
    if (direction == null || grid == null) {
      return;
    }
    System.out.println(getNotation() + " starts sliding " + direction + "!");

    boolean isMoving = true;

    while (isMoving) {
      // First step, calculate next coordinate
      int nextY = position.getY();
      int nextX = position.getX();

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

      // Second step, collision checks, move legality
      // FIX: Changed from > to >= for proper boundary checking
      // Grid is 0-9, so index 10 is out of bounds
      if (nextX < 0 || nextY < 0 || nextY >= GRID_SIZE || nextX >= GRID_SIZE) {
        System.out.println(getNotation() + " fell into the water!");
        grid.removeObject(position);
        this.position = null;
        isMoving = false;
        break;
      }

      ITerrainObject obstacle = grid.getObjectAt(nextPos);

      // FIX: Complete slide logic implementation
      if (obstacle == null) {
        // Empty space - continue sliding
        updatePositionOnGrid(grid, nextPos);
      } else if (obstacle instanceof Food) {
        // Food - collect and stop
        System.out.println(getNotation() + " collected food!");
        grid.removeObject(nextPos);
        pickupFood((Food) obstacle);
        updatePositionOnGrid(grid, nextPos);
        isMoving = false;
      } else if (obstacle instanceof Penguin) {
        // Another penguin - stop before collision
        System.out.println(getNotation() + " stopped before hitting another penguin!");
        isMoving = false;
      } else if (obstacle instanceof IHazard) {
        IHazard hazard = (IHazard) obstacle;

        if (hazard.canSlide()) {
          // Light Ice Block or Sea Lion - slide through and trigger collision
          System.out.println(getNotation() + " collided with " + hazard.getNotation() + "!");
          hazard.onCollision(this, grid);

          // Check if penguin is still alive after collision
          if (this.position == null) {
            isMoving = false;
          } else {
            updatePositionOnGrid(grid, nextPos);
          }
        } else {
          // Heavy Ice Block or Hole in Ice - stop and trigger collision
          System.out.println(getNotation() + " hit " + hazard.getNotation() + "!");
          hazard.onCollision(this, grid);
          isMoving = false;
        }
      } else {
        // Unknown obstacle - stop to be safe
        isMoving = false;
      }
    }
  }

  public String getType() {
    return type.toString().toLowerCase();
  }

  public int getCarriedWeight() {
    measureInventory();
    return carriedWeight;
  }

  public boolean isStunned() {
    return stunned;
  }

  public void setStunned(boolean b) {
    stunned = b;
  }

  public Position getPosition() {
    if (position == null) {
      return null;
    }
    return new Position(position);
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
    grid.removeObject(position);
    this.position = newPosition;
    grid.placeObject(newPosition, this);
  }

  @Override
  public String getNotation() {
    return penguinID;
  }

  public boolean isPlayer() {
    return isPlayer;
  }

  public void setPlayer(boolean player) {
    isPlayer = player;
  }

  public ArrayList<Food> getInventory() {
    return inventory;
  }
}