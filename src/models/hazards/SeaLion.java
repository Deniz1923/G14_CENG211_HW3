package models.hazards;

import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class SeaLion extends Hazard {
  public SeaLion(Position position) {
    super(position, true);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {}

  @Override
  public String getSymbol(){
    return "SL";
  }
}
