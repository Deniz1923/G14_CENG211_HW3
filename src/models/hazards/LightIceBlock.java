package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.penguins.Penguin;

public class LightIceBlock extends Hazard {

  public LightIceBlock() {
    super(HazardType.LIGHT_ICE_BLOCK, true);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    penguin.setStunned(true);
  }

  @Override
  public String getSymbol() {
    return "LB";
  }
}
