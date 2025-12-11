package models.hazards;

import enums.Direction;
import enums.HazardType;
import game.TerrainGrid;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;
import models.penguins.Penguin;

/**
 * Represents a sea lion that bounces penguins in the opposite direction.
 * When a penguin collides with a SeaLion, the penguin bounces back and
 * starts sliding in the opposite direction, while the SeaLion inherits
 * the penguin's original momentum and starts sliding forward.
 *
 * <p>Characteristics:</p>
 * <ul>
 *   <li>Can slide (canSlide = true)</li>
 *   <li>Bounces penguins in opposite direction</li>
 *   <li>Inherits movement from colliding penguin</li>
 *   <li>Can fall off edges of the grid</li>
 *   <li>Can destroy food items in its path</li>
 *   <li>Can plug holes when falling into them</li>
 *   <li>Stops when colliding with other penguins</li>
 * </ul>
 *
 * <p>Collision mechanics:</p>
 * <ul>
 *   <li>Penguin bounces backward (opposite direction)</li>
 *   <li>SeaLion slides forward (original direction)</li>
 *   <li>Both can collide with other objects during their slides</li>
 * </ul>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class SeaLion extends Hazard {

    /**
     * Constructs a SeaLion at the specified position.
     * Sea lions can slide when pushed and bounce penguins backward.
     *
     * @param position The position where this sea lion is placed
     * @throws IllegalArgumentException if position is null
     */
    public SeaLion(Position position) {
        super(position, true, HazardType.SEA_LION);
    }

    /**
     * Handles collision with a penguin.
     * The penguin bounces back in the opposite direction, while this
     * SeaLion inherits the penguin's original movement direction.
     *
     * <p>The actual sliding logic for both the penguin (bouncing back)
     * and the SeaLion (sliding forward) is handled by the calling code
     * in the Penguin.slide() method to avoid circular dependencies.</p>
     *
     * <p>Collision sequence:</p>
     * <ol>
     *   <li>Penguin collides with SeaLion</li>
     *   <li>Collision message is printed</li>
     *   <li>SeaLion is removed from current position</li>
     *   <li>SeaLion slides in penguin's original direction</li>
     *   <li>Penguin bounces in opposite direction</li>
     * </ol>
     *
     * @param penguin The penguin that collided with this sea lion
     * @param grid The terrain grid (used by calling code for movement)
     * @throws IllegalArgumentException if penguin is null
     * @throws IllegalArgumentException if grid is null
     */
    @Override
    public void onCollision(Penguin penguin, TerrainGrid grid) {
        if (penguin == null) {
            throw new IllegalArgumentException(
                    "SeaLion Error: Penguin cannot be null during collision."
            );
        }
        if (grid == null) {
            throw new IllegalArgumentException(
                    "SeaLion Error: TerrainGrid cannot be null during collision."
            );
        }

        try {
            // Print collision message
            // The bounce and slide logic is handled in Penguin.slide()
            // to avoid circular method calls
            System.out.println(penguin.getNotation() +
                    " bounces off the SeaLion!");
        } catch (Exception e) {
            System.err.println("Error during SeaLion collision: " + e.getMessage());
        }
    }

    /**
     * Returns the symbol representing this sea lion on the grid.
     *
     * @return "SL" - the abbreviation for Sea Lion
     */
    @Override
    public String getSymbol() {
        return "SL";
    }

    /**
     * Returns a string representation of this sea lion.
     *
     * @return A descriptive string including position information
     */
    @Override
    public String toString() {
        return "Sea Lion (SL) at " + getPosition().displayPosition();
    }
}