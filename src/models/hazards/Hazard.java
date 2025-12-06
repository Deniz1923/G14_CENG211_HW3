package models.hazards;

import game.TerrainGrid;
import interfaces.ITerrainObject;
import models.Position;
import models.penguins.Penguin;

public abstract class Hazard implements ITerrainObject {
  protected Position position;
  protected boolean canSlide;

  public Hazard(Position position, boolean canSlide) {
    this.position = position;
    this.canSlide = canSlide;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  public boolean canSlide() {
    return canSlide;
  }

  public abstract void onCollision(Penguin penguin, TerrainGrid grid);
  public String getSymbol(){
    return "HZ";
  }
}
