package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.penguins.Penguin;

public class SeaLion extends Hazard {

  public SeaLion() {
    super(HazardType.SEA_LION, true);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {}

  @Override
  public String getSymbol() {
    return "SL";
  }
}
