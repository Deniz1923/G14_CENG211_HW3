package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class HeavyIceBlock extends Hazard {


    public HeavyIceBlock(Position position) {
    super(position, false,HazardType.HEAVY_ICE_BLOCK);
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
