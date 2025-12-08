package game;

import interfaces.ITerrainObject;
import models.Position;

public class TerrainGrid {
  public static final int GRID_SIZE = 10;
  // The Game Grid Itself
  private ITerrainObject[][] terrainGrid = new ITerrainObject[GRID_SIZE][GRID_SIZE];

  public ITerrainObject[][] getTerrainGrid() {
    return terrainGrid;
  }

  public void setTerrainGrid(ITerrainObject[][] terrainGrid) {
    this.terrainGrid = terrainGrid;
  }

  public void placeObject(Position position, ITerrainObject object) {
    if (position == null) {
      throw new IllegalArgumentException("Cannot place object at position NULL");
    }
    if (!isValidPosition(position)) {
      throw new IllegalArgumentException(
          "Position " + position.displayPosition() + " is not a valid position.");
    }
    terrainGrid[position.getY()][position.getX()] = object;
  }

  public ITerrainObject getObjectAt(Position position) {
    if (position == null || !isValidPosition(position)) {
      return null;
    }
    return terrainGrid[position.getY()][position.getX()];
  }

  public void removeObject(Position position) {
    if (position != null && isValidPosition(position)) {
      terrainGrid[position.getY()][position.getX()] = null;
    }
  }

  /** Helper method to validate boundaries. */
  private boolean isValidPosition(Position position) {
    int x = position.getX();
    int y = position.getY();
    return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
  }
}
