package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.penguins.Penguin;

public class HoleInIce extends Hazard {

  public HoleInIce() {
    super(HazardType.HOLE_IN_ICE, false);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {}

  @Override
  public String getSymbol() {
    return "HI";
  }
}
