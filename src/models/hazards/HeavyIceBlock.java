package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

/**
 * Represents a heavy ice block that cannot be moved.
 * When a penguin collides with a HeavyIceBlock, they stop immediately
 * and lose the lightest food item from their inventory as a penalty.
 *
 * <p>Characteristics:</p>
 * <ul>
 *   <li>Cannot slide (canSlide = false)</li>
 *   <li>Acts as an immovable obstacle</li>
 *   <li>Removes lightest food from colliding penguin</li>
 *   <li>If penguin carries no food, no penalty is applied</li>
 * </ul>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class HeavyIceBlock extends Hazard {

  /**
   * Constructs a HeavyIceBlock at the specified position.
   * Heavy ice blocks are immovable obstacles on the terrain.
   *
   * @param position The position where this heavy ice block is placed
   * @throws IllegalArgumentException if position is null
   */
  public HeavyIceBlock(Position position) {
    super(position, false, HazardType.HEAVY_ICE_BLOCK);
  }

  /**
   * Handles collision with a penguin.
   * The penguin stops at their current position and loses their lightest
   * food item. If the penguin has no food, they are unaffected.
   *
   * <p>The penalty system ensures that penguins must be strategic about
   * which paths they take to avoid losing valuable food.</p>
   *
   * @param penguin The penguin that collided with this heavy ice block
   * @param grid The terrain grid (not used in this implementation)
   * @throws IllegalArgumentException if penguin is null
   * @throws IllegalArgumentException if grid is null
   */
  @Override
  public void onCollision(Penguin penguin, TerrainGrid grid) {
    if (penguin == null) {
      throw new IllegalArgumentException(
              "HeavyIceBlock Error: Penguin cannot be null during collision."
      );
    }
    if (grid == null) {
      throw new IllegalArgumentException(
              "HeavyIceBlock Error: TerrainGrid cannot be null during collision."
      );
    }

    try {
      // Penguin loses their lightest food item
      penguin.removeLightestFood();
    } catch (Exception e) {
      System.err.println("Error during HeavyIceBlock collision: " + e.getMessage());
    }
  }

  /**
   * Returns the symbol representing this heavy ice block on the grid.
   *
   * @return "HB" - the abbreviation for Heavy Ice Block
   */
  @Override
  public String getSymbol() {
    return "HB";
  }

  /**
   * Returns a string representation of this heavy ice block.
   *
   * @return A descriptive string including position information
   */
  @Override
  public String toString() {
    return "Heavy Ice Block (HB) at " + getPosition().displayPosition();
  }
}