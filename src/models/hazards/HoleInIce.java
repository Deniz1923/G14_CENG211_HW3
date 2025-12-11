package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class HoleInIce extends Hazard {
  private boolean isPlugged = false;

  public HoleInIce(Position position) {
    super(position, false, HazardType.HOLE_IN_ICE);
  }

  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    if (isPlugged) {
      // Penguin can pass through plugged hole
      return;
    }

    System.out.println(penguin.getNotation() + " falls into " + getNotation() + "!");
    grid.removeObject(penguin.getPosition());
    penguin.setPosition(null);
  }

  public void plug() {
    isPlugged = true;
  }

  public boolean isPlugged() {
    return isPlugged;
  }

  @Override
  public String getNotation() {
    return isPlugged ? "PH" : "HI";
  }
}
