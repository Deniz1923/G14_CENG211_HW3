package interfaces;

import models.Position;

/**
 * The common interface for all entities that can exist on the Icy Terrain Grid.
 *
 * <p>This includes Penguins, Food Items, and Hazards. The Grid is composed primarily of objects
 * implementing this interface.
 */
public interface ITerrainObject {

  /**
   * Retrieves the current coordinate position of the object on the grid.
   *
   * @return The Position object (row, col)
   */
  Position getPosition();

  /**
   * Updates the position of the object on the grid.
   *
   * @param position The new Position to set.
   */
  void setPosition(Position position);

  /**
   * Return the Notation of the ITerrainObject.<br>
   * If it is a Penguin, returns penguinID according to the specification.
   */
  String getNotation();
}
