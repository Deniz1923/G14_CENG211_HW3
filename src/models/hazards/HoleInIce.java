package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class HoleInIce extends Hazard {
  public HoleInIce(Position position) {
    super(position, false, HazardType.HOLE_IN_ICE);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    penguin.setStunned(true); // ölüyo işte ama stunned yazdım şmidilik
  }
}
