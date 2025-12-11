package game;

import interfaces.ITerrainObject;
import models.Position;

/**
 * Represents the 10x10 icy terrain grid where the game takes place. This class manages the
 * placement, retrieval, and removal of all game objects including penguins, hazards, and food.
 *
 * <p>It encapsulates a 2D array and provides safe methods to interact with specific coordinates,
 * ensuring inputs are within valid boundaries.
 */
public class TerrainGrid {
  /**
   * The constant dimension size of the square grid. Defined as 10 according to the game
   * specifications.
   */
  public static final int GRID_SIZE = 10;

  // The Game Grid Itself
  private ITerrainObject[][] terrainGrid = new ITerrainObject[GRID_SIZE][GRID_SIZE];

  /**
   * Replaces the entire internal grid with a new 2D array of objects. Primarily used for
   * initialization or testing purposes.
   *
   * @param terrainGrid The new 2D array to set as the game grid.
   */
  public void setTerrainGrid(ITerrainObject[][] terrainGrid) {
    this.terrainGrid = terrainGrid;
  }

  /**
   * Places a specific terrain object onto the grid at the designated position. This method
   * overwrites any object currently existing at that location.
   *
   * @param position The coordinates where the object should be placed.
   * @param object The object to place on the grid.
   * @throws IllegalArgumentException If the position is null or falls outside the grid boundaries.
   */
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

  /**
   * Retrieves the terrain object located at the specified position. Returns null if the position is
   * empty or if the coordinates are out of bounds.
   *
   * @param position The coordinates to query.
   * @return The object at the specified location, or null if empty or invalid.
   */
  public ITerrainObject getObjectAt(Position position) {
    if (position == null || !isValidPosition(position)) {
      return null;
    }
    return terrainGrid[position.getY()][position.getX()];
  }

  /**
   * Removes the object at the specified position by setting the grid cell to null. If the position
   * is invalid or already empty, this operation does nothing.
   *
   * @param position The coordinates of the object to remove.
   */
  public void removeObject(Position position) {
    if (position != null && isValidPosition(position)) {
      terrainGrid[position.getY()][position.getX()] = null;
    }
  }

  /**
   * Internal helper method to verify if a position is within the 10x10 grid boundaries. Checks if x
   * and y coordinates are non-negative and less than the grid size.
   *
   * @param position The position to validate.
   * @return True if the x and y coordinates are between 0 and 9 inclusive.
   */
  private boolean isValidPosition(Position position) {
    int x = position.getX();
    int y = position.getY();
    return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
  }
}
