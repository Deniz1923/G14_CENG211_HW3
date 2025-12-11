package interfaces;

import game.TerrainGrid;
import models.penguins.Penguin;

/**
 * Interface defining the behavior of Hazards on the Terrain. Extends ITerrainObject as Hazards are
 * in the Terrain.
 */
public interface IHazard extends ITerrainObject {

  /**
   * Handles the logic when a Penguin collides with this Hazard.
   *
   * <p>This method should implement specific consequences based on HazardType:
   *
   * <ul>
   *   <li><b>LightIceBlock:</b> Stuns penguin, block starts sliding.
   *   <li><b>HeavyIceBlock:</b> Penguin stops, loses lightest food item.
   *   <li><b>SeaLion:</b> Penguin bounces back, Sea Lion slides forward.
   *   <li><b>HoleInIce:</b> Penguin is removed from the game.
   * </ul>
   *
   * @param penguin The penguin that collided with the hazard.
   * @param grid The game grid (needed to update positions or remove objects).
   */
  void onCollision(Penguin penguin, TerrainGrid grid);

  /**
   * Determines if this specific hazard is capable of sliding on the ice.
   *
   * <p>Used to check if the hazard should continue moving after a collision (e.g., LightIceBlock,
   * SeaLion).
   *
   * @return true if the hazard can slide, false if it is stationary.
   */
  boolean canSlide();
}
