package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

/**
 * Represents a light ice block that can slide when hit.
 * When a penguin or another sliding hazard collides with a LightIceBlock,
 * the colliding object stops and the LightIceBlock starts sliding in the
 * transmitted direction. The penguin is stunned and skips their next turn.
 *
 * <p>Characteristics:</p>
 * <ul>
 *   <li>Can slide (canSlide = true)</li>
 *   <li>Stuns colliding penguin (they skip next turn)</li>
 *   <li>Starts sliding when hit by penguin or sliding hazard</li>
 *   <li>Can fall off the edges of the grid</li>
 *   <li>Can destroy food items in its path</li>
 *   <li>Can plug holes when falling into them</li>
 * </ul>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class LightIceBlock extends Hazard {

    /**
     * Constructs a LightIceBlock at the specified position.
     * Light ice blocks can slide when pushed by penguins or other objects.
     *
     * @param position The position where this light ice block is placed
     * @throws IllegalArgumentException if position is null
     */
    public LightIceBlock(Position position) {
        super(position, true, HazardType.LIGHT_ICE_BLOCK);
    }

    /**
     * Handles collision with a penguin.
     * The penguin is stunned due to hitting the ice block and their next
     * turn is automatically skipped. The ice block will then slide in the
     * direction transmitted by the collision.
     *
     * <p>Stun effect: The penguin's isStunned flag is set to true, causing
     * them to skip their next turn. The flag is automatically cleared after
     * the skipped turn.</p>
     *
     * @param penguin The penguin that collided with this light ice block
     * @param grid The terrain grid (not used in this implementation)
     * @throws IllegalArgumentException if penguin is null
     * @throws IllegalArgumentException if grid is null
     */
    @Override
    public void onCollision(Penguin penguin, TerrainGrid grid) {
        if (penguin == null) {
            throw new IllegalArgumentException(
                    "LightIceBlock Error: Penguin cannot be null during collision."
            );
        }
        if (grid == null) {
            throw new IllegalArgumentException(
                    "LightIceBlock Error: TerrainGrid cannot be null during collision."
            );
        }

        try {
            // Print collision message
            System.out.println(penguin.getNotation() +
                    " is stunned by hitting the ice block!");

            // Stun the penguin - they will skip their next turn
            penguin.setStunned(true);
        } catch (Exception e) {
            System.err.println("Error during LightIceBlock collision: " + e.getMessage());
        }
    }

    /**
     * Returns the symbol representing this light ice block on the grid.
     *
     * @return "LB" - the abbreviation for Light Ice Block
     */
    @Override
    public String getSymbol() {
        return "LB";
    }

    /**
     * Returns a string representation of this light ice block.
     *
     * @return A descriptive string including position information
     */
    @Override
    public String toString() {
        return "Light Ice Block (LB) at " + getPosition().displayPosition();
    }
}