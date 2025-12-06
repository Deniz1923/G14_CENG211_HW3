package game;

import interfaces.ITerrainObject;
import models.Position;

public class TerrainGrid {

  // The Game Grid Itself
  private ITerrainObject[][] terrainGrid = new ITerrainObject[10][10];

  public ITerrainObject[][] getTerrainGrid() {
    return terrainGrid;
  }

  public void setTerrainGrid(ITerrainObject[][] terrainGrid) {
    this.terrainGrid = terrainGrid;
  }
  public void placeObject(Position position, ITerrainObject object){
      terrainGrid[position.getX()][position.getY()] = object;
  }
  public ITerrainObject getObjectAt(Position position){
    if (position == null) {
      return null;
    }

    int x = position.getX();
    int y = position.getY();

    if (x < 0 || x >= 10 || y < 0 || y >= 10) {
      return null;
    }

    return terrainGrid[y][x];
  }

  public void removeObject(Position pos) {
    terrainGrid[pos.getY()][pos.getX()] = null;
  }
}
