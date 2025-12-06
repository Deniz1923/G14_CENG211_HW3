package models.penguins;

import enums.PenguinType;
import java.util.ArrayList;
import models.Food;
import models.Position;

public abstract class Penguin {
  private final ArrayList<Food> inventory;
  private PenguinType type;
  private Position position;
  private int carriedWeight = 0;
  private boolean stunned = false;

  public Penguin(PenguinType penguinType, Position position) {
    if (penguinType != null) {
      this.type = penguinType;
    }
    this.position = position;
    this.inventory = new ArrayList<>();
  }

  public Penguin(PenguinType penguinType) {
    if (penguinType != null) {
      this.type = penguinType;
    }
    this.position = new Position();
    this.inventory = new ArrayList<>();
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

  private int measureInventory() // measure the weight of each food in the inventory
      {
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

  public void slide() {
    // sliding function
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
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }
}
