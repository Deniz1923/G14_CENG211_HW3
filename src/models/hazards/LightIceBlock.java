package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class LightIceBlock extends Hazard {

  public LightIceBlock(Position position) {
    super(position, true, HazardType.LIGHT_ICE_BLOCK);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    penguin.setStunned(true);
  }
}
