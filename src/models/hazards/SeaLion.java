package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

/**
 * Represents a sea lion that bounces penguins in the opposite direction.
 * When a penguin collides with a SeaLion, the penguin bounces back and
 * starts sliding in the opposite direction, while the SeaLion inherits
 * the penguin's original momentum and starts sliding forward.
 *
 * <p><b>Characteristics</b>:
 *   <p>Can slide (canSlide = true)
 *   <p>Bounces penguins in opposite direction
 *   <p>Inherits movement from colliding penguin
 *   <p>Can fall off edges of the grid
 *   <p>Can destroy food items in its path
 *   <p>Can plug holes when falling into them
 *   <p>Stops when colliding with other penguins
 *   <p>
 *   <p><b>Collision mechanics:</b>
 *   <p>Penguin bounces backward (opposite direction)
 *   <p>SeaLion slides forward (original direction)
 *   <p>Both can collide with other objects during their slides
 * </ul>
 *
 * @author CENG211 14. Group
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
     * <p>Collision sequence:
     *   <p>Penguin collides with SeaLion
     *   <p>Collision message is printed
     *   <p>SeaLion is removed from current position
     *   <p>SeaLion slides in penguin's original direction
     *   <p>Penguin bounces in opposite direction
     *
     * @param penguin The penguin that collided with this sea lion
     * @param grid    The terrain grid (used by calling code for movement)
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
     * Returns a string representation of this sea lion.
     *
     * @return A descriptive string including position information
     */
    @Override
    public String toString() {
        return "Sea Lion (SL) at " + getPosition().displayPosition();
    }
}