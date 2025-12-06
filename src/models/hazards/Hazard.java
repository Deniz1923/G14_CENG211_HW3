package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import interfaces.ITerrainObject;
import models.Position;
import models.penguins.Penguin;

public abstract class Hazard implements ITerrainObject {
  protected Position position;
  protected boolean canSlide;
  protected HazardType hazardType;

  public Hazard(HazardType hazardType, boolean canSlide) {
    this.canSlide = canSlide;
    this.hazardType = hazardType;
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
}
