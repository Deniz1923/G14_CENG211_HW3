package game;

import interfaces.ITerrainObject;
import models.Position;

/**
 * Represents the 10x10 icy terrain grid where the game takes place.
 * The grid stores all game objects (penguins, hazards, and food items)
 * and provides methods to manipulate them safely.
 *
 * <p>Grid characteristics:</p>
 * <ul>
 *   <li>Fixed size of 10x10 squares</li>
 *   <li>Surrounded by water on all sides</li>
 *   <li>Each square can contain at most one object at a time</li>
 *   <li>Coordinates range from (0,0) to (9,9)</li>
 * </ul>
 *
 * <p>The grid uses a 2D array to store ITerrainObjects, allowing
 * polymorphic handling of penguins, hazards, and food items.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class TerrainGrid {
  /** The size of the grid (10x10) */
  public static final int GRID_SIZE = 10;

  /** The 2D array representing the game grid */
  private ITerrainObject[][] terrainGrid = new ITerrainObject[GRID_SIZE][GRID_SIZE];

  /**
   * Gets the underlying 2D array of the terrain grid.
   * Use with caution - direct modification can break game state.
   *
   * @return The 2D array of terrain objects
   */
  public ITerrainObject[][] getTerrainGrid() {
    return terrainGrid;
  }

  /**
   * Sets the entire terrain grid to a new 2D array.
   * This is primarily used for testing or loading saved games.
   *
   * @param terrainGrid The new terrain grid array
   * @throws IllegalArgumentException if terrainGrid is null
   * @throws IllegalArgumentException if dimensions are not 10x10
   */
  public void setTerrainGrid(ITerrainObject[][] terrainGrid) {
    if (terrainGrid == null) {
      throw new IllegalArgumentException(
              "TerrainGrid Error: Cannot set grid to null."
      );
    }
    if (terrainGrid.length != GRID_SIZE) {
      throw new IllegalArgumentException(
              "TerrainGrid Error: Grid must have exactly " + GRID_SIZE + " rows."
      );
    }
    for (int i = 0; i < terrainGrid.length; i++) {
      if (terrainGrid[i] == null || terrainGrid[i].length != GRID_SIZE) {
        throw new IllegalArgumentException(
                "TerrainGrid Error: Each row must have exactly " + GRID_SIZE + " columns."
        );
      }
    }
    this.terrainGrid = terrainGrid;
  }

  /**
   * Places an object at the specified position on the grid.
   * The position must be valid (within grid bounds).
   *
   * <p>This method will overwrite any existing object at that position,
   * so callers should check if the square is empty first if needed.</p>
   *
   * @param position The position where the object should be placed
   * @param object The terrain object to place (penguin, hazard, or food)
   * @throws IllegalArgumentException if position is null
   * @throws IllegalArgumentException if position is out of bounds
   */
  public void placeObject(Position position, ITerrainObject object) {
    if (position == null) {
      throw new IllegalArgumentException(
              "TerrainGrid Error: Cannot place object at position NULL."
      );
    }
    if (!isValidPosition(position)) {
      throw new IllegalArgumentException(
              "TerrainGrid Error: Position " + position.displayPosition() +
                      " is not a valid position. Must be between (0,0) and (" +
                      (GRID_SIZE-1) + "," + (GRID_SIZE-1) + ")."
      );
    }

    try {
      terrainGrid[position.getY()][position.getX()] = object;
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException(
              "TerrainGrid Error: Array index out of bounds for position " +
                      position.displayPosition(), e
      );
    }
  }

  /**
   * Gets the object at the specified position on the grid.
   * Returns null if the position is empty or invalid.
   *
   * @param position The position to check
   * @return The terrain object at that position, or null if empty/invalid
   */
  public ITerrainObject getObjectAt(Position position) {
    if (position == null || !isValidPosition(position)) {
      return null;
    }

    try {
      return terrainGrid[position.getY()][position.getX()];
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println("TerrainGrid Warning: Array access error at " +
              position.displayPosition());
      return null;
    }
  }

  /**
   * Removes the object at the specified position, leaving it empty.
   * If the position is invalid or already empty, no action is taken.
   *
   * @param position The position to clear
   */
  public void removeObject(Position position) {
    if (position != null && isValidPosition(position)) {
      try {
        terrainGrid[position.getY()][position.getX()] = null;
      } catch (ArrayIndexOutOfBoundsException e) {
        System.err.println("TerrainGrid Warning: Cannot remove object at " +
                position.displayPosition());
      }
    }
  }

  /**
   * Checks if a position is within the valid grid boundaries.
   * Valid positions have x and y coordinates between 0 and (GRID_SIZE-1).
   *
   * @param position The position to validate
   * @return true if the position is within bounds, false otherwise
   */
  private boolean isValidPosition(Position position) {
    if (position == null) {
      return false;
    }

    int x = position.getX();
    int y = position.getY();
    return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
  }

  /**
   * Checks if a specific position on the grid is empty.
   *
   * @param position The position to check
   * @return true if the position is valid and empty, false otherwise
   */
  public boolean isEmpty(Position position) {
    return getObjectAt(position) == null;
  }

  /**
   * Clears the entire grid, removing all objects.
   * This is useful for testing or resetting the game.
   */
  public void clearGrid() {
    for (int y = 0; y < GRID_SIZE; y++) {
      for (int x = 0; x < GRID_SIZE; x++) {
        terrainGrid[y][x] = null;
      }
    }
  }

  /**
   * Counts the total number of objects currently on the grid.
   *
   * @return The number of non-null objects on the grid
   */
  public int countObjects() {
    int count = 0;
    for (int y = 0; y < GRID_SIZE; y++) {
      for (int x = 0; x < GRID_SIZE; x++) {
        if (terrainGrid[y][x] != null) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Returns a string representation of the grid showing object count.
   *
   * @return A descriptive string
   */
  @Override
  public String toString() {
    return "TerrainGrid [" + GRID_SIZE + "x" + GRID_SIZE + "] with " +
            countObjects() + " objects";
  }
}