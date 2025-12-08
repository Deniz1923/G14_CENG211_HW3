package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class SeaLion extends Hazard {
  public SeaLion(Position position) {
    super(position, true, HazardType.SEA_LION);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    // TODO it is hard
  }
}
