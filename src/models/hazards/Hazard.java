package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import interfaces.IHazard;
import models.Position;
import models.penguins.Penguin;

/**
 * Abstract base class for all hazards on the icy terrain.
 * Hazards are obstacles that can affect penguins in various ways, including
 * stunning them, removing food, or eliminating them from the game.
 *
 * <p>There are four types of hazards:</p>
 * <ul>
 *   <li>LightIceBlock - Can slide, stuns penguins on collision</li>
 *   <li>HeavyIceBlock - Cannot move, removes lightest food from penguin</li>
 *   <li>SeaLion - Can slide, bounces penguins in opposite direction</li>
 *   <li>HoleInIce - Cannot move, eliminates anything that falls in</li>
 * </ul>
 *
 * <p>Some hazards can slide on ice (canSlide = true), while others remain
 * stationary. Sliding hazards can be pushed by penguins or other sliding objects.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public abstract class Hazard implements IHazard {
  /** Indicates whether this hazard can slide on ice */
  protected final boolean canSlide;

  /** The type of this hazard */
  protected final HazardType hazardType;

  /** The current position of this hazard on the grid */
  protected Position position;

  /**
   * Constructs a Hazard with specified position, sliding capability, and type.
   *
   * @param position The initial position on the grid
   * @param canSlide Whether this hazard can slide when pushed
   * @param hazardType The type of hazard
   * @throws IllegalArgumentException if position is null
   * @throws IllegalArgumentException if hazardType is null
   */
  public Hazard(Position position, boolean canSlide, HazardType hazardType) {
    if (position == null) {
      throw new IllegalArgumentException(
              "Hazard Error: Position cannot be null."
      );
    }
    if (hazardType == null) {
      throw new IllegalArgumentException(
              "Hazard Error: HazardType cannot be null."
      );
    }

    this.position = position;
    this.canSlide = canSlide;
    this.hazardType = hazardType;
  }

  /**
   * Gets the current position of this hazard on the grid.
   *
   * @return The Position object representing the hazard's location
   */
  @Override
  public Position getPosition() {
    return position;
  }

  /**
   * Sets the position of this hazard on the grid.
   * Used when the hazard is moved or placed on the grid.
   *
   * @param position The new position
   * @throws IllegalArgumentException if position is null
   */
  @Override
  public void setPosition(Position position) {
    if (position == null) {
      throw new IllegalArgumentException(
              "Hazard Error: Cannot set position to null."
      );
    }
    this.position = position;
  }

  /**
   * Checks if this hazard can slide on ice.
   * Sliding hazards can be pushed by penguins or other sliding objects.
   *
   * @return true if the hazard can slide, false otherwise
   */
  @Override
  public boolean canSlide() {
    return canSlide;
  }

  /**
   * Returns the notation used to display this hazard on the grid.
   * The notation is a 2-letter abbreviation:
   * <ul>
   *   <li>LB - LightIceBlock</li>
   *   <li>HB - HeavyIceBlock</li>
   *   <li>SL - SeaLion</li>
   *   <li>HI - HoleInIce (or PH when plugged)</li>
   * </ul>
   *
   * @return The hazard type's notation string
   */
  @Override
  public String getNotation() {
    return hazardType.getNotation();
  }

  /**
   * Handles collision between a penguin and this hazard.
   * Each hazard type has different effects on collision:
   * <ul>
   *   <li>LightIceBlock - Stuns the penguin</li>
   *   <li>HeavyIceBlock - Removes penguin's lightest food</li>
   *   <li>SeaLion - Bounces penguin in opposite direction</li>
   *   <li>HoleInIce - Eliminates the penguin from the game</li>
   * </ul>
   *
   * @param penguin The penguin that collided with this hazard
   * @param grid The terrain grid where the collision occurred
   * @throws IllegalArgumentException if penguin or grid is null
   */
  @Override
  public abstract void onCollision(Penguin penguin, TerrainGrid grid);

  /**
   * Returns the symbol used to represent this hazard.
   * Default implementation returns null; subclasses should override.
   *
   * @return The hazard's symbol string, or null if not implemented
   */
  @Override
  public String getSymbol() {
    return getNotation();
  }

  /**
   * Returns a string representation of this hazard.
   *
   * @return A string containing the hazard type and position
   */
  @Override
  public String toString() {
    return hazardType.toString() + " at " + position.displayPosition();
  }
}