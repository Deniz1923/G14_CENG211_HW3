package interfaces;

import game.TerrainGrid;
import models.penguins.Penguin;

public interface IHazard extends ITerrainObject {
  void onCollision(Penguin penguin, TerrainGrid grid);

  boolean canSlide();
}
