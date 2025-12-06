package models.hazards;

import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class HoleInIce extends Hazard {
  public HoleInIce(Position position) {
    super(position, false);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    penguin.setStunned(true); // ölüyo işte ama stunned yazdım şmidilik
  }

  @Override
  public String getSymbol() {
    return "HB";
  }
}
