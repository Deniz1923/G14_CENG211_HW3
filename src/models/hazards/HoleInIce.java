package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

/**
 * Represents a hole in the ice that eliminates anything that falls into it.
 * When a penguin slides into a HoleInIce, they are removed from the game.
 * When a sliding hazard (LightIceBlock or SeaLion) falls into it, the hole
 * becomes plugged and can no longer eliminate objects.
 *
 * <p>Characteristics:</p>
 * <ul>
 *   <li>Cannot slide (canSlide = false)</li>
 *   <li>Eliminates penguins that fall in (removed from game)</li>
 *   <li>Can be plugged by sliding hazards (LightIceBlock or SeaLion)</li>
 *   <li>Once plugged, sliding objects can pass through safely</li>
 *   <li>Notation changes from "HI" to "PH" when plugged</li>
 * </ul>
 *
 * <p>When eliminated, the penguin retains any food they collected,
 * which is still counted in the final scoring.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class HoleInIce extends Hazard {
    /** Indicates whether this hole has been plugged by a sliding hazard */
    private boolean isPlugged = false;

    /**
     * Constructs a HoleInIce at the specified position.
     * The hole starts unplugged and will eliminate anything that falls in.
     *
     * @param position The position where this hole is located
     * @throws IllegalArgumentException if position is null
     */
    public HoleInIce(Position position) {
        super(position, false, HazardType.HOLE_IN_ICE);
    }

    /**
     * Handles collision with a penguin.
     * If the hole is plugged, the penguin can pass through safely.
     * If unplugged, the penguin falls in and is eliminated from the game.
     *
     * <p>When eliminated:</p>
     * <ul>
     *   <li>Penguin is removed from the grid</li>
     *   <li>Position is set to null (indicating elimination)</li>
     *   <li>Collected food is retained for final scoring</li>
     *   <li>Remaining turns are automatically skipped</li>
     * </ul>
     *
     * @param penguin The penguin that collided with this hole
     * @param grid The terrain grid where the elimination occurs
     * @throws IllegalArgumentException if penguin is null
     * @throws IllegalArgumentException if grid is null
     */
    @Override
    public void onCollision(Penguin penguin, TerrainGrid grid) {
        if (penguin == null) {
            throw new IllegalArgumentException(
                    "HoleInIce Error: Penguin cannot be null during collision."
            );
        }
        if (grid == null) {
            throw new IllegalArgumentException(
                    "HoleInIce Error: TerrainGrid cannot be null during collision."
            );
        }

        try {
            // If hole is plugged, penguin can pass through safely
            if (isPlugged) {
                return;
            }

            // Penguin falls into the unplugged hole
            System.out.println(penguin.getNotation() + " falls into " +
                    getNotation() + "!");

            // Remove penguin from the grid
            Position penguinPos = penguin.getPosition();
            if (penguinPos != null) {
                grid.removeObject(penguinPos);
            }

            // Set position to null to indicate elimination
            penguin.setPosition(null);
        } catch (Exception e) {
            System.err.println("Error during HoleInIce collision: " + e.getMessage());
        }
    }

    /**
     * Plugs this hole with a sliding hazard.
     * Once plugged, objects can slide over this position safely.
     * The notation changes from "HI" to "PH" (Plugged Hole).
     *
     * <p>This occurs when a LightIceBlock or SeaLion slides into the hole.
     * The sliding hazard is removed from the game in the process.</p>
     */
    public void plug() {
        this.isPlugged = true;
    }

    /**
     * Checks if this hole has been plugged.
     *
     * @return true if the hole is plugged, false if it's still dangerous
     */
    public boolean isPlugged() {
        return isPlugged;
    }

    /**
     * Returns the notation for this hole on the grid.
     * Returns "PH" if plugged, "HI" if unplugged.
     *
     * @return "PH" for plugged hole, "HI" for hole in ice
     */
    @Override
    public String getNotation() {
        return isPlugged ? "PH" : "HI";
    }

    /**
     * Returns the symbol representing this hole on the grid.
     * Same as getNotation() - "PH" if plugged, "HI" if unplugged.
     *
     * @return "PH" for plugged hole, "HI" for hole in ice
     */
    @Override
    public String getSymbol() {
        return isPlugged ? "PH" : "HI";
    }

    /**
     * Returns a string representation of this hole.
     *
     * @return A descriptive string including plugged status and position
     */
    @Override
    public String toString() {
        String status = isPlugged ? "Plugged Hole (PH)" : "Hole In Ice (HI)";
        return status + " at " + getPosition().displayPosition();
    }
}