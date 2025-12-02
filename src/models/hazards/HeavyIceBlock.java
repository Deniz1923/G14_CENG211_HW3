package models.hazards;

import game.TerrainGrid;
import models.penguins.Penguin;

public class HeavyIceBlock extends Hazard {

  public HeavyIceBlock() {
    super(false);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    penguin.removeLightestFood();
  }

  @Override
  public String getSymbol() {
    return "HB";
  }
}
