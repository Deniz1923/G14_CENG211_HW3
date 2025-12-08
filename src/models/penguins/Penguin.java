package models.penguins;

import static game.TerrainGrid.GRID_SIZE;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
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
  private int measureInventory() {
    int sum = 0;
    for (Food food : inventory) {
      sum += food.getWeight();
    }
    carriedWeight = sum;
    return sum;
  }

  abstract void specialAbility();

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
      // x,y format in here to be simple looking
      Position nextPos = new Position(nextX, nextY);

      // Second step, collision checks, move legality
      // Falling into water case
      if (nextX < 0 || nextY < 0 || nextY > GRID_SIZE || nextX > GRID_SIZE) {
        System.out.println(getNotation() + " fell into the water!");

        grid.removeObject(getPosition()); // empty the penguin's before moving slot
        this.setPosition(null);

        isMoving = false;
        break;
      }
      ITerrainObject obstacle = grid.getObjectAt(nextPos);

      if (obstacle != null) {

        if (obstacle instanceof Food) {
          System.out.println(getNotation() + " collected food!");
          grid.removeObject(nextPos);

          pickupFood((Food) obstacle); // already checked so not a problem
          updatePositionOnGrid(grid, nextPos);
          isMoving = false;
        }
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
    // deep copy inside Position class
    return position.getPosition();
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {

    grid.removeObject(position);

    this.setPosition(newPosition);

    grid.placeObject(newPosition, this); // this : penguin
  }

  @Override
  public String getNotation() {
    // Returns P1, P2, or P3
    return penguinID;
  }
}
