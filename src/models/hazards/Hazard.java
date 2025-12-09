package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import interfaces.IHazard;
import models.Position;
import models.penguins.Penguin;

public abstract class Hazard implements IHazard {
  protected final boolean canSlide;
  protected final HazardType hazardType;
  protected Position position;

  public Hazard(Position position, boolean canSlide, HazardType hazardType) {
    if (position == null) {
      throw new IllegalArgumentException("Hazard position cannot be null");
    }
    if (hazardType == null) {
      throw new IllegalArgumentException("HazardType cannot be null");
    }
    this.position = position;
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

  @Override
  public String getNotation() {
    return hazardType.getNotation();
  }

  public abstract void onCollision(Penguin penguin, TerrainGrid grid);

    public String getSymbol() {
        return null;
    }
}
