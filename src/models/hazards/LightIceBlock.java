package models.hazards;

import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class LightIceBlock extends Hazard {

  public LightIceBlock(Position position) {
    super(position,true);
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
