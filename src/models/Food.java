package models;

import enums.FoodType;
import interfaces.ITerrainObject;

public class Food implements ITerrainObject {
  private final FoodType type;
  private final int weight;
  private Position position;

  public Food(FoodType type, Position position, int weight) {
    this.type = type;
    this.weight = weight;
    this.position = position;
  }

  public FoodType getType() {
    return type;
  }

  public int getWeight() {
    return weight;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  @Override
  public String getNotation() {
    return type.getNotation();
  }
}
