package models;

import enums.FoodType;
import interfaces.ITerrainObject;

import java.util.Random;

public class Food implements ITerrainObject {
  Random random = new Random();
  private FoodType type;
  private int weight;
  private Position position;

  public Food(FoodType type, Position position) {
    this.type = type;
    weight = random.nextInt(1, 6);
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
  public String getSymbol() {
    return "";
  }
}
