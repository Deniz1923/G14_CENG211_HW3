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
    // FIX: Remove stunned setting - penguin falls in and is eliminated
    // Setting position to null indicates elimination
    System.out.println(penguin.getNotation() + " fell through the hole in the ice!");
    grid.removeObject(penguin.getPosition());
    penguin.setPosition(null);
  }
}
